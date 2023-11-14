package Controleur;

import Modele.VESPAP;
import Vue.Principale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        String username = vuePrincipale.getUsername();
        String password = vuePrincipale.getPassword();
        if(!username.isEmpty() && !password.isEmpty()) {
            try {
                boolean identifiantsOk = VESPAP.getInstance().Login(username, password);
                if(identifiantsOk) {
                    String bienvenue = "Connexion établie! Bienvenue " + username + "!";
                    JOptionPane.showMessageDialog(vuePrincipale, bienvenue, "Bienvenue", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(vuePrincipale, e.getMessage(), "Erreur de login", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            String message = "Erreur! Veuillez renseigner un login et un mot de passe!";
            JOptionPane.showMessageDialog(vuePrincipale, message, "Champs Manquants", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPush_BtnLogout() {

    }

    private void onPush_BtnVoirFactures() {

    }

    private void onPush_BtnPayerFacture() {

    }
}
