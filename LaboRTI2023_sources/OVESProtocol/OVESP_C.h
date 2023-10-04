#ifndef OVESP_C_H
#define OVESP_C_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <string>
#include "../LibSocket/LibSocket.h"
#include "../Data/Article.h"
#include "../Data/DataBaseException.h"

using namespace std;

//connecte deconnect du client
bool OVESP_Login(const char* user,const char* password, int socket);
void OVESP_Logout(int socket);

//ce charge de construire les requete pour envoyer au serveur (place une entete)
Article OVESP_Consult(int idArticle, int socket);
void OVESP_Achat();
void OVESP_Caddie();
void OVESP_Confirm();



#endif