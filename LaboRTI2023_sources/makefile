#.SILENT:

SERV=Serveur_Achat

ServeurAchat:	$(SERV)/ServeurAchat.cpp LibSocket.o
	g++ $(SERV)/ServeurAchat.cpp LibSocket.o -o ServeurAchat -lpthread

LibSocket.o:	$(SERV)/LibSocket.h $(SERV)/LibSocket.cpp
	g++ $(SERV)/LibSocket.cpp -o LibSocket.o -c

clean:
	rm *.o

clobber:
	rm ServeurAchat
