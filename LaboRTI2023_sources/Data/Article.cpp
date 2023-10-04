#include "Article.h"

Article::Article() : Article(1, "default", 0, "default", 0.0) { }

Article::Article(int id, string intitule, int qte, string img, float prix)
{
    setId(id);
    setIntitule(intitule);
    setQte(qte);
    setImage(img);
    setPrix(prix);
}



void Article::setId(int id)
{
    if(id < 1)
    {
        throw Exception("Id d'article invalide!");
    }
    this->id = id;
}

void Article::setIntitule(string intitule)
{
    if(intitule.empty())
    {
        throw Exception("Intitule d'article invalide!");
    }
    this->intitule = intitule;
}

void Article::setQte(int qte)
{
    if(qte < 0)
    {
        throw Exception("Quantite d'article invalide!");
    }
    this->quantite = qte;
}

void Article::setImage(string img)
{
    if(img.empty())
    {
        throw Exception("Image d'article invalide!");
    }
    this->image = img;
}

void Article::setPrix(float prix)
{
    if(prix < 0)
    {
        throw Exception("Prix d'article invalide!");
    }
    this->prix = prix;
}



int Article::getId() { return id; }

string Article::getIntitule() { return intitule; }

int Article::getQte() { return quantite; }

string Article::getImage() { return image; }

float Article::getPrix() { return prix; }