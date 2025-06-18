package ui;

import app.AppManager;
import service.TareaService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import validacion.ValidacionException;

import ui.KanbanPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TareaPanel extends JPanel {

    private final AppManager manager;
    private final TareaService service;

    private final JTable tabla;
    private final DefaultTableModel modelo;


    public TareaPanel(AppManager manager, TareaService service) {
        this.manager  = manager;
        this.service  = service;

        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Título", "Horas Est.", "Horas Reales", "Estado"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        BotoneraPanel botones = new BotoneraPanel(
                "Agregar", "Eliminar", "Tablero",
                e -> abrirFormulario(null),
                e -> eliminarSeleccionada(),
                e -> manager.mostrar(new KanbanPanel(manager, service))
        );
        add(botones, BorderLayout.SOUTH);

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tabla.getSelectedRow();
                    if (fila != -1) {
                        int id = (int) modelo.getValueAt(fila, 0);
                        try {
                            abrirFormulario(service.consulta(id));
                        } catch (ServiceException se) {
                            mostrarError("No se pudo cargar la tarea.");
                        }
                    }
                }
            }
        });

        refrescarTabla();
    }

    private void mostrarWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------------------------------------------------------------------
    //  Listado
    // ---------------------------------------------------------------------
    private void refrescarTabla() {
        new SwingWorker<List<model.Tarea>, Void>() {
            @Override protected List<model.Tarea> doInBackground() {
                try {
                    return service.listado();
                } catch (ServiceException se) {
                    // Propagamos para que done() lo capture
                    throw new RuntimeException(se);
                }
            }
            @Override protected void done() {
                try {
                    List<model.Tarea> tareas = get();
                    modelo.setRowCount(0);
                    for (model.Tarea t : tareas) {
                        modelo.addRow(new Object[]{
                                t.getId(), t.getTitulo(),
                                t.getHorasEstimadas(), t.getHorasReales(),
                                t.getEstado()
                        });
                    }
                } catch (Exception ex) {
                    mostrarError("No se pudo listar las tareas.");
                }
            }
        }.execute();
    }


    private void eliminarSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarWarn("Seleccioná una tarea para eliminar.");
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que querés eliminar esta tarea?",
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            int id = (int) modelo.getValueAt(fila, 0);
            try {
                service.baja(id);
                refrescarTabla();
                mostrarInfo("Tarea eliminada correctamente.");
            } catch (ServiceException se) {
                mostrarError("No pudimos eliminar la tarea.");
            }
        }
    }

    private void abrirFormulario(model.Tarea existente) {
        JTextField tituloTxt = new JTextField();
        JTextField descTxt   = new JTextField();
        JTextField estTxt    = new JTextField();
        JTextField realTxt   = new JTextField();
        JTextField inicioTxt = new JTextField();
        JTextField finTxt    = new JTextField();
        JComboBox<model.EstadoTarea> estadoBox = new JComboBox<>(model.EstadoTarea.values());

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
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Título:"));          form.add(tituloTxt);
        form.add(new JLabel("Descripción:"));     form.add(descTxt);
        form.add(new JLabel("Horas Estimadas:")); form.add(estTxt);
        form.add(new JLabel("Horas Reales:"));    form.add(realTxt);
        form.add(new JLabel("Inicio Sprint:"));   form.add(inicioTxt);
        form.add(new JLabel("Fin Sprint:"));      form.add(finTxt);
        form.add(new JLabel("Estado:"));          form.add(estadoBox);

        int res = JOptionPane.showConfirmDialog(
                this, form,
                existente == null ? "Agregar tarea" : "Editar tarea",
                JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String titulo = tituloTxt.getText();
                String desc   = descTxt.getText();
                int est       = Integer.parseInt(estTxt.getText());
                int real      = Integer.parseInt(realTxt.getText());
                java.time.LocalDate inicio = inicioTxt.getText().isBlank() ? null : java.time.LocalDate.parse(inicioTxt.getText());
                java.time.LocalDate fin    = finTxt.getText().isBlank() ? null : java.time.LocalDate.parse(finTxt.getText());
                model.EstadoTarea estado   = (model.EstadoTarea) estadoBox.getSelectedItem();

                if (existente == null) {
                    service.alta(titulo, desc, est, real, inicio, fin, estado);
                } else {
                    service.modificar(existente.getId(), titulo, desc, est, real, inicio, fin, estado);
                }
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                mostrarWarn("Las horas deben ser números enteros.");

            } catch (ValidacionException ve) {
                mostrarWarn(ve.getMessage());

            } catch (ServiceException se) {
                mostrarError("No pudimos guardar la tarea. Probá de nuevo.");
            }
        }
    }
}
