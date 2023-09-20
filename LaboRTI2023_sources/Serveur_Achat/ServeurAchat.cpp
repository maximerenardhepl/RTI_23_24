#include <iostream>
#include "LibSocket.h"
using namespace std;

int main()
{
    int sEcoute = 0;
    if((sEcoute = ServerSocket(1200)) == -1)
    {
        perror("Erreur de ServerSocket");
        exit(1);
    }

    int sService = 0;
    while(1)
    {
        if((sService = Accept()) != -1)
        {
            
        }
    }


    return 0;
}