#include "OVESP_C.h"

#define NB_ARTICLE 21

//ce charge d'envoyer et receptionner les paquets
void Echange(char* requete, char* reponse, int socket);


bool OVESP_Login(const char* user,const char* password, int socket, int newclient)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    //si c'est newclient est = a 1 alors jenvoie une requete au serveur et je dit d'ajouter dans la bd
    if(newclient == 1) 
    {
        sprintf(requete,"REGISTER#%s#%s",user,password);
    }
    else 
    {
        sprintf(requete,"LOGIN#%s#%s",user,password);
    }

    // ***** Envoi requete + réception réponse **************
    //printf("Nouvelle requete: %s\n", requete);
    Echange(requete,reponse, socket);
    //printf("Reponse recue: %s\n", reponse);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse,"#"); // entête = LOGIN (normalement...)
    ptr = strtok(NULL,"#");

    if (strcmp(ptr,"ok") == 0)
    {
        //printf("OVESP_Login: Connexion réussie!\n");
        return true;
    }
    else
    {
        //printf("OVESP_Login: Erreur de login\n");
        throw Exception("Erreur de login!");
    }

    return true;
}

void OVESP_Logout(int socket)
{
    char requete[200],reponse[200];
    int nbEcrits, nbLus;

    // ***** Construction de la requete *********************
    sprintf(requete,"LOGOUT");

    // ***** Envoi requete + réception réponse **************
    Echange(requete,reponse, socket);
}

Article OVESP_Consult(int idArticle, int socket)
{
    char requete[200], reponse[200];
    int nbEcrits, nbLus;

    sprintf(requete, "CONSULT#%d", idArticle);

    //printf("Nouvelle requete: %s\n", requete);
    Echange(requete, reponse, socket);
    //printf("Reponse recue: %s\n", reponse);

    const char *delim = "#";
    char* token = strtok(reponse, delim);
    token = strtok(NULL, delim);

    Article resArticle;
    if(strcmp(token, "KO") == 0)
    {
        //Lancer une exception pour traiter l'erreur survenue dans l'envoi de la requete
        int errCode = atoi(strtok(NULL, delim));

        string message;
        if(errCode == DataBaseException::QUERY_ERROR) {
            message = "Une erreur est survenue lors de l'envoi de la requete...Veuillez reessayer!";
        }
        else if(errCode == DataBaseException::EMPTY_RESULT_SET) {
            message = "Aucun article correspondant a votre demande n'a ete trouve!";
        }
        throw Exception(message);
    }
    else
    {
        int id = atoi(strtok(NULL, delim));
        string intitule = strtok(NULL, delim);
        int stock = atoi(strtok(NULL, delim));
        string image = strtok(NULL, delim);
        float prix = atof(strtok(NULL, delim));

        //printf("prix = %lf\n", prix);

        resArticle = Article(id, intitule, stock, image, prix);
    }
    return resArticle;
}

Article OVESP_Achat(int idArticle, int quantite, int socket)
{
    char requete[200], reponse[200];
    int nbEcrits, nbLus;

    sprintf(requete, "ACHAT#%d#%d", idArticle, quantite);

    //printf("Nouvelle requete: %s\n", requete);
    Echange(requete, reponse, socket);
    //printf("Reponse recue: %s\n", reponse);

    const char* delim = "#";
    char* token = strtok(reponse, delim);
    token = strtok(NULL, delim);

    Article resArticle;
    if(strcmp(token, "KO") == 0)
    {
        int errCode = atoi(strtok(NULL, delim));

        string message;
        if(errCode == DataBaseException::QUERY_ERROR) {
            message = "Une erreur est survenue lors de l'envoi de la requete...Veuillez reessayer!";
        }
        else if(errCode == DataBaseException::EMPTY_RESULT_SET) {
            message = "Aucun article correspondant a votre demande n'a ete trouve!";
        }
        else if(errCode == AchatArticleException::INSUFFICIENT_STOCK)
        {
            message = strtok(NULL, delim); //Récupération du message d'erreur créé par le serveur.
        }
        throw Exception(message);
    }
    else
    {
        int id = atoi(strtok(NULL, delim));
        string intitule = strtok(NULL, delim);
        int stock = atoi(strtok(NULL, delim));
        string image = strtok(NULL, delim);
        float prix = atof(strtok(NULL, delim));

        resArticle = Article(id, intitule, stock, image, prix);
    }
    return resArticle;
}

void OVESP_Caddie()
{
    
}

void OVESP_Confirm()
{

}

void Echange(char* requete, char* reponse, int socket)
{
    int nbEcrits, nbLus;

    if((nbEcrits = Send(socket, requete, strlen(requete) )) == -1)
    {
        perror("Erreur de Send");
        close(socket);
        exit(1);
    }

    if((nbLus = Receive(socket, reponse)) == -1)
    {
        perror("Client : Fonction Echange : Erreur de Receive");
        close(socket);
        exit(1);
    }

    if(nbLus == 0)
    {
        //printf("Client : Fonction Echange : Le serveur s'est arrete, pas de reponse reçue...\n");
        close(socket);
        exit(1);
    }
}

void OVESP_Cancel(int socket,Article* panier[], int IndArt)
{
    char requete[200], reponse[200];

    //passe lid pour trouver larticle et la quantiter pour reincrementer la bd
    sprintf(requete, "CANCEL#%d#%d#%d",panier[IndArt]->getId(),panier[IndArt]->getQte(),IndArt);

    //supprimer du panier et recompacter les elements
    delete panier[IndArt];
    for (int i = IndArt; i < NB_ARTICLE - 1; i++) 
    {
        panier[i] = panier[i + 1];
    }

    Echange(requete, reponse, socket);

}

void OVESP_Cancel_All(int socket,Article* panier[])
{
    //printf("============= 1 debut de cancel all\n");
    char requete[200], reponse[200];

    //reset le panier du client 
    for(int i=0 ; i < NB_ARTICLE && panier[i] != NULL; i++)
    {
            delete panier[i];
            panier[i] = NULL;
    }
    //printf("============= 1.1\n");
    sprintf(requete, "CANCELALL");

    //envoie une requete serveur pour vider le panier coter serveur et réincrémente la bd
    //printf("Nouvelle requete: %s\n", requete);
    Echange(requete, reponse, socket);    
}

