#include "windowclient.h"
#include "ui_windowclient.h"
#include <QMessageBox>
#include <string>

#include <sys/socket.h>
#include <unistd.h>

#include "../Data/Article.h"
#include "../Data/Exception.h"
#include "../OVESProtocol/OVESP_C.h"

using namespace std;

extern WindowClient *w;

#define NB_ARTICLE 21

int socketC = 0;

string loginClient;

Article articleEnCours;
Article* panier[NB_ARTICLE];

void InitPanier(Article* panier[]);
bool ajouteArticlePanier(Article& nouveauArt);
void majTablePanier();

#define REPERTOIRE_IMAGES "ClientQt/images/"

WindowClient::WindowClient(int sClient,QWidget *parent) : QMainWindow(parent), ui(new Ui::WindowClient)
{
    ui->setupUi(this);

    // Configuration de la table du panier (ne pas modifer)
    ui->tableWidgetPanier->setColumnCount(3);
    ui->tableWidgetPanier->setRowCount(0);
    QStringList labelsTablePanier;
    labelsTablePanier << "Article" << "Prix à l'unité" << "Quantité";
    ui->tableWidgetPanier->setHorizontalHeaderLabels(labelsTablePanier);
    ui->tableWidgetPanier->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetPanier->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetPanier->horizontalHeader()->setVisible(true);
    ui->tableWidgetPanier->horizontalHeader()->setDefaultSectionSize(160);
    ui->tableWidgetPanier->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetPanier->verticalHeader()->setVisible(false);
    ui->tableWidgetPanier->horizontalHeader()->setStyleSheet("background-color: lightyellow");

    ui->pushButtonPayer->setText("Confirmer achat");
    setPublicite("!!! Bienvenue sur le Maraicher en ligne !!!");

    socketC = sClient;
    InitPanier(panier);
}

WindowClient::~WindowClient()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles : ne pas modifier /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setNom(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditNom->clear();
    return;
  }
  ui->lineEditNom->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getNom()
{
  strcpy(nom,ui->lineEditNom->text().toStdString().c_str());
  return nom;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setMotDePasse(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditMotDePasse->clear();
    return;
  }
  ui->lineEditMotDePasse->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getMotDePasse()
{
  strcpy(motDePasse,ui->lineEditMotDePasse->text().toStdString().c_str());
  return motDePasse;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setPublicite(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditPublicite->clear();
    return;
  }
  ui->lineEditPublicite->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setImage(const char* image)
{
  // Met à jour l'image
  char cheminComplet[80];
  sprintf(cheminComplet,"%s%s",REPERTOIRE_IMAGES,image);
  QLabel* label = new QLabel();
  label->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
  label->setScaledContents(true);
  QPixmap *pixmap_img = new QPixmap(cheminComplet);
  label->setPixmap(*pixmap_img);
  label->resize(label->pixmap()->size());
  ui->scrollArea->setWidget(label);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::isNouveauClientChecked()
{
  if (ui->checkBoxNouveauClient->isChecked()) return 1;
  return 0;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setArticle(const char* intitule,float prix,int stock,const char* image)
{
  ui->lineEditArticle->setText(intitule);
  if (prix >= 0.0)
  {
    char Prix[20];
    sprintf(Prix,"%.2f",prix);
    ui->lineEditPrixUnitaire->setText(Prix);
  }
  else ui->lineEditPrixUnitaire->clear();
  if (stock >= 0)
  {
    char Stock[20];
    sprintf(Stock,"%d",stock);
    ui->lineEditStock->setText(Stock);
  }
  else ui->lineEditStock->clear();
  setImage(image);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getQuantite()
{
  return ui->spinBoxQuantite->value();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setTotal(float total)
{
  if (total >= 0.0)
  {
    char Total[20];
    sprintf(Total,"%.2f",total);
    ui->lineEditTotal->setText(Total);
  }
  else ui->lineEditTotal->clear();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::loginOK()
{
  ui->pushButtonLogin->setEnabled(false);
  ui->pushButtonLogout->setEnabled(true);
  ui->lineEditNom->setReadOnly(true);
  ui->lineEditMotDePasse->setReadOnly(true);
  ui->checkBoxNouveauClient->setEnabled(false);

  ui->spinBoxQuantite->setEnabled(true);
  ui->pushButtonPrecedent->setEnabled(true);
  ui->pushButtonSuivant->setEnabled(true);
  ui->pushButtonAcheter->setEnabled(true);
  ui->pushButtonSupprimer->setEnabled(true);
  ui->pushButtonViderPanier->setEnabled(true);
  ui->pushButtonPayer->setEnabled(true);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::logoutOK()
{
  ui->pushButtonLogin->setEnabled(true);
  ui->pushButtonLogout->setEnabled(false);
  ui->lineEditNom->setReadOnly(false);
  ui->lineEditMotDePasse->setReadOnly(false);
  ui->checkBoxNouveauClient->setEnabled(true);

  ui->spinBoxQuantite->setEnabled(false);
  ui->pushButtonPrecedent->setEnabled(false);
  ui->pushButtonSuivant->setEnabled(false);
  ui->pushButtonAcheter->setEnabled(false);
  ui->pushButtonSupprimer->setEnabled(false);
  ui->pushButtonViderPanier->setEnabled(false);
  ui->pushButtonPayer->setEnabled(false);

  setNom("");
  setMotDePasse("");
  ui->checkBoxNouveauClient->setCheckState(Qt::CheckState::Unchecked);

  setArticle("",-1.0,-1,"");

  w->videTablePanier();
  w->setTotal(-1.0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table du panier (ne pas modifier) /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::ajouteArticleTablePanier(const char* article,float prix,int quantite)
{
    char Prix[20],Quantite[20];

    sprintf(Prix,"%.2f",prix);
    sprintf(Quantite,"%d",quantite);

    // Ajout possible
    int nbLignes = ui->tableWidgetPanier->rowCount();
    nbLignes++;
    ui->tableWidgetPanier->setRowCount(nbLignes);
    ui->tableWidgetPanier->setRowHeight(nbLignes-1,10);

    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(article);
    ui->tableWidgetPanier->setItem(nbLignes-1,0,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Prix);
    ui->tableWidgetPanier->setItem(nbLignes-1,1,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Quantite);
    ui->tableWidgetPanier->setItem(nbLignes-1,2,item);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::videTablePanier()
{
    ui->tableWidgetPanier->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getIndiceArticleSelectionne()
{
    QModelIndexList liste = ui->tableWidgetPanier->selectionModel()->selectedRows();
    if (liste.size() == 0) return -1;
    QModelIndex index = liste.at(0);
    int indice = index.row();
    return indice;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier ////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueMessage(const char* titre,const char* message)
{
   QMessageBox::information(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueErreur(const char* titre,const char* message)
{
   QMessageBox::critical(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// CLIC SUR LA CROIX DE LA FENETRE /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::closeEvent(QCloseEvent *event)
{
    on_pushButtonLogout_clicked();
    //close(socketC);
    exit(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions clics sur les boutons ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogin_clicked()
{
    try
    {
        //OVESP_Login lance une exception en cas d'erreur du Login.
        if(OVESP_Login(getNom(),getMotDePasse(), socketC, isNouveauClientChecked()) == true)
        {
          //Bien connecté -> acces aux différentes fonctionnalités.
          loginOK();
          loginClient = getNom();
          
          char msg[150];
          sprintf(msg, "Bienvenue %s! Vous etes maintenant bien connecte et pouvez proceder a vos achats!", getNom());
          
          w->dialogueMessage("Connexion etablie", msg);
          setNom("");
          setMotDePasse("");

          on_pushButtonSuivant_clicked();
        }
    }
    catch(Exception& e)
    {
        setNom("");
        setMotDePasse("");
        w->dialogueErreur("Erreur - Login", e.getMessage().c_str());
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogout_clicked()
{
    on_pushButtonViderPanier_clicked();
    OVESP_Logout(socketC);
    logoutOK();
    loginClient = "";
    articleEnCours.setId(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSuivant_clicked()
{
    if(articleEnCours.getId() == 21)
    {
        articleEnCours.setId(0);
    }
    
    try
    {
        Article art = OVESP_Consult(articleEnCours.getId()+1, socketC);

        articleEnCours.setId(art.getId());
        articleEnCours.setIntitule(art.getIntitule());
        articleEnCours.setQte(art.getQte());
        articleEnCours.setImage(art.getImage());
        articleEnCours.setPrix(art.getPrix());

        w->setArticle(art.getIntitule().c_str(), art.getPrix(), art.getQte(), art.getImage().c_str());
    }
    catch(Exception& e)
    {
        w->dialogueErreur("Erreur - Consultation d'article", e.getMessage().c_str());
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPrecedent_clicked()
{
    if(articleEnCours.getId() == 1)
    {
        articleEnCours.setId(22);
    }
    
    try
    {
        Article art = OVESP_Consult(articleEnCours.getId()-1, socketC);

        //Maj de l'objet articleEnCours qui représente l'article qui est affiche à l'écran actuellement pour l'utilisateur
        articleEnCours.setId(art.getId());
        articleEnCours.setIntitule(art.getIntitule());
        articleEnCours.setQte(art.getQte());
        articleEnCours.setImage(art.getImage());
        articleEnCours.setPrix(art.getPrix());

        w->setArticle(art.getIntitule().c_str(), art.getPrix(), art.getQte(), art.getImage().c_str());
    }
    catch(Exception& e)
    {
        w->dialogueErreur("Erreur - Consultation d'article", e.getMessage().c_str());
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonAcheter_clicked()
{
    try
    {
        Article art = OVESP_Achat(articleEnCours.getId(), w->getQuantite(), socketC);
        ajouteArticlePanier(art);

        int nouveauStock = (articleEnCours.getQte() - w->getQuantite());
        w->setArticle(art.getIntitule().c_str(), art.getPrix(), nouveauStock, art.getImage().c_str());
        
        //w->ajouteArticleTablePanier(art.getIntitule().c_str(), art.getPrix(), art.getQte());

        /*string msgConfirm = "Votre commande de ";
        msgConfirm += to_string(art.getQte());
        msgConfirm += " ";
        msgConfirm += art.getIntitule();
        msgConfirm += " a bien ete validee!";

        w->dialogueMessage("Achat confirmé", msgConfirm.c_str());*/
    }
    catch(Exception& e)
    {
        w->dialogueErreur("Erreur - Achat d'article", e.getMessage().c_str());
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSupprimer_clicked()
{
  if(getIndiceArticleSelectionne() == -1)
  {
    printf("faut appuyer");
  }
  else
  {
    OVESP_Cancel(socketC, panier, getIndiceArticleSelectionne());
    majTablePanier();
  }
    

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonViderPanier_clicked()
{
    //fonction qui va vider le panier du serveur comme du client et réincrémenter le stock
    OVESP_Cancel_All(socketC, panier);

    majTablePanier();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPayer_clicked()
{
    try
    {
        if(OVESP_Confirm(loginClient, socketC) == true)
        {
            //On vide complètement le panier en mémoire...
            for(int i=0; i < NB_ARTICLE && panier[i] != NULL; i++)
            {
                delete panier[i];
                panier[i] = NULL;
            }
            majTablePanier();
            w->dialogueMessage("Confirmation Achat", "Une facture detaillee a bien ete cree pour votre commande!\nMerci de consulter vos factures et de procéder au paiement.");
        }
    }
    catch(Exception& e)
    {
        w->dialogueErreur("Erreur - Confirmation du panier", e.getMessage().c_str());
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void InitPanier(Article* panier[])
{   
    //init le panier du client 
    for(int i=0; i < NB_ARTICLE; i++)
    {
        panier[i] = NULL;
    }
}

bool ajouteArticlePanier(Article& nouveauArt)
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

        //A partir d'ici, on a forcement modifie le tableau d'articles -> donc maj de l'interface graphique!
        majTablePanier();
        return true;
    }
    else
    {
        //On a pas trouvé d'espace disponible pour stocker l'article -> on return false (l'ajout ne s'est pas bien passé)
        return false;
    }
}

void majTablePanier()
{
    w->videTablePanier();
    for(int i=0; i < NB_ARTICLE && panier[i] != NULL; i++)
    {
        w->ajouteArticleTablePanier(panier[i]->getIntitule().c_str(), panier[i]->getPrix(), panier[i]->getQte());
    }
}
