#include "Exception.h"

Exception::Exception()
{
	#ifdef DEBUG
	cout << ">>> Classe Exception : Constructeur par défaut <<<" << endl;
	#endif
}

Exception::Exception(string msg)
{
	#ifdef DEBUG
	cout << "Classe Exception : Constructeur d'initialisation <<<" << endl;
	#endif
	setMessage(msg);
}

Exception::Exception(const Exception &exception)
{
	#ifdef DEBUG
	cout << "Classe Exception : Constructeur de copie <<<" << endl;
	#endif
	setMessage(exception.getMessage());
}

Exception::~Exception()
{
	#ifdef DEBUG
	cout << ">>> Classe Exception : Destructeur <<<" << endl;
	#endif
}

void Exception::setMessage(string msg)
{
	if(msg.length() > 0)
		message = msg;
}

string Exception::getMessage() const { return message; }