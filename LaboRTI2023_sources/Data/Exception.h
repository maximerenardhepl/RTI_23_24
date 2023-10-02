#ifndef EXCEPTION_H
#define EXCEPTION_H

#include <iostream>
#include <cstring>
#include <exception>

using namespace std;

class Exception : public exception
{

    protected:
	    string message;

    public:
        Exception();
        Exception(string msg);
        Exception(const Exception &exception);
        ~Exception();

        void setMessage(string msg);
        string getMessage(void) const;
};

#endif