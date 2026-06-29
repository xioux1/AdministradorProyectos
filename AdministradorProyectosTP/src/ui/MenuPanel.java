package ui;

import app.AppManager;
import ui.componentes.BotonesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    public MenuPanel(AppManager manager) {
        setLayout(new BorderLayout());

        String[] textos = {"Tareas", "Proyectos", "Empleados", "Asignaciones", "Reportes"};
        ActionListener[] listeners = {
            e -> manager.mostrar(manager.getTareaPanel()),
            e -> manager.mostrar(manager.getProyectoPanel()),
            e -> manager.mostrar(manager.getEmpleadoPanel()),
            e -> { manager.getAsignacionPanel().refrescar(); manager.mostrar(manager.getAsignacionPanel()); },
            e -> manager.mostrar(manager.getReportePanel())
        };

        add(new BotonesPanel(new GridLayout(0,1,10,10), textos, listeners), BorderLayout.CENTER);
    }
}
