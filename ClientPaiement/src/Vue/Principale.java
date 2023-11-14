package Vue;

import javax.swing.*;

public class Principale extends JFrame{
    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField password;
    private JTextField username;
    private JTextField visa;
    private JButton voirFacturesButton;
    private JButton payerFactureButton;
    private JTable table1;

    public void Principale()
    {
        setContentPane(panel1);
        setSize(900, 530);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JMenuBar menuPrincipal = new JMenuBar();
        setJMenuBar(menuPrincipal);
    }
}
