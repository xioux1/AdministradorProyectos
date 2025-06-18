package main;                     

import app.AppManager;            
import dao.TareaDAO;
import dao.ProyectoDAO;
import dao.EmpleadoDAO;
import dao.jdbc.JdbcTareaDAO;
import dao.jdbc.JdbcProyectoDAO;
import dao.jdbc.JdbcEmpleadoDAO;
import service.TareaService;
import service.ProyectoService;
import service.EmpleadoService;
import service.TareaServiceImpl;
import service.ProyectoServiceImpl;
import service.EmpleadoServiceImpl;
import ui.TareaPanel;
import ui.ProyectoPanel;
import ui.EmpleadoPanel;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {           
    public static void main(String[] args) throws Exception {
        Connection c = DriverManager.getConnection(
                "jdbc:h2:file:./tareas", "sa", "");

        TareaDAO tareaDao      = new JdbcTareaDAO(c);
        ProyectoDAO proyectoDao = new JdbcProyectoDAO(c);
        EmpleadoDAO empleadoDao = new JdbcEmpleadoDAO(c);

        TareaService tareaSvc      = new TareaServiceImpl(tareaDao);
        ProyectoService projSvc    = new ProyectoServiceImpl(proyectoDao);
        EmpleadoService empSvc     = new EmpleadoServiceImpl(empleadoDao);

        AppManager  mgr   = new AppManager();

        SwingUtilities.invokeLater(() ->
            mgr.mostrar(new TareaPanel(mgr, tareaSvc))
        );
        mgr.start();
    }
}
