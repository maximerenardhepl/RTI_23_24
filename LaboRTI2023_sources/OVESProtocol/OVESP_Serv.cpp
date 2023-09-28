#include "OVESP_Serv.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>



int clients[NB_MAX_CLIENTS];
int nbClients = 0;

pthread_mutex_t mutexClients;

//process : fonction qui va etre utiliser par le serveur pour traiter les packet entrant
bool OVESP(char* requete, char* reponse, int socket)
{
    const char *delim = "#";
    char* token = strtok(requete, delim);

    if(strcmp(token, "CONSULT") == 0)
    {

    }

    if(strcmp(token, "ACHAT") == 0)
    {

    }

    if(strcmp(token, "CADDIE") == 0)
    {

    }

    if(strcmp(token, "CONFIRM") == 0)
    {

    }

    return true;
}

bool OVESP_Login(const char* user,const char* password)
{
    return 0;
}

int OVESP_Operation(char op,int a,int b)
{
    return 0;
}

//////////////////////////////////////////////////////////
//fonction pour la gestion des client
//////////////////////////////////////////////////////////


//va etre utiliser l'ors d'un login d'un client 
//prend un socket du serveur qui est connecter avec le client
int estPresent(int socket)
{
    int indice = -1;

    pthread_mutex_lock(&mutexClients);
        //parcours le vecteur pour trouver un endroit sans client
        for(int i=0 ; i<nbClients ; i++)
        {   
            if (clients[i] == socket)
            { 
                indice = i; break; 
            }
        }
    pthread_mutex_unlock(&mutexClients);

    return indice;
}

//permert lajout d'un client(socket) dans le vecteur global
void ajoute(int socket)
{
    pthread_mutex_lock(&mutexClients);
        clients[nbClients] = socket;
        nbClients++;
    pthread_mutex_unlock(&mutexClients);
}

//permet de retirer un client(socket) dans le vecteur global
void retire(int socket)
{
    int pos = estPresent(socket);
    if (pos == -1) 
    {
        return;
    }

    pthread_mutex_lock(&mutexClients);
        for (int i=pos ; i<=nbClients-2 ; i++)
        {
            clients[i] = clients[i+1];
        }
        nbClients--;
    pthread_mutex_unlock(&mutexClients);

}

//permet de close tout lles socket si le serveur plante
void OVESP_Close()
{
    pthread_mutex_lock(&mutexClients);

    for (int i=0 ; i<nbClients ; i++)
    {
        close(clients[i]);
    }
    
    pthread_mutex_unlock(&mutexClients);
}