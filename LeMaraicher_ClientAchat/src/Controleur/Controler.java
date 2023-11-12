package Controleur;

import Modele.Article;
import Modele.Interfaces.ActionSocketListener;
import Modele.Ovesp;
import Modele.ThSocketEvent;
import Modele.ThreadInitSocket;
import Vues.ConnectionView;
import Vues.LoadingConnection;
import Vues.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class Controler extends WindowAdapter implements ActionListener, MouseListener, MouseMotionListener, ActionSocketListener {
    private ConnectionView refConnectionView;
    private MainView refMainView;

    private boolean isSocketInit;
    private boolean isViewRegisterDisplayed; //indique si ce qui est affiché sur la vue de Connexion est la vue pour se connecter ou bien la vue pour s'inscrire.

    public Controler() {
        isViewRegisterDisplayed = false;
        isSocketInit = false;
    }

    public void setRefView(MainView view) {
        refMainView = view;
    }
    public void setRefConnectionView(ConnectionView view) { refConnectionView = view; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == refConnectionView.getBtnConnexion()) {
            refConnectionView.afficheChargement();

            ThreadInitSocket th = new ThreadInitSocket();
            th.addSocketListener(this);
            th.start();

            //Le login se fera réellement lors de la réception de la réponse de la connexion du thread au serveur...
        }
        else {
            if(refMainView != null) {
                if(e.getSource() == refMainView.getMenuDeconnexion()) {
                    if(isViewRegisterDisplayed) {
                       isViewRegisterDisplayed = false; //On réinitialise le boolean dans le cas ou la connexion qui vient d'avoir lieu était une inscription...
                    }
                    try {
                        if(!Ovesp.getInstance().getPanier().isEmpty()) {
                            Ovesp.getInstance().cancelAll();
                            refMainView.getModeleTablePanier().clearTable();
                        }

                        Ovesp.getInstance().setNumArt(0); //On remet à 0 le NumArt pour la prochaine connexion qui aura lieu...
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
                    catch(Exception ex) {
                        JOptionPane.showMessageDialog(refMainView, ex.getMessage(), "Erreur suppression panier - Logout", JOptionPane.ERROR_MESSAGE);
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
                    try {
                        Article art = Ovesp.getInstance().Achat( Ovesp.getInstance().getNumArt(),refMainView.getSpinnerQteArticle());
                        ajouteArticlePanier(art);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(refMainView, ex.getMessage(), "Erreur Achat", JOptionPane.ERROR_MESSAGE);
                    }

                }
                else if(e.getSource() == refMainView.getBtnSupprimerArticle()) {
                    JTable refJTable = refMainView.getTablePanier();
                    int artSelectionne = refJTable.getSelectedRow();
                    if(artSelectionne != -1) {
                        try {
                            Ovesp.getInstance().cancel(artSelectionne);
                            refMainView.getModeleTablePanier().removeRow(artSelectionne);
                        }
                        catch(Exception ex) {
                            JOptionPane.showMessageDialog(refMainView, ex.getMessage(), "Erreur suppression d'article", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        System.out.println("aucune ligne selectionnee...");
                    }
                }
                else if(e.getSource() == refMainView.getBtnViderPanier()) {
                    try {
                        if(!Ovesp.getInstance().getPanier().isEmpty()) {
                            Ovesp.getInstance().cancelAll();
                            refMainView.getModeleTablePanier().clearTable();
                        }
                    }
                    catch(Exception ex) {
                        JOptionPane.showMessageDialog(refMainView, ex.getMessage(), "Erreur suppression panier", JOptionPane.ERROR_MESSAGE);

                    }
                }
                else if(e.getSource() == refMainView.getBtnConfirmerAchat()) {
                    try {
                        Ovesp.getInstance().Confirm(Ovesp.getInstance().loginUser);
                    }
                    catch(Exception ex) {

                    }
                }
            }
        }
    }


    //Permet de détecter le résultat de l'initialisation de la socket lors de la connexion au serveur.
    @Override
    public void actionSocketDetected(ThSocketEvent e) {
        if(e.isInitSuccessful()) {
            isSocketInit = true;

            boolean isNewClient = false;
            if(isViewRegisterDisplayed) {
                isNewClient = true;
            }

            try {
                String username = refConnectionView.getTxtFieldLogin().getText();
                String password = String.copyValueOf(refConnectionView.getTxtFieldPassword().getPassword());

                if(Ovesp.getInstance().login(username, password, isNewClient)) {
                    refMainView = new MainView();
                    refMainView.setControler(this);
                    refConnectionView.dispose();
                    refMainView.setVisible(true);


                    if (Ovesp.getInstance().getNumArt() == 21) {
                        Ovesp.getInstance().setNumArt(1);
                        MiseAJour();
                    } else {
                        Ovesp.getInstance().setNumArt(Ovesp.getInstance().getNumArt() + 1);
                        MiseAJour();
                    }
                }
            }
            catch(Exception ex) {
                refConnectionView.affichePanelConnexion();
                JOptionPane.showMessageDialog(refConnectionView, ex.getMessage(), "Login - Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            LoadingConnection vueChargement = refConnectionView.getVueChargement();
            vueChargement.getTextPaneMsg().setText("Une erreur est survenue! Impossible de joindre le serveur...Veuillez réessayer!");
            vueChargement.getPanelBoutons().setVisible(true);

            vueChargement.getBtnAbandonner().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    vueChargement.getTextPaneMsg().setText("Veuillez patienter pendant le chargement...");
                    vueChargement.getPanelBoutons().setVisible(false);
                    refConnectionView.affichePanelConnexion();
                }
            });

            Controler refControler = this;
            vueChargement.getBtnReessayer().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    vueChargement.getPanelBoutons().setVisible(false);
                    vueChargement.getTextPaneMsg().setText("Veuillez patienter pendant le chargement...");

                    ThreadInitSocket th = new ThreadInitSocket();
                    th.addSocketListener(refControler);
                    th.start();
                }
            });
        }
    }

    public void windowClosing(WindowEvent e) {
        if(e.getSource() == refMainView) {
            try {
                if(!Ovesp.getInstance().getPanier().isEmpty()) {
                    Ovesp.getInstance().cancelAll();
                    refMainView.getModeleTablePanier().clearTable();
                }

                Ovesp.getInstance().logout();
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(refMainView, "Une erreur inconnue est survenue! Fermeture de la connexion. L'application va s'arrêter.", "Logout - Erreur", JOptionPane.ERROR_MESSAGE);
            }
            catch(Exception ex) {
                JOptionPane.showMessageDialog(refMainView, ex.getMessage(), "Erreur suppression panier - Logout", JOptionPane.ERROR_MESSAGE);
            }
        }

        if(isSocketInit) {
            Ovesp.getInstance().closeConnection();
        }
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
        refMainView.SetLabelImage(Art.getImage());
    }

    public void ajouteArticlePanier(Article nouveauArt) {
        boolean found = false;
        int i = 0;
        ArrayList<Article> refPanier = Ovesp.getInstance().getPanier();

        //Vérifie si cet article qu'on veut ajouter n'est par hasard pas déjà présent dans le vecteur d'articles...
        while(i < refPanier.size() && !found)
        {
            Article artCourant = refPanier.get(i);
            if(artCourant.getId() == nouveauArt.getId()) {
                found = true;
            }
            else {
                i++;
            }
        }

        if(found)
        {
            //On a trouvé le meme article deja existant -> on incremente simplement la quantite.
            int qteActuelle = refPanier.get(i).getQuantite();
            int nouvelleQte = qteActuelle + nouveauArt.getQuantite();
            refPanier.get(i).setQuantite(nouvelleQte);
        }
        else
        {
            refMainView.getModeleTablePanier().addRow(nouveauArt);
        }
    }
}
