#ifndef LibSocket_H
#define LibSocket_H

int ServerSocket(int port);
int Accept(int sEcoute,char *ipClient);
int ClientSocket(char* ipServeur,int portServeur);
int Send(int sSocket,char* data,int taille);
int Receive(int sSocket, char* data);

#endif

