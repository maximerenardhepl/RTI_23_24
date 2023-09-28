#ifndef OVESP_Serv_H
#define OVESP_Serv_H

#define NB_MAX_CLIENTS 10

bool OVESP(char* requete, char* reponse, int socket);
bool OVESP_Login(const char* user,const char* password);
int OVESP_Operation(char op,int a,int b);
void OVESP_Close();

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);



#endif