package Controleur;

import Modele.Ovesp;
import Vues.ConnectionView;
import Vues.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Controler extends WindowAdapter implements ActionListener {
    private ConnectionView refConnectionView;
    private MainView refMainView;

    public void setRefView(MainView view) {
        refMainView = view;
    }
    public void setRefConnectionView(ConnectionView view) { refConnectionView = view; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refConnectionView.getBtnConnexion()) {
            String username = refConnectionView.getTxtFieldLogin().getText();
            String password = String.copyValueOf(refConnectionView.getTxtFieldPassword().getPassword());

            try {
                if(Ovesp.getInstance().login(username, password)) {
                    refMainView = new MainView();
                    refMainView.setControler(this);
                    refConnectionView.dispose();
                    refMainView.setVisible(true);
                }
            }
            catch(Exception ex) {
                JOptionPane.showMessageDialog(refConnectionView, ex.getMessage(), "Login - Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            if(refMainView != null) {
                if(e.getSource() == refMainView.getMenuDeconnexion()) {
                    try {
                        Ovesp.getInstance().logout();

                        refConnectionView = new ConnectionView();
                        refConnectionView.setControler(this);
                        refMainView.dispose();
                        refConnectionView.setVisible(true);
                    }
                    catch(IOException ex) {
                        JOptionPane.showMessageDialog(refMainView, "Une erreur inconnue est survenue! Fermeture de la connexion. L'application va s'arrêter.", "Logout - Erreur", JOptionPane.ERROR_MESSAGE);
                        refMainView.dispose();
                    }

                }
                else if(e.getSource() == refMainView.getBtnPrecedent()) {

                }
                else if(e.getSource() == refMainView.getBtnSuivant()) {

                }
                else if(e.getSource() == refMainView.getBtnAcheter()) {

                }
            }
        }
    }

    public void windowClosing(WindowEvent e) {
        if(e.getSource() == refMainView) {
            try {
                Ovesp.getInstance().logout();
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(refMainView, "Une erreur inconnue est survenue! Fermeture de la connexion. L'application va s'arrêter.", "Logout - Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        Ovesp.getInstance().closeConnection();
    }
}
