#ifndef OVESP_C_H
#define OVESP_C_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

//connecte deconnect du client
bool OVESP_Login(const char* user,const char* password);
void OVESP_Logout();

//ce charge de construire les requete pour envoyer au serveur (place une entete)
void OVESP_Consult();
void OVESP_Achat();
void OVESP_Caddie();
void OVESP_Confirm();



#endif