#ifndef LibSocket_H
#define LibSocket_H

#include <iostream>
#include <cstring>
#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>

class DataSource1D
{   
    private:
     
    

    public:
        
        int ServerSocket(int port);
        int Accept(int sEcoute,char *ipClient);
        int ClientSocket(char* ipServeur,int portServeur);
        int Send(int sSocket,char* data,int taille);
        int Receive(int sSocket,char* data);
    
};
#endif

