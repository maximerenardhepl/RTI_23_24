package Modele.Interfaces;

import Modele.ThSocketEvent;

import java.util.EventListener;

public interface ActionSocketListener extends EventListener {

    public void actionSocketDetected(ThSocketEvent e);
}
