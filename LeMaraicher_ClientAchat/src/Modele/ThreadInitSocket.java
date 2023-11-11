package Modele;

import Modele.Interfaces.ActionSocketListener;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ThreadInitSocket extends Thread {

    private boolean isSocketInit;
    ArrayList<ActionSocketListener> listListeners;

    public ThreadInitSocket() {
        isSocketInit = false;
        listListeners = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            Ovesp.getInstance().init();
            isSocketInit = true;
        }
        catch(IOException e) {
            System.out.println("Serveur injoingnable...");
        }

        notifySocketEvent();
    }

    public void addSocketListener(ActionSocketListener listener)
    {
        if(!listListeners.contains(listener)) {
            listListeners.add(listener);
        }
    }

    public void removeSocketListener(ActionSocketListener listener)
    {
        if(listListeners.contains(listener)) {
            listListeners.remove(listener);
        }
    }

    private void notifySocketEvent()
    {
        ThSocketEvent e = new ThSocketEvent(this, isSocketInit);
        for(int i=0; i < listListeners.size(); i++)
        {
            ActionSocketListener listener = listListeners.get(i);
            listener.actionSocketDetected(e);
        }
    }
}
