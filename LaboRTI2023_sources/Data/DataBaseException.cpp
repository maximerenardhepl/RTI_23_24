#include "DataBaseException.h"

const int DataBaseException::QUERY_ERROR = 1;
const int DataBaseException::EMPTY_RESULT_SET = 2;

DataBaseException::DataBaseException() : Exception() { }

DataBaseException::DataBaseException(string msg, int code) : Exception(msg)
{
    this->setCode(code);
}

void DataBaseException::setCode(int code)
{
    if(code == QUERY_ERROR || code == EMPTY_RESULT_SET)
    {
        this->code = code;
    }
}

int DataBaseException::getCode() { return this->code; }