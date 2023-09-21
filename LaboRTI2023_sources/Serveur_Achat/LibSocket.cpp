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
#include <math.h>


////////////////////////////////////////////////////////////////////////////////////////////////////////////
//in : prend le port (toujours plus de 1000)
//process : fonction qui sera appelée par le processus serveur
//out :  retourne la socket d’écoute ainsi créé

//info:
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int ServerSocket(int port)
{
    int s;

    // Affiche le pid du processus
    printf("pid = %d\n", getpid());

    // Crée la socket, retourne 0 en cas d'échec
    if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("Erreur de socket()");
        return -1;
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
        perror("Erreur de getaddrinfo()\n");
        close(s);
        return -1;
    }

    // Fait appel à bind() pour lier la socket à l'adresse réseau
    if (bind(s, results->ai_addr, results->ai_addrlen) < 0) {
        perror("Erreur de bind()\n");
        freeaddrinfo(results);
        close(s);
        return -1;
    }

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
    if(listen(sEcoute, SOMAXCONN) == -1)
    {
        return -1;
    }
    printf("listen() reussi !\n");

    //Définition des structures permettant de récupérer les informations sur le client qui s'est connecté via l'appel système 'accept()'
    //Ces informations pourraient etre stockées dans un fichier de log par exemple (idée)
    struct sockaddr adrClient;
    socklen_t adrClientLen;
    char host[NI_MAXHOST];
    char port[NI_MAXSERV];

    int sService = 0;
    if((sService = accept(sEcoute, &adrClient, &adrClientLen)) == -1)
    {
        return -1;
    }
    printf("accept() reussi !");
    printf("socket de service = %d\n",sService);

    if(ipClient != NULL)
    {
        getpeername(sService,&adrClient,&adrClientLen);
        getnameinfo(&adrClient,adrClientLen,
        host,NI_MAXHOST,
        port,NI_MAXSERV,
        NI_NUMERICSERV | NI_NUMERICHOST);

        ipClient = host;
    }
    
    return sService;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :

//info : normalement la fonction fonctoinne mais je n'aie pas pu la tester car il faut les ip du serveur 
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int ClientSocket(char* ipServeur,int portServeur)
{
    int s = 0;

    // Création du socket du client
    if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1) // Notez la double égalité ici
    {
        perror("Erreur de socket()\n");
        return -1; // Retourne une valeur d'erreur (-1) au lieu de 0
    }

    printf("Descripteur du socket client : %d\n", s);

    // Récupération des informations du client

    struct addrinfo hints;
    struct addrinfo *results;
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_NUMERICSERV;

    char portStr[6];
    sprintf(portStr, "%d", portServeur);

    if (getaddrinfo(ipServeur, portStr, &hints, &results) != 0) 
    {
        perror("Erreur de getaddrinfo client\n");
        close(s);
        return -1;
    }

    // Fait appel à connect() pour se connecter au serveur
    if (connect(s, results->ai_addr, results->ai_addrlen) < 0) 
    {
        perror("Erreur de connect()\n");
        freeaddrinfo(results);
        close(s);
        return -1;
    }

    printf("Connexion réussie !\n");

    freeaddrinfo(results);

    // Retourne le descripteur de fichier du socket client connecté
    return s;
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
    if(data == NULL) 
    {
        //Le buffer doit etre initialisé
        return -1;
    }
    else 
    {
        char nbBytesStr[4];
        int nbBytes = 0; //Va contenir la valeur correspondant à la taille réelle de la charge utile du paquet de bytes.
        int nbCarLus = 0;

        //Lecture des 4 premiers octets contenant la taille de la réelle charge utile du paquet de bytes.
        //Exemple : Charge utile = "Hello World" -> alors le vrai message écrit sur le pipe sera : "0011Hello World"
        for(int i = 0, j = 3; i < 4; i++, j--)
        {
            if((nbCarLus = read(sSocket, nbBytesStr, 1)) <= 0) return nbCarLus; //Si une erreur se produit lors de la lecture on retourne la valeur retournée par read()
            nbBytes += nbBytesStr[i] * pow(10, j); //Actualisation de la valeur de nbBytes
        }
        
        if(sizeof(data) >= nbBytes)
        {
            return (nbCarLus = read(sSocket, data, nbBytes));
        }
        else
        {
            //Taille du buffer de stockage (ici data) insuffisante.
            return -1;
        }
    }
}