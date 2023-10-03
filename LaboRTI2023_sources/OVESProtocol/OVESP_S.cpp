#include "OVESP_S.h"

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

int clients[NB_MAX_CLIENTS];
int nbClients = 0;

//gestion des client
bool estPresent(int socket);
bool ajoute(int socket);
void retire(int socket);

//questionne la bd pour voir si present
bool verif_Log(const char* user,const char* password);
Article getArticleOnDB(int idArticle, MYSQL* conn);


bool OVESP_Decode(char* requete, char* reponse, int socket, MYSQL* conn)
{
    const char *delim = "#";
    char* token = strtok(requete, delim);

    if (strcmp(token,"LOGIN") == 0)
    {
        //si le client est déja loger
        if(estPresent(socket))
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
        }
        else
        {
            char user[200];
            char password[200];
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
            }
        }
        return true;
    }

    if (strcmp(token,"LOGOUT") == 0)
    {
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
    }



    if(strcmp(token, "CONSULT") == 0)
    {
        if(estPresent(socket))
        {

        }
        else
        {
            int idArticle = atoi(strtok(NULL, delim));

            try 
            {
                Article art = getArticleOnDB(idArticle, conn);
            }
            catch(DataBaseException e) 
            {
                string msg = "Erreur de recuperation de l'article :\nCode d'erreur : " + to_string(e.getCode()) + "\tMessage d'erreur : " + e.getMessage() + "\n";
                cout << msg;
            }
            
        }
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


//Fonctions d'accès à la base de données:

//regarde dans la bd si le compte existe bien
bool verif_Log(const char* user,const char* password)
{
    //question la bd si le client existe retourne true ou false
    return true;
}


//Process : Récupère les informations de l'article en base de données (retour sous forme d'objet Article)
Article getArticleOnDB(int idArticle, MYSQL *conn)
{
    char requete[256];
    sprintf(requete, "select * from articles where id = %d;", idArticle);

    if(mysql_query(conn, requete) != 0)
    {
        //Construction et envoi du message d'erreur...
        string msg = "Erreur de mysql_query() : ";
        msg += mysql_error(conn);

        throw DataBaseException(msg, DataBaseException::QUERY_ERROR);
    }
    else
    {
        MYSQL_RES* resultat;
        if( (resultat = mysql_store_result(conn)) == NULL)
        {
            //Construction et envoi du message d'erreur...
            string msg = "Erreur de mysql_store_result() : "; 
            msg += mysql_error(conn);

            throw DataBaseException(msg, DataBaseException::EMPTY_RESULT_SET);
        }
        else
        {
            //Récupération des données issues de la requete...
            MYSQL_ROW tuple;
            tuple = mysql_fetch_row(resultat);

            int id = atoi(tuple[0]);
            string intitule = tuple[1];
            int stock = atoi(tuple[2]);
            string image = tuple[3];
            float prix = atof(tuple[4]);

            Article art(id, intitule, stock, image, prix);
            return art;
        }
    }
}

//////////////////////////////////////////////////////////
//fonctions pour la gestion des clients
//////////////////////////////////////////////////////////

//va etre utiliser l'ors d'un login d'un client 
//prend un socket du serveur qui est connecter avec le client
bool estPresent(int socket)
{
    bool present = false;
    pthread_mutex_lock(&mutexClients);
        //parcours le vecteur pour trouver un endroit sans client
        for(int i=0 ; i<nbClients ; i++)
        {   
            if (clients[i] == socket)
            { 
                present = true;
            }
        }
    pthread_mutex_unlock(&mutexClients);
    return present;
}

//permert lajout d'un client(socket) dans le vecteur global
bool ajoute(int socket)
{
    bool estAjoute = false;

    pthread_mutex_lock(&mutexClients);
        if(nbClients < NB_MAX_CLIENTS)
        {
            clients[nbClients] = socket;
            nbClients++;

            estAjoute = true;
        }
    pthread_mutex_unlock(&mutexClients);
    return estAjoute;
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