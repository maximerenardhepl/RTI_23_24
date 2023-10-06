#include "OVESP_S.h"

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

int clients[NB_MAX_CLIENTS];
int nbClients = 0;

//gestion des client
bool estPresent(int socket);
bool ajoute(int socket);
void retire(int socket);

//questionne la bd pour voir si present
bool verif_Log(const char* user,const char* pass,MYSQL *conn);
bool insert_new_client(char name, char pass,MYSQL *conn);
Article getArticleOnDB(int idArticle, MYSQL* conn);
Article buyArticleOnDB(int idArticle, int quantite, MYSQL* conn);


bool OVESP_Decode(char* requete, char* reponse, int socket, MYSQL* conn)
{
    printf("OVESP_Decode Serveur : Debut de la fonction....\n");

    const char *delim = "#";
    char* token = strtok(requete, delim);

    char test[30];
    strcpy(test, token);

    printf("Token dans OVESP_Decode = %s\n", token);
    printf("Token dans variable test = %s\n", test);

    ////////////////////////////////////////////////////////////

    printf("CECI EST UN TEST\n");

    if(strcmp(test, "LOGIN") == 0)
    {
        //si le client est déja loger
        printf("Requete LOGIN detectee dans le OVESP_Decode\n");

        if(estPresent(socket))
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
        }
        else
        {
            char* user = strtok(NULL, delim);
            char* password = strtok(NULL, delim);

            printf("Test client pas encore present");

            //on verif si il existe (quil est deja inscrit)
            if(verif_Log(user,password,conn) != false)
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

    printf("CECI EST UN TEST 2\n");

    ////////////////////////////////////////////////////////////

    if(strcmp(token,"LOGOUT") == 0)
    {
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
    }

    ////////////////////////////////////////////////////////////

    if(strcmp(token, "CONSULT") == 0)
    {
        if(!estPresent(socket))
        {
            sprintf(reponse, "CONSULT#KO#-1");
            return false; //OVESP_Decode va renvoyer false -> le thread s'occupant du client va arreter sa boucle et donc arreter se s'occuper du client...
                            //car erreur anormale -> un client non present dans le vecteur des clients connectes n'est pas censé pouvoir communiquer avec le serveur car aucun thread n'est censé lui avoir été attribué.
        }
        else
        {
            int idArticle = atoi(strtok(NULL, delim));

            try 
            {
                Article art = getArticleOnDB(idArticle, conn);
                sprintf(reponse, "CONSULT#OK#%d#%s#%d#%s#%ld", art.getId(), art.getIntitule(), art.getQte(), art.getImage(), art.getPrix());
            }
            catch(DataBaseException e)
            {
                string msg = "Catch DataBaseException :\nCode d'erreur : " + to_string(e.getCode()) + "\tMessage d'erreur : " + e.getMessage() + "\n";
                cout << msg;

                sprintf(reponse, "CONSULT#KO#%d", e.getCode());
            } 
        }
        return true;
    }

    if(strcmp(token, "ACHAT") == 0)
    {
        if(!estPresent(socket))
        {
            sprintf(reponse, "ACHAT#KO#-1");
            return false;
        }
        else
        {
            int idArticle = atoi(strtok(NULL, delim));
            int qte = atoi(strtok(NULL, delim));

            try
            {
                Article art = buyArticleOnDB(idArticle, qte, conn);
                sprintf(reponse, "ACHAT#OK#%d#%s#%d#%s#%ld", art.getId(), art.getIntitule(), art.getQte(), art.getImage(), art.getPrix());
            }
            catch(DataBaseException e)
            {
                sprintf(reponse, "ACHAT#KO#%d", e.getCode());
            }
            catch(AchatArticleException e)
            {
                sprintf(reponse, "ACHAT#KO#%d#%s", e.getCode(), e.getMessage());
            }
        }
        return true;
    }

    if(strcmp(token, "CONFIRM") == 0)
    {

    }

    return true;
}


//Fonctions d'accès à la base de données:

//regarde dans la bd si le compte existe bien
bool verif_Log(const char* user,const char* pass,MYSQL *conn)
{
    //question la bd si le client existe retourne true ou false
    if(mysql_query(conn, "select * from clients where login = user;") != 0)
    {
        mysql_error(conn);
        return false;
    }
    else
    {
        return true;
    }
}

bool insert_new_client(char name, char pass, MYSQL *conn)
{
    char requete[100];
    sprintf(requete, "INSERT INTO clients (login, password) VALUES ('%s', '%s');", name, pass);
    
    return mysql_query(conn, requete);

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

Article buyArticleOnDB(int idArticle, int quantite, MYSQL* conn)
{
    char requete[256];
    sprintf(requete, "select * from articles where id = %d", idArticle);

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
        if(mysql_store_result(conn) == NULL)
        {
            //Construction et envoi du message d'erreur...
            string msg = "Erreur de mysql_store_result() : "; 
            msg += mysql_error(conn);

            throw DataBaseException(msg, DataBaseException::EMPTY_RESULT_SET);
        }
        else
        {
            MYSQL_ROW tuple;
            tuple = mysql_fetch_row(resultat);

            int id = atoi(tuple[0]);
            string intitule = tuple[1];
            int stockReel = atoi(tuple[2]);
            string image = tuple[3];
            float prix = atof(tuple[4]);

            if(stockReel < quantite)
            {
                //Construction et envoi du message d'erreur...
                string msg = "Impossible d'acheter l'article! Le stock (" + to_string(stockReel) + ") pour cet article est insuffisant";
                throw AchatArticleException(msg, AchatArticleException::INSUFFICIENT_STOCK);
            }
            else
            {
                //Envoi d'une requete pour décrémenter le stock de l'article de la quantite demandée dans la BD.
                int nouveauStock = stockReel - quantite;
                sprintf(requete, "update articles set stock = %d where id = %d", nouveauStock, idArticle);

                if(mysql_query(conn, requete) != 0)
                {
                    //Construction et envoi du message d'erreur...
                    string msg = "Erreur de mysql_query() : ";
                    msg += mysql_error(conn);

                    throw DataBaseException(msg, DataBaseException::QUERY_ERROR);
                }

                Article art(id, intitule, quantite, image, prix);
                return art;
            }
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