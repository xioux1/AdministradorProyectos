package ui.form;

import ui.componentes.FormBuilder;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class CamposTareaPanel extends CamposPanel {
    private final JTextField tituloTxt = new JTextField();
    private final JTextField descTxt = new JTextField();
    private final JTextField estTxt = new JTextField();
    private final JTextField realTxt = new JTextField();
    private final JTextField inicioTxt = new JTextField();
    private final JTextField finTxt = new JTextField();
    private final JComboBox<model.EstadoTarea> estadoBox = new JComboBox<>(model.EstadoTarea.values());
    private final JComboBox<model.Proyecto> proyectoBox = new JComboBox<>();
    private final JComboBox<model.Empleado> empleadoBox = new JComboBox<>();
    private final JTextArea histArea = new JTextArea();

    public CamposTareaPanel(model.Tarea existente, List<model.HistorialEstado> historial,
                            List<model.Proyecto> proyectos, List<model.Empleado> empleados) {
        histArea.setEditable(false);
        FormBuilder b = new FormBuilder()
                .add("Título:", tituloTxt)
                .add("Descripción:", descTxt)
                .add("Horas Estimadas:", estTxt)
                .add("Horas Reales:", realTxt)
                .add("Inicio Sprint (AAAA-MM-DD):", inicioTxt)
                .add("Fin Sprint (AAAA-MM-DD):", finTxt)
                .add("Proyecto:", proyectoBox)
                .add("Empleado:", empleadoBox)
                .add("Estado:", estadoBox)
                .add("Historial:", new JScrollPane(histArea));
        setLayout(new BorderLayout());
        add(b.build(), BorderLayout.CENTER);
        if (proyectos != null) {
            for (model.Proyecto p : proyectos) proyectoBox.addItem(p);
        }
        if (empleados != null) {
            empleadoBox.addItem(null); // opción sin asignar
            for (model.Empleado e : empleados) empleadoBox.addItem(e);
            empleadoBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                      boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setText(value == null ? "" : value.toString());
                    return this;
                }
            });
        }
        if (existente != null) {
            tituloTxt.setText(existente.getTitulo());
            descTxt.setText(existente.getDescripcion());
            estTxt.setText(String.valueOf(existente.getHorasEstimadas()));
            realTxt.setText(String.valueOf(existente.getHorasReales()));
            if (existente.getInicioSprint() != null)
                inicioTxt.setText(existente.getInicioSprint().toString());
            if (existente.getFinSprint() != null)
                finTxt.setText(existente.getFinSprint().toString());
            if (existente.getEstado() != null)
                estadoBox.setSelectedItem(existente.getEstado());
            proyectoBox.setSelectedItem(existente.getProyecto());
            if (existente.getEmpleado() != null)
                empleadoBox.setSelectedItem(existente.getEmpleado());
        }
        if (historial != null) {
            for (model.HistorialEstado h : historial) {
                histArea.append(h.getFecha() + " - " + h.getEstado() + " - " + h.getResponsable() + "\n");
            }
        }
    }

    public CamposTareaPanel(List<model.Proyecto> proyectos, List<model.Empleado> empleados) {
        this(null, null, proyectos, empleados);
    }

    public String getTitulo() { return tituloTxt.getText(); }
    public String getDescripcion() { return descTxt.getText(); }
    public int getHorasEstimadas() { return Integer.parseInt(estTxt.getText()); }
    public int getHorasReales() { return Integer.parseInt(realTxt.getText()); }
    public LocalDate getInicioSprint() {
        return inicioTxt.getText().isBlank() ? null : LocalDate.parse(inicioTxt.getText());
    }
    public LocalDate getFinSprint() {
        return finTxt.getText().isBlank() ? null : LocalDate.parse(finTxt.getText());
    }
    public model.EstadoTarea getEstado() { return (model.EstadoTarea) estadoBox.getSelectedItem(); }
    public model.Proyecto getProyecto() { return (model.Proyecto) proyectoBox.getSelectedItem(); }
    public model.Empleado getEmpleado() { return (model.Empleado) empleadoBox.getSelectedItem(); }
}
