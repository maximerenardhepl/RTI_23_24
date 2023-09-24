#.SILENT:

CLI=ClientQt
depClient=$(CLI)/mainclient.o $(CLI)/windowclient.o $(CLI)/moc_windowclient.o

SERV=Serveur_Achat
PC=ProtocolCommunication
SOCK=LibSocket

all:	ServeurAchat Client

ServeurAchat:	$(SERV)/ServeurAchat.cpp $(SOCK)/LibSocket.o $(PC)/CP.o
	g++ $(SERV)/ServeurAchat.cpp $(SOCK)/LibSocket.o $(PC)/CP.o -o ServeurAchat -lpthread

LibSocket.o:	$(SOCK)/LibSocket.h $(SOCK)/LibSocket.cpp
	g++ $(SOCK)/LibSocket.cpp -o $(SOCK)/LibSocket.o -c

CP.o:	$(PC)/CP.h $(PC)/CP.cpp
	g++ $(PC)/CP.cpp -o $(PC)/CP.o -c


Client:	$(depClient)
	g++ -Wno-unused-parameter -o Client $(SOCK)/LibSocket.o $(CLI)/mainclient.o $(CLI)/windowclient.o $(CLI)/moc_windowclient.o  /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread

$(CLI)/mainclient.o:	$(CLI)/mainclient.cpp $(SOCK)/LibSocket.o
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/mainclient.o $(SOCK)/LibSocket.o $(CLI)/mainclient.cpp

$(CLI)/windowclient.o:	$(CLI)/windowclient.h $(CLI)/windowclient.cpp
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/windowclient.o $(CLI)/windowclient.cpp

$(CLI)/moc_windowclient.o:	$(CLI)/ui_windowclient.h $(CLI)/moc_windowclient.cpp
	g++ -Wno-unused-parameter -c -pipe -g -std=gnu++11 -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++ -o $(CLI)/moc_windowclient.o $(CLI)/moc_windowclient.cpp

clean:
	rm $(PC)/*.o
	rm $(SOCK)/*.o

clobber:
	rm ServeurAchat
	rm Client
