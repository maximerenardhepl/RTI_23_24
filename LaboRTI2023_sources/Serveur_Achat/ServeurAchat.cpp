#include <iostream>
#include <pthread.h>
#include <signal.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#include "../LibSocket/LibSocket.h"
#include "../ProtocolCommunication/CP.h"
#include "../OVESProtocol/OVESP_Serv.h"

using namespace std;

void HandlerSIGINT(int s);
void* FctThreadClient(void* p);
void TraitementClient(int sService);
bool areClientsInQueue(void);


#define NB_THREADS_POOL_MAX 10
#define TAILLE_FILE_ATT 30

int TabSocket[TAILLE_FILE_ATT] = {-1};
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
    
    //initialisation
    pthread_t th;

   
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
    while(1)
    {
        char ipClient[50];
        if((sService = Accept(sEcoute, ipClient)) != -1)
        {
            pthread_mutex_lock(&mutexTabSocket);

            //retour début du tableau si place libre dans la file.
            /*if (indiceEcriture == (TAILLE_FILE_ATT-1) && TabSocket[0] == -1)
            {
                indiceEcriture = 0;
            }
            else //Pour les autres cases...
            {*/
                if(indiceEcriture == -1) //Vérif si l'element courant de la file d'attente est libre.
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
            //}

            pthread_mutex_unlock(&mutexTabSocket);
        }
    }
    return 0;
}

//thread client 
void* FctThreadClient(void* p)
{
    int Ssocket=0;
    bool isClientsWaiting = false;

    while(1)
    {
        pthread_mutex_lock(&mutexTabSocket);

            //ce réveille après un condsignal
            while((isClientsWaiting = areClientsInQueue()) == false) //Tant qu'il n'y a pas de clients en attente...on endort le thread.
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
        
        
        pthread_exit(0);
    }
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
    printf("\t[THREAD %d] Debut traitement du client...\n", pthread_self())

    char requete[200], reponse[200];
    bool onContinue = true;
    int nbLus, nbEcrits;

    while(onContinue)
    {
        printf("\t[THREAD %p] Attente requete...\n",pthread_self());
        if((nbLus = Receive(sService, requete)) == -1)
        {
            perror("Erreur de Receive");
            close(sService);
            HandlerSIGINT(0);
        }

        if(nbLus == 0)
        {
            printf("\t[THREAD %p] Fin de connexion du client.\n",pthread_self());
            close(sService);
            return;
        }

        printf("\t[THREAD %p] Requete recue = %s\n",pthread_self(),requete);
        onContinue = OVESP(requete, reponse, sService);

        if((nbEcrits = Send(sService, reponse, sizeof(reponse))) == -1)
        {
            perror("Erreur de Send");
            close(sService);
            HandlerSIGINT(0);
        }

        if(!onContinue)
            printf("\t[THREAD %p] Fin de connexion de la socket %d\n", pthread_self(), sService);
    }
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
    //SMOP_Close();
    
    exit(0);
}

