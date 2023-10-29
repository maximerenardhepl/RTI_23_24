package Vues;

import Controleur.Controler;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{

    private JMenuItem menuDeconnexion;
    private JButton btnPrecedent;
    private JButton btnSuivant;
    private JButton btnAcheter;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JSpinner spinner1;
    private JTextPane bienvueSurLeMaraicherTextPane;
    private JTable table1;
    private JButton supprimerArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerAchatButton;
    private JTextField textField4;
    private JPanel mainPanel;

    public MainView() {
        setContentPane(mainPanel);
        setSize(900, 500);
        setMinimumSize(new Dimension(900, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuPrincipal = new JMenuBar();
        setJMenuBar(menuPrincipal);

        JMenu menuProfil = new JMenu("Profil");
        menuDeconnexion = new JMenuItem("Deconnexion");
        menuProfil.add(menuDeconnexion);
        menuPrincipal.add(menuProfil);
    }

    public void setControler(Controler c) {
        menuDeconnexion.addActionListener(c);
        confirmerAchatButton.addActionListener(c);
        viderLePanierButton.addActionListener(c);
        supprimerArticleButton.addActionListener(c);
        btnAcheter.addActionListener(c);
        btnSuivant.addActionListener(c);
        btnPrecedent.addActionListener(c);

        this.addWindowListener(c);
    }

    public JMenuItem getMenuDeconnexion() { return menuDeconnexion; }

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
