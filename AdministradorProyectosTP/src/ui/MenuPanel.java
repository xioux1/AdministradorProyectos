package ui;

import app.AppManager;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(AppManager manager) {
        setLayout(new GridLayout(0,1,10,10));
        JButton tareas = new JButton("Tareas");
        JButton proyectos = new JButton("Proyectos");
        JButton empleados = new JButton("Empleados");
        JButton reportes = new JButton("Reportes");
        JButton asignaciones = new JButton("Asignaciones");
        tareas.addActionListener(e->manager.mostrar(manager.getTareaPanel()));
        proyectos.addActionListener(e->manager.mostrar(manager.getProyectoPanel()));
        empleados.addActionListener(e->manager.mostrar(manager.getEmpleadoPanel()));
        reportes.addActionListener(e->manager.mostrar(manager.getReportePanel()));
        asignaciones.addActionListener(e->manager.mostrar(manager.getAsignacionPanel()));
        add(tareas);add(proyectos);add(empleados);add(asignaciones);add(reportes);
    }
}
