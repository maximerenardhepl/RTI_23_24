#include "OVESP_Serv.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

int clients[NB_MAX_CLIENTS];
int nbClients = 0;

pthread_mutex_t mutexClients;

//process : fonction qui va 
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