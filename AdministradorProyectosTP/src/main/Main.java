package main;                     

import app.AppManager;            
import dao.TareaDAO;
import dao.ProyectoDAO;
import dao.EmpleadoDAO;
import dao.jdbc.*;
import dao.HistorialDAO;
import dao.AsignacionDAO;
import service.TareaService;
import service.ProyectoService;
import service.EmpleadoService;
import service.*;
import ui.*;

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
        HistorialDAO histDao    = new JdbcHistorialDAO(c);
        AsignacionDAO asigDao   = new JdbcAsignacionDAO(c);

        TareaService tareaSvc      = new TareaServiceImpl(tareaDao, histDao);
        ProyectoService projSvc    = new ProyectoServiceImpl(proyectoDao);
        EmpleadoService empSvc     = new EmpleadoServiceImpl(empleadoDao);
        AsignacionService asigSvc  = new AsignacionServiceImpl(asigDao, empleadoDao);
        ReporteService repSvc      = new ReporteServiceImpl(tareaDao);

        AppManager  mgr   = new AppManager();
        mgr.initPanels(tareaSvc, projSvc, empSvc, asigSvc, repSvc);

        SwingUtilities.invokeLater(() ->
            mgr.mostrar(mgr.getMenuPanel())
        );
        mgr.start();
    }
}
