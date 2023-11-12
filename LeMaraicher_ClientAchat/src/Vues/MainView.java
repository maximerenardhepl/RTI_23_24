package Vues;

import Controleur.Controler;
import Modele.Ovesp;
import Vues.JTableModel.JTableModelPanier;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{

    private JMenuItem menuDeconnexion;
    private JButton btnPrecedent;
    private JButton btnSuivant;
    private JButton btnAcheter;
    private JTextField LabelArticle;
    private JTextField LabelPrix;
    private JTextField LabelStock;

    public int getSpinnerQteArticle() {
        return (int)(spinnerQteArticle.getModel().getValue());
    }

    private JSpinner spinnerQteArticle;
    private JTextPane bienvenueSurLeMaraicherTextPane;
    private JTable jTablePanier;
    private JTableModelPanier modeleTablePanier;
    private JButton supprimerArticleButton;
    private JButton viderLePanierButton;
    private JButton confirmerAchatButton;
    private JTextField textField4;
    private JPanel mainPanel;
    private JLabel labelImage;

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

    public JButton getBtnSupprimerArticle() {
        return supprimerArticleButton;
    }

    public JButton getBtnViderPanier() {
        return viderLePanierButton;
    }

    public JButton getBtnConfirmerAchat() {
        return confirmerAchatButton;
    }

    public JTable getTablePanier() {
        return jTablePanier;
    }

    public JTableModelPanier getModeleTablePanier() {
        return modeleTablePanier;
    }

    //methode pour remplir les champs
    public void SetLabelArticle(String newText) {LabelArticle.setText(newText);}

    //convertir avant de lui passer
    public void SetLabelPrix(String newText) {LabelPrix.setText(newText);}
    public void SetLabelStock(String newText) {LabelStock.setText(newText);}
    public JLabel getLabelImage() {return labelImage;}

    public void SetLabelImage(String PathImage)
    {
        String FullPath = "img/" + PathImage;

        ImageIcon image = new ImageIcon(FullPath);
        labelImage.setIcon(image);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        modeleTablePanier = new JTableModelPanier(Ovesp.getInstance().getPanier());
        jTablePanier = new JTable(modeleTablePanier);
    }
}
