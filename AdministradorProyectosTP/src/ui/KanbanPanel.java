package ui;

import app.AppManager;
import service.TareaService;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import ui.Dialogs;

public class KanbanPanel extends JPanel {
    private final AppManager manager;
    private final TareaService service;
    private final DefaultListModel<model.Tarea> backlogModel = new DefaultListModel<>();
    private final DefaultListModel<model.Tarea> progresoModel = new DefaultListModel<>();
    private final DefaultListModel<model.Tarea> doneModel = new DefaultListModel<>();

    public KanbanPanel(AppManager manager, TareaService service) {
        this.manager = manager;
        this.service = service;
        setLayout(new BorderLayout(10,10));

        JPanel board = new JPanel(new GridLayout(1,3,10,10));
        board.add(createColumn("Backlog", backlogModel));
        board.add(createColumn("En Progreso", progresoModel));
        board.add(createColumn("Terminada", doneModel));
        add(board, BorderLayout.CENTER);

        JButton volver = new JButton("Volver");
        volver.addActionListener(e -> manager.mostrar(new TareaPanel(manager, service)));
        add(volver, BorderLayout.SOUTH);

        cargarDatos();
    }

    private JScrollPane createColumn(String titulo, DefaultListModel<model.Tarea> model) {
        JList<model.Tarea> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    model.Tarea t = list.getSelectedValue();
                    if (t != null) moverTarea(t);
                }
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return new JScrollPane(panel);
    }

    private void moverTarea(model.Tarea t) {
        model.EstadoTarea nuevo;
        if (t.getEstado() == model.EstadoTarea.BACKLOG) nuevo = model.EstadoTarea.EN_PROGRESO;
        else if (t.getEstado() == model.EstadoTarea.EN_PROGRESO) nuevo = model.EstadoTarea.TERMINADA;
        else return;
        try {
            service.cambiarEstado(t.getId(), nuevo);
            cargarDatos();
        } catch (ServiceException ex) {
            Dialogs.error(this, "No se pudo mover la tarea");
        }
    }

    private void cargarDatos() {
        backlogModel.clear();
        progresoModel.clear();
        doneModel.clear();
        try {
            List<model.Tarea> tareas = service.listado();
            for (model.Tarea t : tareas) {
                if (t.getEstado() == model.EstadoTarea.BACKLOG) backlogModel.addElement(t);
                else if (t.getEstado() == model.EstadoTarea.EN_PROGRESO) progresoModel.addElement(t);
                else doneModel.addElement(t);
            }
        } catch (ServiceException ex) {
            Dialogs.error(this, "No se pudo cargar el tablero");
        }
    }
}
