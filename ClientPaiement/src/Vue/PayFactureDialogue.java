package Vue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PayFactureDialogue extends JDialog {


    private JTextField visa;
    private JTextField titulaire;
    private JButton confirmerPaiementButton;
    private JPanel dial;
    private boolean ok;

    public PayFactureDialogue(){
        setContentPane(dial);
        dial.setSize(300,400);

        ok = false;

        setModal(true);
        confirmerPaiementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = true;
                setVisible(false);
            }
        });
    }

    public boolean isOk()
    {
        return ok;
    }

    public String getVisa() {
        return visa.getText();
    }

    public String getTitulaire() {
        return titulaire.getText();
    }
}
