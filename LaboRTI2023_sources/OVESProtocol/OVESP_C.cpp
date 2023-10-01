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

void OVESP_Consult()
{

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

