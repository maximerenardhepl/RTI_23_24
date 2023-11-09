package Controleur;

import Modele.Article;
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
                else if (e.getSource() == refMainView.getBtnPrecedent()) {
                    if (Ovesp.getInstance().getNumArt() == 1) {
                        Ovesp.getInstance().setNumArt(21);
                        MiseAJour();
                    } else {
                        Ovesp.getInstance().setNumArt(Ovesp.getInstance().getNumArt() - 1);
                        MiseAJour();
                    }
                }
                else if (e.getSource() == refMainView.getBtnSuivant()) {
                    if (Ovesp.getInstance().getNumArt() == 21) {
                        Ovesp.getInstance().setNumArt(1);
                        MiseAJour();
                    } else {
                        Ovesp.getInstance().setNumArt(Ovesp.getInstance().getNumArt() + 1);
                        MiseAJour();
                    }
                }
                else if(e.getSource() == refMainView.getBtnAcheter()) {

                    //on récupère l'article courant
                    Ovesp.getInstance().getNumArt();

                }
                else if(e.getSource() == refMainView.getBtnSupprimerArticle()) {
                    int artSelectionne = refMainView.getTablePanier().getSelectedRow();
                    if(artSelectionne != -1) {
                        try {
                            Ovesp.getInstance().cancel(artSelectionne);
                        }
                        catch(Exception ex) {
                            JOptionPane.showMessageDialog(refMainView, ex.getMessage(), "Erreur suppression d'article", JOptionPane.ERROR_MESSAGE);
                        }
                    }
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

    public void MiseAJour()
    {
        Article Art;

        try {
            Ovesp.getInstance().Consult(Ovesp.getInstance().getNumArt());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        //afficher dans les champs de l'objet retourner par consult qui seraius une variable membres de mon singleton
        Art = Ovesp.getInstance().getArtCourant();

        refMainView.SetLabelArticle(Art.getIntitule());

        String stock = Integer.toString(Art.getQuantite());
        refMainView.SetLabelStock(stock);

        String prix = Float.toString(Art.getPrix());
        refMainView.SetLabelPrix(prix);
        //refMainView.SetLabelImage(Art.getImage());
    }
}
