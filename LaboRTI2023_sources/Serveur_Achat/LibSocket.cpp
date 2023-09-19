#include "LibSocket.h"

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//in : prend le port (toujours plus de 1000)
//process : fonction qui sera appelée par le processus serveur
//out :  retourne la socket d’écoute ainsi créé

//info: je met tout comme ça il faut trier et corriger c'est juste pour avoir la structure
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int LibSocket::ServerSocket(int port)
{
    //crée le socket et l'associer au port

        int s;

        //affiche le pid du processus
        printf("pid = %d\n",getpid());

        //crée la socket si rater return 0 si reussis retourne 1
        if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1)
        {
            perror("Erreur de socket()");
            exit(1);
        }

        printf("socket creee = %d\n",s);

    //construit l’adresse réseau de la socket par appel à getaddrinfo()
        
        struct addrinfo hints;
        struct addrinfo *results;
        memset(&hints,0,sizeof(struct addrinfo));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = AI_NUMERICSERV;
        
        if (getaddrinfo(argv[1],argv[2],&hints,&results) != 0)
        {
            exit(1);
        }

    //fait appel à bind() pour lier la socket à l’adresse réseau

        if(bind(sEcoute,results->ai_addr,results->ai_addrlen) < 0)
        {
            perror("Erreur de bind()");
            exit(1);
        }

        freeaddrinfo(results);
        printf("bind() reussi !\n");
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int LibSocket::Accept(int sEcoute,char *ipClient)
{

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int LibSocket::ClientSocket(char* ipServeur,int portServeur)
{

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int LibSocket::Send(int sSocket,char* data,int taille)
{

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//process :
////////////////////////////////////////////////////////////////////////////////////////////////////////////

int LibSocket::Receive(int sSocket,char* data)
{

}