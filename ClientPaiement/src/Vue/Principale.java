package Vue;

import javax.swing.*;
import Controleur.Controleur;

public class Principale extends JFrame{
    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField password;
    private JTextField username;
    private JTextField visa;
    private JButton voirFacturesButton;
    private JButton payerFactureButton;
    private JTable tableFactures;
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

        DesactiveVuePrincipale();

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

    public void ActiveVuePrincipale()
    {
        voirFacturesButton.setEnabled(true);
        tableFactures.setEnabled(true);
        visa.setEnabled(true);
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
        visa.setEnabled(false);
        voirFacturesButton.setEnabled(false);
        payerFactureButton.setEnabled(false);
        menuDeconnexion.setEnabled(false);
    }
}
