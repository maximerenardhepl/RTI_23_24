#include "windowclient.h"
#include "../LibSocket/LibSocket.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>

#include <QApplication>

WindowClient *w;

void HandlerSIGINT(int);

int sClient = 0;

int main(int argc, char *argv[])
{
    if (argc != 3)
    {
        printf("Erreur...\n");
        printf("USAGE : Client ipServeur portServeur\n");
        exit(1);
    }

    // Armement des signaux
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    
    if (sigaction(SIGINT,&A,NULL) == -1)
    {
        perror("Erreur de sigaction");
        exit(1);
    }

    // Connexion sur le serveur 
    if ((sClient = ClientSocket(argv[1],atoi(argv[2]))) == -1)
    {
        perror("Erreur de ClientSocket");
        exit(1);
    }

    printf("socket client = %d\n",sClient);
    printf("Connecte sur le serveur.\n");
    //sleep(60);


    QApplication a(argc, argv);
    w = new WindowClient();
    w->show();
    return a.exec();
}

void HandlerSIGINT(int s)
{
    printf("\nArret du client.\n");
    close(sClient);
    exit(0);
}

