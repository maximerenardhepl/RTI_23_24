#ifndef OVESP_H
#define OVESP_H

#define NB_MAX_CLIENTS 2

bool OVESP_Decode(char* requete, char* reponse, int socket);

bool OVESP_Login(const char* user,const char* password);
void OVESP_Logout();

void OVESP_Consult();
void OVESP_Achat();
void OVESP_Caddie();
void OVESP_Confirm();

void Echange(char* requete, char* reponse);

void OVESP_Close();

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

#endif