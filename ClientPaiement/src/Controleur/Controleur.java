package Controleur;

import Classes.Facture;
import Modele.VESPAP;
import Vue.Principale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controleur implements ActionListener {
    private Principale vuePrincipale;

    public Controleur(Principale vuePrincipale)
    {
        this.vuePrincipale = vuePrincipale;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(vuePrincipale != null) {
            if(e.getSource() == vuePrincipale.getLoginButton()) {
                onPush_BtnLogin();
            }
            else if(e.getSource() == vuePrincipale.getMenuDeconnexion()) {
                onPush_BtnLogout();
            }
            else if(e.getSource() == vuePrincipale.getVoirFacturesButton()) {
                onPush_BtnVoirFactures();
            }
            else if(e.getSource() == vuePrincipale.getPayerFactureButton()) {
                onPush_BtnPayerFacture();
            }
        }
    }

    private void onPush_BtnLogin() {
        String username = vuePrincipale.getTxtFieldUsername().getText();
        String password = String.valueOf(vuePrincipale.getTxtFieldPassword().getPassword());
        if(!username.isEmpty() && !password.isEmpty()) {
            try {
                boolean identifiantsOk = VESPAP.getInstance().Login(username, password);
                if(identifiantsOk) {
                    vuePrincipale.ActiveVuePrincipale();
                    String bienvenue = "Connexion établie! Bienvenue " + username + "!";
                    JOptionPane.showMessageDialog(vuePrincipale, bienvenue, "Bienvenue", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(vuePrincipale, e.getMessage(), "Erreur de login", JOptionPane.ERROR_MESSAGE);
                vuePrincipale.getTxtFieldPassword().setText("");
            }
        }
        else {
            String message = "Erreur! Veuillez renseigner un login et un mot de passe!";
            JOptionPane.showMessageDialog(vuePrincipale, message, "Champs Manquants", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPush_BtnLogout() {
        VESPAP.getInstance().Logout(VESPAP.getInstance().getInfoclient());
        vuePrincipale.DesactiveVuePrincipale();
        JOptionPane.showMessageDialog(vuePrincipale, "Vous êtes bien déconnecté!", "Déconnexion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onPush_BtnVoirFactures() {
        try {
            String numClientStr = vuePrincipale.getTxtFieldNumClient().getText();
            int numClient = Integer.parseInt(numClientStr);

            ArrayList<Facture> listeFacture = VESPAP.getInstance().GetFactures(numClient);
            vuePrincipale.getTableModelFactures().updateDataSource(listeFacture);
        }
        catch(NumberFormatException e) {
            String message = "Numéro de client invalide! Le numéro de client doit contenir uniquement des chiffres.";
            JOptionPane.showMessageDialog(vuePrincipale, message, "Erreur Saisie", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(vuePrincipale, e.getMessage(), "Erreur de login", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void onPush_BtnPayerFacture() {

    }
}
