#ifndef ARTICLE_H
#define ARTICLE_H

#include <cstring>
#include <iostream>
#include "Exception.h"

using namespace std;

class Article
{
    private:
        int id;
        string intitule;
        int quantite;
        string image;
        float prix;

    public:
        
        //Constructeurs
        Article();
        Article(int id, string intitule, int qte, string img, float prix);
        Article(const Article& art);

        //Setters
        void setId(int id);
        void setIntitule(string intitule);
        void setQte(int qte);
        void setImage(string img);
        void setPrix(float prix);


        //Getters
        int getId() const;
        string getIntitule() const;
        int getQte() const;
        string getImage() const;
        float getPrix() const;

        void Affiche();
};

#endif