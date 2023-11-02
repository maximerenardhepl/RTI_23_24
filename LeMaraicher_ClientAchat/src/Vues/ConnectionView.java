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

    public JButton getBtnConnexion() { return btnConnexion; }
    public JTextField getTxtFieldLogin() { return txtFieldLogin; }
    public JPasswordField getTxtFieldPassword() { return passwordField; }

    public JLabel getLabelNotRegister() { return labelNotRegister; }

    public ConnectionView() {
        setContentPane(mainPanel);
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void setControler(Controler c) {
        btnConnexion.addActionListener(c);
        labelNotRegister.addMouseListener(c);
        labelNotRegister.addMouseMotionListener(c);
        this.addWindowListener(c);
    }
}
