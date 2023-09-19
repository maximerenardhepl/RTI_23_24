#ifndef LibSocket_H
#define LibSocket_H

#include <iostream>
using namespace std;
#include <cstring>
#include <iostream>
#include <fstream>

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

