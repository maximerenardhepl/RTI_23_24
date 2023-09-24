#include <iostream>
#include <pthread.h>
#include <signal.h>
#include "LibSocket.h"
using namespace std;

void HandlerSIGINT(int s);
void* FctThreadClient(void* p);

#define POOL_MAX 10
int TabSocket[20] = {0};
pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t condSocketsAcceptees;
int indiceEcriture=0, indiceLecture=0;
    
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

    int sEcoute = 0;
    //je passe un tableau d'argument avec donc l'ip et le port
    if((sEcoute = ServerSocket(atoi(argv[1]))) == -1)
    {
        perror("Erreur de ServerSocket");
        exit(1);
    }

    //création du pool de thread
    for(int i=0 ; i < POOL_MAX ; i++)
    {
        pthread_create(&th,NULL,FctThreadClient,NULL);
    }


    //lancement de la boucle du serveur
    int sService = 0;
    while(1)
    {
        char ipClient[50];
        if((sService = Accept(sEcoute, ipClient)) != -1)
        {
            pthread_mutex_lock(&mutexSocketsAcceptees);
            TabSocket[indiceEcriture] = sService;
            indiceEcriture++;
            pthread_mutex_unlock(&mutexSocketsAcceptees);
            pthread_cond_signal(&condSocketsAcceptees);
        }
    }
    return 0;
}

//thread client 
void* FctThreadClient(void* p)
{
    int Ssocket=0;

    pthread_mutex_lock(&mutexSocketsAcceptees);
    //ce réveille après un condsignal
    pthread_cond_wait(&condSocketsAcceptees,&mutexSocketsAcceptees);

    Ssocket = TabSocket[indiceLecture];

    pthread_mutex_unlock(&mutexSocketsAcceptees);

    return 0;
}

//signale pour couper les processus
void HandlerSIGINT(int s)
{

}