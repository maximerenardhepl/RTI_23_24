#include "LibSocket.h"
#include <iostream>
#include <cstring>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>


////////////////////////////////////////////////////////////////////////////////////////////////////////////
//in : prend le port (toujours plus de 1000)
//process : fonction qui sera appelée par le processus serveur
//out :  retourne la socket d’écoute ainsi créé

//info: je met tout comme ça il faut trier et corriger c'est juste pour avoir la structure
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int ServerSocket(int port)
{
    int s;

    // Affiche le pid du processus
    printf("pid = %d\n", getpid());

    // Crée la socket, retourne 0 en cas d'échec
    if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("Erreur de socket()");
        return 0;
    }

    printf("socket creee = %d\n", s);

    // Construit l'adresse réseau de la socket par appel à getaddrinfo()
    struct addrinfo hints;
    struct addrinfo *results;
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_NUMERICSERV;

    char portStr[6];
    sprintf(portStr, "%d", port);

    if (getaddrinfo(NULL, portStr, &hints, &results) != 0) {
        perror("Erreur de getaddrinfo()");
        close(s);
        return 0;
    }

    int socketService=0;

    // Fait appel à bind() pour lier la socket à l'adresse réseau
    if ((socketService = bind(s, results->ai_addr, results->ai_addrlen)) < 0) {
        perror("Erreur de bind()");
        freeaddrinfo(results);
        close(s);
        return 0;
    }

    printf("return de bind %d",socketService);

    freeaddrinfo(results);
    printf("bind() reussi !\n");

    //retourn le socket d'écoute
    return s;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int Accept(int sEcoute,char *ipClient)
{
    return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int ClientSocket(char* ipServeur,int portServeur)
{
    return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int Send(int sSocket,char* data,int taille)
{
    return 0;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int Receive(int sSocket,char* data)
{
    return 0;
}