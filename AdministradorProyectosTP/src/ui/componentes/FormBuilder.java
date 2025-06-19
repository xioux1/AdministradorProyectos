package ui.componentes;

import javax.swing.*;
import java.awt.*;

public class FormBuilder {
    private final JPanel panel;

    public FormBuilder() {
        panel = new JPanel(new GridLayout(0, 2, 5, 5));
    }

    public FormBuilder add(String label, JComponent field) {
        panel.add(new JLabel(label));
        panel.add(field);
        return this;
    }

    public JPanel build() {
        return panel;
    }
}
