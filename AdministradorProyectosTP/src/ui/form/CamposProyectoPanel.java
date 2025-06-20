package ui.form;

import ui.componentes.FormBuilder;
import javax.swing.*;
import java.awt.*;

public class CamposProyectoPanel extends CamposPanel {
    private final JTextField nombreTxt = new JTextField();

    public CamposProyectoPanel(model.Proyecto existente) {
        FormBuilder b = new FormBuilder()
                .add("Nombre:", nombreTxt);
        setLayout(new BorderLayout());
        add(b.build(), BorderLayout.CENTER);
        if (existente != null) {
            nombreTxt.setText(existente.getNombre());
        }
    }

    public CamposProyectoPanel() { this(null); }

    public String getNombre() { return nombreTxt.getText(); }
}
