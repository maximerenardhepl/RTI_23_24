#include <iostream>
#include "LibSocket.h"
using namespace std;

//nombre para/ port
int main(int argc, char* argv[])
{
    //vérifie la présence d'argument
    if (argc != 2)
    {
        printf("Erreur...\n");
        printf("USAGE : ServeurAchat portServeur\n");
        exit(1);
    }

    int sEcoute = 0;
    //je passe un tableau d'argument avec donc l'ip et le port
    if((sEcoute = ServerSocket(atoi(argv[1]))) == -1)
    {
        perror("Erreur de ServerSocket");
        exit(1);
    }

    int sService = 0;
    while(1)
    {
        char ipClient[50];
        if((sService = Accept(sEcoute, ipClient)) != -1)
        {
            
        }
    }

    return 0;
}