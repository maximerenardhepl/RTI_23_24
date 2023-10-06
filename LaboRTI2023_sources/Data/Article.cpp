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

Article::Article(const Article& art)
{
    this->setId(art.getId());
    this->setIntitule(art.getIntitule());
    this->setQte(art.getQte());
    this->setImage(art.getImage());
    this->setPrix(art.getPrix());
}


void Article::setId(int id)
{
    if(id < 0)
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



int Article::getId() const { return id; }

string Article::getIntitule() const { return intitule; }

int Article::getQte() const { return quantite; }

string Article::getImage() const { return image; }

float Article::getPrix() const { return prix; }