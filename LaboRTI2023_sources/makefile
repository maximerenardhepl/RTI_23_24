.SILENT:

CLI=ClientQt
depClient=$(CLI)/mainclient.o $(CLI)/windowclient.o $(CLI)/moc_windowclient.o $(SOCK)/LibSocket.o $(OVESP)/OVESP_C.o $(DATA)/Article.o $(DATA)/Exception.o

DATA_DEPENDENCIES=$(DATA)/Article.o $(DATA)/Exception.o $(DATA)/DataBaseException.o

SERV=Serveur_Achat
OVESP=OVESProtocol
SOCK=LibSocket
BD=BD_Maraicher
DATA=Data

all:	$(DATA_DEPENDENCIES) ServeurAchat Client CreationBD

ServeurAchat:	$(SERV)/ServeurAchat.cpp $(SOCK)/LibSocket.o $(OVESP)/OVESP_S.o
	echo "Creation ServeurAchat"
	g++ $(SERV)/ServeurAchat.cpp $(SOCK)/LibSocket.o $(OVESP)/OVESP_S.o $(DATA_DEPENDENCIES) -o ServeurAchat -lpthread -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lm -lrt -lssl -lcrypto -ldl -lresolv

$(SOCK)/LibSocket.o:	$(SOCK)/LibSocket.h $(SOCK)/LibSocket.cpp
	echo "Creation LibSocket.o"
	g++ $(SOCK)/LibSocket.cpp -o $(SOCK)/LibSocket.o -c

$(OVESP)/OVESP_S.o:	$(OVESP)/OVESP_S.h $(OVESP)/OVESP_S.cpp
	echo "Creation OVESP_S.o"
	g++ $(OVESP)/OVESP_S.cpp -o $(OVESP)/OVESP_S.o -c -I/usr/include/mysql -m64 -L/usr/lib64/mysql




Client:	$(depClient)
	echo "Creation Client"
	g++ -Wno-unused-parameter -o Client $(DATA)/Article.o $(DATA)/Exception.o $(SOCK)/LibSocket.o $(OVESP)/OVESP_C.o $(CLI)/mainclient.o $(CLI)/windowclient.o $(CLI)/moc_windowclient.o  /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread

$(OVESP)/OVESP_C.o:	$(OVESP)/OVESP_C.h $(OVESP)/OVESP_C.cpp
	echo "Creation OVESP_C.o"
	g++ $(OVESP)/OVESP_C.cpp -o $(OVESP)/OVESP_C.o -c


$(CLI)/mainclient.o:	$(CLI)/mainclient.cpp
	echo "Creation mainclient.o"
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/mainclient.o $(CLI)/mainclient.cpp

$(CLI)/windowclient.o:	$(CLI)/windowclient.h $(CLI)/windowclient.cpp
	echo "Creation windowclient.o"
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/windowclient.o $(CLI)/windowclient.cpp

$(CLI)/moc_windowclient.o:	$(CLI)/ui_windowclient.h $(CLI)/moc_windowclient.cpp
	echo "Creation moc_windowclient.o"
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/moc_windowclient.o $(CLI)/moc_windowclient.cpp




$(DATA)/Article.o:	$(DATA)/Article.h $(DATA)/Article.cpp
	echo "Creation Article.o"
	g++ $(DATA)/Article.cpp -o $(DATA)/Article.o -c

$(DATA)/Exception.o:	$(DATA)/Exception.h $(DATA)/Exception.cpp
	echo "Creation Exception.o"
	g++ $(DATA)/Exception.cpp -o $(DATA)/Exception.o -c

$(DATA)/DataBaseException.o:	$(DATA)/DataBaseException.h $(DATA)/DataBaseException.cpp
	echo "Creation DataBaseException.o"
	g++ $(DATA)/DataBaseException.cpp -o $(DATA)/DataBaseException.o -c




CreationBD:	$(BD)/CreationBD.cpp
	echo "Creation de CreationBD"
	g++ -o CreationBD $(BD)/CreationBD.cpp -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl




clean:
	echo "Suppression des fichiers .o"
	find . -name "*.o" -type f -exec rm -f {} +
	

clobber:
	echo "Suppression des fichiers executables"
	rm ServeurAchat
	rm Client
	rm CreationBD
