#include "OVESP_S.h"

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

#define NB_ARTICLE 21

int clients[NB_MAX_CLIENTS];
int nbClients = 0;

//gestion des client
bool estPresent(int socket);
bool ajoute(int socket);
void retire(int socket);
void afficheVecClients(const char* enTete);

//Gestion du panier des clients.
bool ajouteArticlePanier(Article* panier[], Article& nouveauArt);

//questionne la bd pour voir si present
bool verif_Log(char* user, char* pass,MYSQL *conn);
Article getArticleOnDB(int idArticle, MYSQL* conn);
Article buyArticleOnDB(int idArticle, int quantite, MYSQL* conn);
void createInvoiceOnDB(Article* panier[]);


bool OVESP_Decode(char* requete, char* reponse, int socket, MYSQL* conn, Article* panier[])
{
    const char *delim = "#";
    char* token = strtok(requete, delim);

    ////////////////////////////////////////////////////////////

    if(strcmp(token, "LOGIN") == 0)
    {
        //si le client est déja loger
        if(estPresent(socket))
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
        }
        else
        {
            char* user = strtok(NULL, delim);
            char* password = strtok(NULL, delim);

            try
            {
                verif_Log(user, password, conn);
                sprintf(reponse,"LOGIN#ok");
                ajoute(socket);

            }
            catch(DataBaseException& e)
            {
                sprintf(reponse, "LOGIN#ko#Mauvais identifiants !");
            }    
        }
        return true;
    }

    ///////////////////////////////////////////////

    if(strcmp(token,"REGISTER") == 0)
    {
        char* user = strtok(NULL, delim);
        char* password = strtok(NULL, delim);

        char requete[100];
        sprintf(requete, "INSERT INTO clients (login, password) VALUES ('%s', '%s');", user, password);
        
        mysql_query(conn, requete);
        ajoute(socket);

        sprintf(reponse,"LOGIN#ok#Client inscrit !");

    }

    ////////////////////////////////////////////////////////////

    if(strcmp(token,"LOGOUT") == 0)
    {
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
    }

    ////////////////////////////////////////////////////////////

    if(strcmp(token,"CANCEL") == 0)
    {

    }

    ////////////////////////////////////////////////////////////

    if(strcmp(token,"CANCELALL") == 0)
    {
        printf("============= 2 cancel all serveur\n");
        Article Art;
        int S=0;
        MYSQL_RES* resultat;
        MYSQL_ROW tuple;
        
        //reincremente la BD
        
        printf("============= 3 boucle pour remttre en stock\n");
        for(int i=0 ; i < NB_ARTICLE && panier[i] != NULL ; i++)
        {
            //savoir combien il y a d'article
            sprintf(requete, "select stock from articles where id = %d;",panier[i]->getId());
            mysql_query(conn, requete);
            resultat = mysql_store_result(conn);
            tuple = mysql_fetch_row(resultat);

            S = atoi(tuple[0]) + panier[i]->getQte();

            //printf("retour stock %d \n",S);

            //insert le nombre correcte d'article
            sprintf(requete, "update articles set stock = %d where id = %d;",S , panier[i]->getId());

            if(mysql_query(conn, requete) != 0)
            {
                printf("erreur bd\n");
            }             
        }

        printf("============= 4 vide le panier \n");
       
        //vide le panier du serveur 
        printf("vide la panier du serveur\n");
        for(int i=0 ; i < NB_ARTICLE && panier[i] != NULL; i++)
        {
            delete panier[i];
            panier[i] = NULL;
        }
        printf("============= 5 fin de cancelall serveur\n");
        sprintf(reponse, "CANCELALL#ok");
        return true;
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
                sprintf(reponse, "CONSULT#OK#%d#%s#%d#%s#%lf", art.getId(), art.getIntitule().c_str(), art.getQte(), art.getImage().c_str(), art.getPrix());
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
                ajouteArticlePanier(panier, art);
                sprintf(reponse, "ACHAT#OK#%d#%s#%d#%s#%lf", art.getId(), art.getIntitule().c_str(), art.getQte(), art.getImage().c_str(), art.getPrix());
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
        if(!estPresent(socket))
        {
            sprintf(reponse, "CONFIRM#KO#-1");
            return false;
        }
        else
        {
            try
            {
                createInvoiceOnDB(panier);
                sprintf("CONFIRM#OK");
            }
            catch(DataBaseException& e)
            {
                sprintf(reponse, "CONFIRM#KO#%d", e.getCode());
            }
        }
    }

    return true;
}



//Fonctions de gestion du panier du client.
bool ajouteArticlePanier(Article* panier[], Article& nouveauArt)
{
    bool found = false;
    int i = 0;

    //Vérifie si cet article qu'on veut ajouter n'est par hasard pas déjà présent dans le vecteur d'articles...
    while(i < NB_ARTICLE && panier[i] != NULL && !found)
    {
        if(panier[i]->getId() == nouveauArt.getId()) {
            found = true;
        }
        else {
            i++;
        }
    }

    if(i < NB_ARTICLE) //Dans ce cas la boucle s'est arrete soit parce qu'on a trouvé le meme article deja existant soit
                        //parce qu'on est simplement tombé sur une case du vecteur contenant NULL donc pas d'article.
    {
        if(found)
        {
            //On a trouvé le meme article deja existant -> on incremente simplement la quantite.
            int qteActuelle = panier[i]->getQte();
            int nouvelleQte = qteActuelle + nouveauArt.getQte();
            panier[i]->setQte(nouvelleQte);
        }
        else
        {
            //On a trouvé une place libre dans le vecteur pour ajouter ce nouvel article.
            panier[i] = new Article(nouveauArt);
        }
        return true;
    }
    else
    {
        //On a pas trouvé d'espace disponible pour stocker l'article -> on return false (l'ajout ne s'est pas bien passé)
        return false;
    }
}







//Fonctions d'accès à la base de données:

//regarde dans la bd si le compte existe bien
bool verif_Log(char* user,char* pass, MYSQL *conn)
{
    //question la bd si le client existe retourne true ou false
    char requete[200];
    sprintf(requete, "select * from clients where login like '%s' and password like '%s';", user, pass);
    
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
        if((resultat = mysql_store_result(conn)) == NULL)
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
            if(tuple != NULL)
            {
                //printf("OVESP_S: verif log: Le client existe et les identifiants sont corrects!\n");
                return true;
            }
            else
            {
                string msg = "Erreur de mysql_store_result() : "; 
                msg += mysql_error(conn);

                throw DataBaseException(msg, DataBaseException::EMPTY_RESULT_SET);
            }            
        }
    }
    return false;
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
            string intitule(tuple[1]);
            float prix = atof(tuple[2]);
            int stock = atoi(tuple[3]);
            string image(tuple[4]);

            Article art(id, intitule, stock, image, prix);
            return art;
        }
    }
}

Article buyArticleOnDB(int idArticle, int quantite, MYSQL* conn)
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
        if((resultat = mysql_store_result(conn)) == NULL)
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
            float prix = atoi(tuple[2]);
            int stockReel = atof(tuple[3]);
            string image = tuple[4];

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

void createInvoiceOnDB(Article* panier[])
{
    char requete[256];
    sprintf(requete, "insert into factures() values();")
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
    printf("Trace: passage fct retire() / socket = %d\n", socket);

    bool estAjoute = false;

    pthread_mutex_lock(&mutexClients);
        if(nbClients < NB_MAX_CLIENTS)
        {
            clients[nbClients] = socket;
            nbClients++;

            estAjoute = true;
        }
        afficheVecClients("ajoute");

    pthread_mutex_unlock(&mutexClients);
    return estAjoute;
}

//permet de retirer un client(socket) dans le vecteur global
void retire(int socket)
{
    printf("Trace: passage fct retire() / socket = %d\n", socket);

    pthread_mutex_lock(&mutexClients);
        bool found = false;
        int i;
        for(i = 0; i < NB_MAX_CLIENTS && !found; i++)
        {
            if(clients[i] == socket)
            {
                found = true;
            }
        }

        if(found)
        {
            i--; //car i a été incrémenté après avoir passé found à "true" et donc avant de sortir de la boucle.
            while(i < (nbClients-1) )
            {
                clients[i] = clients[i+1];
                i++;
            }
            nbClients--;
        }
        afficheVecClients("retire");
        
    pthread_mutex_unlock(&mutexClients);
}

void afficheVecClients(const char* enTete)
{
    printf("---Affichage Clients connectes (sockets) dans fct %s---\n", enTete);
    for(int i = 0; i < NB_MAX_CLIENTS; i++)
    {
        printf("%d\t", clients[i]);
    }
    printf("\n--------------------------------------------\n");
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