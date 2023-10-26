package Controleur;

import Modele.Ovesp;
import Vues.MainView;

import java.awt.event.*;
import java.io.IOException;

public class Controler extends WindowAdapter implements ActionListener {
    private MainView refMainView;

    public void setRefView(MainView view) {
        refMainView = view;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refMainView.getBtnPrecedent()) {
            Ovesp.getInstance().login();
        }
        else if(e.getSource() == refMainView.getBtnSuivant()) {

        }
        else if(e.getSource() == refMainView.getBtnAcheter()) {

        }
    }

    public void windowClosing(WindowEvent e) {
        try {
            System.out.println("test111");
            Ovesp.getInstance().closeConnection();
        }
        catch(IOException ex) {
               ex.printStackTrace();
        }
    }
}
