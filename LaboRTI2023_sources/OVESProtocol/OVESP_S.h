#ifndef OVESP_S_H
#define OVESP_S_H

#define NB_MAX_CLIENTS 2

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <mysql.h>
#include <ctime>
#include <sstream>

#include "../Data/DataBaseException.h"
#include "../Data/AchatArticleException.h"
#include "../Data/Article.h"

using namespace std;

//Gestion des clients
bool estPresent(int socket);
bool ajoute(int socket);
void retire(int socket);

//prend la requete du client et la décortique
bool OVESP_Decode(char* requete, char* reponse, int socket, MYSQL *conn, Article* panier[]);

//ferme tout les socket si serveur crash
void OVESP_Close();

#endif