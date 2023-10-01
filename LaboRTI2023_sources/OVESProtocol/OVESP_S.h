#ifndef OVESP_S_H
#define OVESP_S_H

#define NB_MAX_CLIENTS 2

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>


//prend la requete du client et la décortique
bool OVESP_Decode(char* requete, char* reponse, int socket);

//ferme tout les socket si serveur crash
void OVESP_Close();

#endif