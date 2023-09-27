CLI=ClientQt
depClient=$(CLI)/mainclient.o $(CLI)/windowclient.o $(CLI)/moc_windowclient.o $(OVESP)/OVESP_Client.o

SERV=Serveur_Achat
OVESP=OVESProtocol
SOCK=LibSocket

all:	ServeurAchat Client

ServeurAchat:	$(SERV)/ServeurAchat.cpp $(SOCK)/LibSocket.o $(OVESP)/OVESP_Serv.o
	g++ $(SERV)/ServeurAchat.cpp $(SOCK)/LibSocket.o $(OVESP)/OVESP_Serv.o -o ServeurAchat -lpthread

LibSocket.o:	$(SOCK)/LibSocket.h $(SOCK)/LibSocket.cpp
	g++ $(SOCK)/LibSocket.cpp -o $(SOCK)/LibSocket.o -c

OVESP_Serv.o:	$(OVESP)/OVESP_Serv.h $(OVESP)/OVESP_Serv.cpp
	g++ $(OVESP)/OVESP_Serv.cpp -o $(OVESP)/OVESP_Serv.o -c

OVESP_Client.o:	$(OVESP)/OVESP_Client.h $(OVESP)/OVESP_Client.cpp
	g++ $(OVESP)/OVESP_Client.cpp -o $(OVESP)/OVESP_Client.o -c


Client:	$(depClient)
	g++ -Wno-unused-parameter -o Client $(SOCK)/LibSocket.o $(CLI)/mainclient.o $(CLI)/windowclient.o $(CLI)/moc_windowclient.o  /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread

$(CLI)/mainclient.o:	$(CLI)/mainclient.cpp $(SOCK)/LibSocket.o
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/mainclient.o $(SOCK)/LibSocket.o $(CLI)/mainclient.cpp

$(CLI)/windowclient.o:	$(CLI)/windowclient.h $(CLI)/windowclient.cpp
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/windowclient.o $(CLI)/windowclient.cpp

$(CLI)/moc_windowclient.o:	$(CLI)/ui_windowclient.h $(CLI)/moc_windowclient.cpp
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/moc_windowclient.o $(CLI)/moc_windowclient.cpp

clean:
	rm $(OVESP)/*.o
	rm $(SOCK)/*.o
	rm $(CLI)/*.o

clobber:
	rm ServeurAchat
	rm Client
