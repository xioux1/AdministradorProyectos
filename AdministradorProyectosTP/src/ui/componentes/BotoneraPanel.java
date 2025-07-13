package ui.componentes;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

public class BotoneraPanel extends JPanel {

	public BotoneraPanel(String textoOk, String textoCancel, String textoVolver,
                         ActionListener onOk,
                         ActionListener onCancel,
                         ActionListener onVolver) {
        setLayout(new FlowLayout());

        JButton ok     = new JButton(textoOk);
        JButton cancel = new JButton(textoCancel);
        JButton volver = new JButton(textoVolver);

        ok.addActionListener(onOk);
        cancel.addActionListener(onCancel);
        volver.addActionListener(onVolver);

        add(ok); add(cancel); add(volver);
    }
}
