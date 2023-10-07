#include <iostream>
#include <pthread.h>
#include <signal.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <mysql.h>

#include "../LibSocket/LibSocket.h"
#include "../OVESProtocol/OVESP_S.h"

using namespace std;

void HandlerSIGINT(int s);
void* FctThreadClient(void* p);
void TraitementClient(int sService);
bool areClientsInQueue(void);
MYSQL* ConnectDB(MYSQL *connexion);
void AchatToCaddie(char* reponse);


#define NB_THREADS_POOL_MAX 2
#define TAILLE_FILE_ATT 10

MYSQL *connexion;
pthread_mutex_t mutexConnexionBD;

int TabSocket[TAILLE_FILE_ATT];
pthread_mutex_t mutexTabSocket;
pthread_cond_t condTabSocket;
int indiceEcriture=0, indiceLecture=0;
int sEcoute = 0;
    
//nombre para/ port
int main(int argc, char* argv[])
{   
    //vérifie la présence d'argument
    if (argc != 2)
    {
        printf("Erreur...\n");
        printf("USAGE : ServeurAchat portServeur\n");
        exit(1);
    }

    //initialisation
    pthread_mutex_init(&mutexConnexionBD, NULL);

    pthread_mutex_init(&mutexTabSocket,NULL);
    pthread_cond_init(&condTabSocket,NULL);
    pthread_t th;

    for(int i=0; i < TAILLE_FILE_ATT; i++)
    {
        TabSocket[i] = -1;
    }

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;

    if (sigaction(SIGINT,&A,NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }

    //Initialisation de la connexion à la BD
    if((connexion = ConnectDB(connexion)) == NULL)
    {
        perror("Erreur de connexion à la base de données...\n");
        exit(1);
    }
    printf("Connexion a la base de donnees reussie!\n");


    //je passe un tableau d'argument avec donc l'ip et le port
    if((sEcoute = ServerSocket(atoi(argv[1]))) == -1)
    {
        perror("Erreur de ServerSocket");
        exit(1);
    }

    //création du pool de thread
    for(int i=0 ; i < NB_THREADS_POOL_MAX ; i++)
    {
        pthread_create(&th, NULL, FctThreadClient, NULL);
    }

    //lancement de la boucle du serveur
    int sService = 0;
    char ipClient[50];
    while(1)
    {
        if((sService = Accept(sEcoute, ipClient)) != -1)
        {
            pthread_mutex_lock(&mutexTabSocket);

            if(TabSocket[indiceEcriture] == -1) //Vérif si l'element courant de la file d'attente est libre.
            {
                TabSocket[indiceEcriture] = sService;
                if(indiceEcriture == (TAILLE_FILE_ATT - 1) /*&& TabSocket[0] == -1*/)
                {
                    indiceEcriture = 0;
                }
                else
                {
                    indiceEcriture++;
                }
                pthread_cond_signal(&condTabSocket);
            }
            else
            {
                //File remplie -> alerter le client et fermer le client.
                close(sService);
            }

            pthread_mutex_unlock(&mutexTabSocket);
        }
    }
    return 0;
}

//thread client 
void* FctThreadClient(void* p)
{
    int Ssocket=0;

    while(1)
    {
        pthread_mutex_lock(&mutexTabSocket);

            //ce réveille après un condsignal
            while(!areClientsInQueue()) //Tant qu'il n'y a pas de clients en attente...on endort le thread.
                pthread_cond_wait(&condTabSocket,&mutexTabSocket);

            Ssocket = TabSocket[indiceLecture];
            TabSocket[indiceLecture] = -1;
            
            if(indiceLecture == TAILLE_FILE_ATT-1)
            {
                //remise au début de la tete de lecture
                indiceLecture=0;
            }
            else
            {
                indiceLecture++;
            }

        pthread_mutex_unlock(&mutexTabSocket);


        TraitementClient(Ssocket);
    }
    pthread_exit(0);
}

bool areClientsInQueue()
{
    for(int i = 0; i < TAILLE_FILE_ATT; i++)
    {
        if(TabSocket[i] != -1)
        {
            return true;
        }
    }
    return false;
}

void TraitementClient(int sService)
{
    printf("\t[THREAD %p] Debut traitement du client...\n", pthread_self());

    char requete[200], reponse[200];
    bool onContinue = true;
    int nbLus, nbEcrits;

    while(onContinue)
    {
        //recois le message
        //printf("\t[THREAD %p] Attente d'une requete...\n", pthread_self());
        if((nbLus = Receive(sService, requete)) == -1)
        {
            perror("Erreur de Receive");
            close(sService);
            HandlerSIGINT(0);
        }

        //si jamais on a rien lu le socket est deconnecter
        if(nbLus == 0)
        {
            printf("\t[THREAD %p] Fin de connexion du client.\n",pthread_self());
            close(sService);
            return;
        }

        //savoir qui recois quoi
        //printf("\t[THREAD %p] Requete recue = %s\n",pthread_self(),requete);

        pthread_mutex_lock(&mutexConnexionBD);

        printf("Requete recue - socket %d : %s\n", sService, requete);
        onContinue = OVESP_Decode(requete, reponse, sService, connexion);
        printf("Reponse envoyee au client: %s\n", reponse);
        
        pthread_mutex_unlock(&mutexConnexionBD);

        //Vérifie si la reponse est un ACHAT -> MAJ du Caddie.
        AchatToCaddie(reponse);

        //on renvoi la reponse au client
        printf("Avant Send() -> strlen(reponse) = %d\n", strlen(reponse));

        if((nbEcrits = Send(sService, reponse, strlen(reponse))) == -1)
        {
            perror("Erreur de Send");
            close(sService);
            HandlerSIGINT(0);
        }
    
        //si oncontinue deviens false c'est la fin de la connexion
        if(!onContinue)
            printf("\t[THREAD %p] Fin de connexion de la socket %d\n", pthread_self(), sService);
    }
}

MYSQL* ConnectDB(MYSQL *connexion)
{
    connexion = mysql_init(NULL);
    mysql_real_connect(connexion, "localhost", "Student", "PassStudent1_", "PourStudent", 0, 0, 0);
    return connexion;
}

void AchatToCaddie(char* reponse)
{

}

//signale pour couper les processus
void HandlerSIGINT(int s)
{
    printf("\nArret du serveur.\n");
    close(sEcoute);
    
    pthread_mutex_lock(&mutexTabSocket);

    for (int i=0 ; i<TAILLE_FILE_ATT ; i++) {
        if (TabSocket[i] != -1) close(TabSocket[i]);
    }
        
    pthread_mutex_unlock(&mutexTabSocket);
    mysql_close(connexion);
    OVESP_Close();
    
    exit(0);
}

