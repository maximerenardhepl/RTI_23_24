#include "OVESP.h"

#include <stdlib.h>
#include <stdio.h>

int clients[NB_MAX_CLIENTS];
int nbClients = 0;

bool OVESP(char* requete, char* reponse, int socket)
{
    char* token = strtok(requete, '#');

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