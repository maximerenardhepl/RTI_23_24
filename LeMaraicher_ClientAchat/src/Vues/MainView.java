package Vues;

import Controleur.Controler;

import javax.swing.*;

public class MainView extends JFrame{
    private JButton btnPrecedent;
    private JButton btnSuivant;
    private JButton btnAcheter;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JSpinner spinner1;
    private JTextPane textPane1;
    private JTable table1;
    private JButton supprimerArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerAchatButton;
    private JTextField textField4;

    public void setControler(Controler c) {
        confirmerAchatButton.addActionListener(c);
        viderLePanierButton.addActionListener(c);
    }

    public JButton getBtnPrecedent() {
        return btnPrecedent;
    }

    public JButton getBtnSuivant() {
        return btnSuivant;
    }

    public JButton getBtnAcheter() {
        return btnAcheter;
    }
}
