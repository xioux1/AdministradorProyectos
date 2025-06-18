package main;                     

import app.AppManager;            
import dao.TareaDAO;
import dao.jdbc.JdbcTareaDAO;
import service.TareaService;
import service.TareaServiceImpl;
import ui.TareaPanel;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {           
    public static void main(String[] args) throws Exception {
        Connection c = DriverManager.getConnection(
                "jdbc:h2:file:./tareas", "sa", "");

        TareaDAO dao      = new JdbcTareaDAO(c);
        TareaService svc  = new TareaServiceImpl(dao);
        AppManager  mgr   = new AppManager();

        SwingUtilities.invokeLater(() ->
            mgr.mostrar(new TareaPanel(mgr, svc))
        );
        mgr.start();
    }
}
