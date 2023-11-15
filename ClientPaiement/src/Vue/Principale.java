package Vue;

import javax.swing.*;
import Controleur.Controleur;
import Modele.VESPAP;
import Vue.JTableModel.JTableModelFactures;

public class Principale extends JFrame{
    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField password;
    private JTextField username;
    private JTextField visa;
    private JButton voirFacturesButton;
    private JButton payerFactureButton;
    private JTable tableFactures;
    private JTableModelFactures tableModelFactures;
    private JMenuItem menuDeconnexion;

    public String getUsername() { return username.getText(); }
    public String getPassword() { return String.valueOf(password.getPassword()); }

    public JMenuItem getMenuDeconnexion() { return menuDeconnexion; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getVoirFacturesButton() { return  voirFacturesButton; }
    public JButton getPayerFactureButton() { return payerFactureButton; }

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
    }

    public void setControler(Controleur c) {
        loginButton.addActionListener(c);
        voirFacturesButton.addActionListener(c);
        payerFactureButton.addActionListener(c);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tableModelFactures = new JTableModelFactures(VESPAP.getInstance().getListeFacture());
        tableFactures = new JTable(tableModelFactures);
    }
}
