package ui.form;

import ui.componentes.FormBuilder;
import javax.swing.*;
import java.awt.*;

public class CamposEmpleadoPanel extends CamposPanel {
    private final JTextField nombreTxt = new JTextField();
    private final JTextField costoTxt = new JTextField();

    public CamposEmpleadoPanel(model.Empleado existente) {
        FormBuilder b = new FormBuilder()
                .add("Nombre:", nombreTxt)
                .add("Costo Hora:", costoTxt);
        setLayout(new BorderLayout());
        add(b.build(), BorderLayout.CENTER);
        if (existente != null) {
            nombreTxt.setText(existente.getNombre());
            costoTxt.setText(String.valueOf(existente.getCostoHora()));
        }
    }

    public CamposEmpleadoPanel() {
        this(null);
    }

    public String getNombre() { return nombreTxt.getText(); }
    public int getCostoHora() { return Integer.parseInt(costoTxt.getText()); }
}
