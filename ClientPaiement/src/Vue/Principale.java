package Vue;

import javax.swing.*;
import Controleur.Controleur;
import Modele.VESPAP;
import Vue.JTableModel.JTableModelFactureDetaillee;
import Vue.JTableModel.JTableModelFactures;

import java.util.ArrayList;

public class Principale extends JFrame{
    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField password;
    private JTextField username;
    private JTextField numClient;
    private JButton voirFacturesButton;
    private JButton payerFactureButton;
    private JTable tableFactures;
    private JTable tableFactureDetaillee;
    private JTableModelFactures tableModelFactures;
    private JTableModelFactureDetaillee tableModelFactureDetaillee;
    private JMenuItem menuDeconnexion;


    public JTextField getTxtFieldUsername() { return username; }
    public JPasswordField getTxtFieldPassword() { return password; }

    public JTextField getTxtFieldNumClient() { return numClient; }

    public JMenuItem getMenuDeconnexion() { return menuDeconnexion; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getVoirFacturesButton() { return  voirFacturesButton; }
    public JButton getPayerFactureButton() { return payerFactureButton; }

    public JTable getTableFactures() { return tableFactures; }
    public JTableModelFactures getTableModelFactures() { return tableModelFactures; }

    public JTableModelFactureDetaillee getTableModelFactureDetaillee() { return tableModelFactureDetaillee; }

    public Principale()
    {
        setContentPane(panel1);
        setSize(900, 530);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuPrincipal = new JMenuBar();
        setJMenuBar(menuPrincipal);

        JMenu menuProfil = new JMenu("Profil");
        menuDeconnexion = new JMenuItem("Deconnexion");
        menuProfil.add(menuDeconnexion);
        menuPrincipal.add(menuProfil);

        DesactiveVuePrincipale();
    }

    public void setControler(Controleur c) {
        loginButton.addActionListener(c);
        menuDeconnexion.addActionListener(c);
        voirFacturesButton.addActionListener(c);
        payerFactureButton.addActionListener(c);
        tableFactures.addMouseListener(c);
        this.addWindowListener(c);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tableModelFactures = new JTableModelFactures(VESPAP.getInstance().getListeFacture());
        tableFactures = new JTable(tableModelFactures);

        tableModelFactureDetaillee = new JTableModelFactureDetaillee(new ArrayList<>());
        tableFactureDetaillee = new JTable(tableModelFactureDetaillee);
    }

    public void ActiveVuePrincipale()
    {
        voirFacturesButton.setEnabled(true);
        tableFactures.setEnabled(true);
        numClient.setEnabled(true);
        voirFacturesButton.setEnabled(true);
        payerFactureButton.setEnabled(true);
        menuDeconnexion.setEnabled(true);

        loginButton.setEnabled(false);
        username.setText("");
        password.setText("");
    }

    public void DesactiveVuePrincipale()
    {
        voirFacturesButton.setEnabled(false);
        tableFactures.setEnabled(false);
        numClient.setEnabled(false);
        voirFacturesButton.setEnabled(false);
        payerFactureButton.setEnabled(false);
        menuDeconnexion.setEnabled(false);

        loginButton.setEnabled(true);
    }
}
