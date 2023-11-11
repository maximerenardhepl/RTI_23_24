package Vues;

import javax.swing.*;

public class LoadingConnection extends JFrame {

    private JPanel panelChargement;
    private JButton btnReessayer;
    private JButton btnAbandonner;
    private JPanel panelBoutons;
    private JTextPane txtPaneMsg;

    public LoadingConnection() {
        setContentPane(panelChargement);
        setSize(450, 300);
        panelBoutons.setVisible(false);
    }

    public JPanel getPanelBoutons() { return panelBoutons; }
    public JPanel getPanelChargement() { return panelChargement; }

    public JTextPane getTextPaneMsg() {
        return txtPaneMsg;
    }

    public JButton getBtnReessayer() { return btnReessayer; }

    public JButton getBtnAbandonner() { return btnAbandonner; }
}
