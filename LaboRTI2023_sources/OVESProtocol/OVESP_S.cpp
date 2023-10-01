#include "OVESP_S.h"

pthread_mutex_t mutexClients;

int clients[NB_MAX_CLIENTS];
int nbClients = 0;
//gestion des client
int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

//questionne la bd pour voir si present
bool verif_Log(const char* user,const char* password);


bool OVESP_Decode(char* requete, char* reponse, int socket)
{
    const char *delim = "#";
    char* token = strtok(requete, delim);

    if (strcmp(ptr,"LOGIN") == 0)
    {
        //si le client est déja loger 
        if(estPresent(socket) == 0)
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
            return false;
        }
        else
        {
            //on verif si il existe (quil est deja inscrit)
            if(verif_Log(user,password))
            {
                //alors on ajout dans la file des cliens
                sprintf(reponse,"LOGIN#ok");
                ajoute(socket);
            }
            else
            {
                //client n'existe pas et n'est pas déja logger
                sprintf(reponse,"LOGIN#ko#Mauvais identifiants !");
                return false;
            }
        }
    }

    if (strcmp(ptr,"LOGOUT") == 0)
    {
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
    }

    if(strcmp(token, "CONSULT") == 0)
    {

    }

    if(strcmp(token, "ACHAT") == 0)
    {

    }

    if(strcmp(token, "CADDIE") == 0)
    {

    }

    if(strcmp(token, "CONFIRM") == 0)
    {

    }

    return true;
}

//regarde dans la bd si le compte existe bien
bool verif_Log(const char* user,const char* password)
{
    //question la bd si le client existe retourne true ou false
    return true;
}

//////////////////////////////////////////////////////////
//fonctions pour la gestion des clients
//////////////////////////////////////////////////////////

//va etre utiliser l'ors d'un login d'un client 
//prend un socket du serveur qui est connecter avec le client
int estPresent(int socket)
{
    int indice = -1;

    pthread_mutex_lock(&mutexClients);
        //parcours le vecteur pour trouver un endroit sans client
        for(int i=0 ; i<nbClients ; i++)
        {   
            if (clients[i] == socket)
            { 
                indice = i; break; 
            }
        }
    pthread_mutex_unlock(&mutexClients);

    return indice;
}

//permert lajout d'un client(socket) dans le vecteur global
void ajoute(int socket)
{
    pthread_mutex_lock(&mutexClients);
        clients[nbClients] = socket;
        nbClients++;
    pthread_mutex_unlock(&mutexClients);
}

//permet de retirer un client(socket) dans le vecteur global
void retire(int socket)
{
    int pos = estPresent(socket);
    if (pos == -1) 
    {
        return;
    }

    pthread_mutex_lock(&mutexClients);
        for (int i=pos ; i<=nbClients-2 ; i++)
        {
            clients[i] = clients[i+1];
        }
        nbClients--;
    pthread_mutex_unlock(&mutexClients);

}

//permet de close tout les sockets si le serveur plante
void OVESP_Close()
{
    pthread_mutex_lock(&mutexClients);

    for (int i=0 ; i<nbClients ; i++)
    {
        close(clients[i]);
    }
    
    pthread_mutex_unlock(&mutexClients);
}