package Controleur;

import Modele.Ovesp;
import Vues.ConnectionView;
import Vues.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Controler extends WindowAdapter implements ActionListener, MouseListener, MouseMotionListener {
    private ConnectionView refConnectionView;
    private MainView refMainView;

    private boolean isViewRegisterDisplayed; //indique si ce qui est affiché sur la vue de Connexion est la vue pour se connecter ou bien la vue pour s'inscrire.

    public Controler() {
        isViewRegisterDisplayed = false;
    }

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
                boolean isNewClient = false;
                if(isViewRegisterDisplayed) {
                    isNewClient = true;
                }

                if(Ovesp.getInstance().login(username, password, isNewClient)) {
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
                    if(isViewRegisterDisplayed) {
                       isViewRegisterDisplayed = false; //On réinitialise le boolean dans le cas ou la connexion qui vient d'avoir lieu était une inscription...
                    }
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == refConnectionView.getLabelNotRegister()) {
            if(isViewRegisterDisplayed) {
                refConnectionView.getLabelNotRegister().setText("Pas encore inscrit ?");
                refConnectionView.getBtnConnexion().setText("Se connecter");
                isViewRegisterDisplayed = false;
            }
            else {
                refConnectionView.getLabelNotRegister().setText("J'ai déjà un compte");
                refConnectionView.getBtnConnexion().setText("S'inscrire");
                isViewRegisterDisplayed = true;
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        refConnectionView.getLabelNotRegister().setForeground(Color.BLACK);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        refConnectionView.getLabelNotRegister().setForeground(Color.BLUE);
    }
}
