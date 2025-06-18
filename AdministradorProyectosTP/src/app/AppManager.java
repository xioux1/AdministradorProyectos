package app;

import javax.swing.JFrame;
import javax.swing.JPanel;

import service.*;
import ui.*;

public final class AppManager {
    private final JFrame frame = new JFrame("Administrador de Proyectos");
    private TareaPanel tareaPanel;
    private ProyectoPanel proyectoPanel;
    private EmpleadoPanel empleadoPanel;
    private ReportePanel reportePanel;
    private MenuPanel menuPanel;

    public AppManager() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLocationRelativeTo(null);
    }
    public void mostrar(JPanel pnl) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(pnl);
        frame.validate(); frame.repaint();
    }
    public void start() { frame.setVisible(true); }

    public void initPanels(TareaService ts, ProyectoService ps, EmpleadoService es, ReporteService rs) {
        tareaPanel = new TareaPanel(this, ts);
        proyectoPanel = new ProyectoPanel(this, ps);
        empleadoPanel = new EmpleadoPanel(this, es);
        reportePanel = new ReportePanel(this, rs);
        menuPanel = new MenuPanel(this);
    }

    public JPanel getTareaPanel(){return tareaPanel;}
    public JPanel getProyectoPanel(){return proyectoPanel;}
    public JPanel getEmpleadoPanel(){return empleadoPanel;}
    public JPanel getReportePanel(){return reportePanel;}
    public JPanel getMenuPanel(){return menuPanel;}
}