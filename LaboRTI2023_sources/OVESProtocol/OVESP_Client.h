#ifndef OVESP_Client_H
#define OVESP_Client_H

#define NB_MAX_CLIENTS 10

void Echange(char* requete, char* reponse);
bool OVESP_Login(const char* user,const char* password);
void OVESP_Logout();
void OVESP_Operation(char op,int a,int b);



#endif