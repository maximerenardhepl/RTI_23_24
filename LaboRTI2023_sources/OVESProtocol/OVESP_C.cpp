#include "OVESP_C.h"

//ce charge d'envoyer et receptionner les paquets
void Echange(char* requete, char* reponse);


bool OVESP_Login(const char* user,const char* password)
{
    char requete[200],reponse[200];
    bool onContinue = true;

    // ***** Construction de la requete *********************
    sprintf(requete,"LOGIN#%s#%s",user,password);

    // ***** Envoi requete + réception réponse **************
    Echange(requete,reponse);

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

void OVESP_Logout()
{
    char requete[200],reponse[200];
    int nbEcrits, nbLus;

    // ***** Construction de la requete *********************
    sprintf(requete,"LOGOUT");

    // ***** Envoi requete + réception réponse **************
    Echange(requete,reponse);
}

Article OVESP_Consult(int idArticle)
{
    char requete[200], reponse[200];
    int nbEcrits, nbLus;

    sprintf(requete, "CONSULT#%d", idArticle);
    Echange(requete, reponse);

    const char *delim = "#";
    char* token = strtok(reponse, delim);

    Article resArticle;
    if(atoi(token) != 0)
    {
        //Lancer une exception pour traiter l'erreur survenue dans l'envoi de la requete
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

void Echange(char* requete, char* reponse)
{

}

