#include "AchatArticleException.h"

const int AchatArticleException::INSUFFICIENT_STOCK = 3;

AchatArticleException::AchatArticleException() : Exception() { }

AchatArticleException::AchatArticleException(string msg, int code) : Exception(msg)
{
    this->setCode(code);
}

void AchatArticleException::setCode(int code)
{
    if(code == INSUFFICIENT_STOCK)
    {
        this->code = code;
    }
}

int AchatArticleException::getCode() { return this->code; }