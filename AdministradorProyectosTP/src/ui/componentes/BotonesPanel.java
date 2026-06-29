package ui.componentes;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

public class BotonesPanel extends JPanel {

    public BotonesPanel(LayoutManager layout, String[] textos, ActionListener[] listeners) {
        setLayout(layout);
        for (int i = 0; i < textos.length; i++) {
            JButton boton = new JButton(textos[i]);
            boton.addActionListener(listeners[i]);
            add(boton);
        }
    }

    public BotonesPanel(String[] textos, ActionListener[] listeners) {
        this(new FlowLayout(), textos, listeners);
    }
}
