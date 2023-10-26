package Controleur;

import Vues.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controler implements ActionListener {
    private MainView refMainView;

    public void setRefView(MainView view) {
        refMainView = view;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refMainView.getBtnPrecedent()) {

        }
        else if(e.getSource() == refMainView.getBtnSuivant()) {

        }
        else if(e.getSource() == refMainView.getBtnAcheter()) {

        }
    }
}
