#ifndef OVESP_S_H
#define OVESP_S_H

#define NB_MAX_CLIENTS 2

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <mysql.h>
#include "../Data/DataBaseException.h"
#include "../Data/AchatArticleException.h"
#include "../Data/Article.h"

using namespace std;

//prend la requete du client et la décortique
bool OVESP_Decode(char* requete, char* reponse, int socket, MYSQL* conn);

//ferme tout les socket si serveur crash
void OVESP_Close();

#endif