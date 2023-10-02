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

        //Setters
        void setId(int id);
        void setIntitule(string intitule);
        void setQte(int qte);
        void setImage(string img);
        void setPrix(float prix);


        //Getters
        int getId();
        string getIntitule();
        int getQte();
        string getImage();
        float getPrix();
};

#endif