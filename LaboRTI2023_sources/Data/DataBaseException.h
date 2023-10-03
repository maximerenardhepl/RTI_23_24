#ifndef DATABASEEXCEPTION_H
#define DATABASEEXCEPTION_H

#include <iostream>
#include <string>
#include "Exception.h"

using namespace std;

class DataBaseException : public Exception
{
    private:
        int code;

    public:
        static const int EMPTY_RESULT_SET;
        static const int QUERY_ERROR;

        DataBaseException();
        DataBaseException(string msg, int code);

        void setCode(int code);
        int getCode();
};

#endif