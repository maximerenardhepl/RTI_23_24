package Vues;

import Controleur.Controler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ConnectionView extends JFrame {
    private JTextField txtFieldLogin;
    private JButton btnConnexion;
    private JPasswordField passwordField;
    private JPanel mainPanel;
    private JLabel labelNotRegister;
    private LoadingConnection vueChargement;


    public JButton getBtnConnexion() { return btnConnexion; }
    public JTextField getTxtFieldLogin() { return txtFieldLogin; }
    public JPasswordField getTxtFieldPassword() { return passwordField; }

    public JLabel getLabelNotRegister() { return labelNotRegister; }

    public LoadingConnection getVueChargement() {
        return vueChargement;
    }

    public ConnectionView() {
        setContentPane(mainPanel);
        Dimension d = new Dimension(450, 350);
        setMinimumSize(d);
        setSize(d);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        vueChargement = new LoadingConnection();
    }

    public void setControler(Controler c) {
        btnConnexion.addActionListener(c);
        labelNotRegister.addMouseListener(c);
        labelNotRegister.addMouseMotionListener(c);
        this.addWindowListener(c);
    }

    public void afficheChargement()
    {
        setContentPane(vueChargement.getPanelChargement());
        setVisible(true);
    }

    public void affichePanelConnexion()
    {
        setContentPane(mainPanel);
        setVisible(true);
    }
}
