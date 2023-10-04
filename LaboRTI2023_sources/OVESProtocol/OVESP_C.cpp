#include "OVESP_C.h"

//ce charge d'envoyer et receptionner les paquets
void Echange(char* requete, char* reponse, int socket);


bool OVESP_Login(const char* user,const char* password, int socket)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    // ***** Construction de la requete *********************
    sprintf(requete,"LOGIN#%s#%s",user,password);

    // ***** Envoi requete + réception réponse **************
    Echange(requete,reponse, socket);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse,"#"); // entête = LOGIN (normalement...)
    ptr = strtok(NULL,"#");

    if (strcmp(ptr,"ok") == 0) 
    {
        //connexion réussie
        printf("connexion réussie.\n");
    }
    else
    {
        printf("erreur de login");
        onContinue = false;
    }
    return onContinue;
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
    Echange(requete, reponse, socket);

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

        resArticle = Article(id, intitule, stock, image, prix);
    }
    return resArticle;
}

void OVESP_Achat()
{

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
        perror("Erreur de Receive");
        close(socket);
        exit(1);
    }

    if(nbLus == 0)
    {
        printf("Serveur arrete, pas de reponse reçue...\n");
        close(socket);
        exit(1);
    }
}

