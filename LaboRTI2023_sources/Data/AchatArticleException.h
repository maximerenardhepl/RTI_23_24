#ifndef ACHATARTICLEEXCEPTION_H
#define ACHATARTICLEEXCEPTION_H

class AchatArticleException : public Exception
{
    private:
        int code;

    public:
        static const int INSUFFICIENT_STOCK;

        AchatArticleException();
        AchatArticleException(string msg, int code);

        void setCode(int code);
        int getCode();
};

#endif