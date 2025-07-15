package app;

import javax.swing.JFrame;
import javax.swing.JPanel;

// Import only the classes that are used to avoid wildcard imports and
// improve readability.
import service.TareaService;
import service.ProyectoService;
import service.EmpleadoService;
import service.AsignacionService;
import service.ReporteService;

import ui.TareaPanel;
import ui.ProyectoPanel;
import ui.EmpleadoPanel;
import ui.AsignacionPanel;
import ui.ReportePanel;
import ui.MenuPanel;

public final class AppManager {
    private final JFrame frame = new JFrame("Administrador de Proyectos");
    private TareaPanel tareaPanel;
    private ProyectoPanel proyectoPanel;
    private EmpleadoPanel empleadoPanel;
    private AsignacionPanel asignacionPanel;
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

    public void initPanels(TareaService ts, ProyectoService ps,
                           EmpleadoService es, AsignacionService as,
                           ReporteService rs) {
        tareaPanel = new TareaPanel(this, ts);
        proyectoPanel = new ProyectoPanel(this, ps);
        empleadoPanel = new EmpleadoPanel(this, es);
        asignacionPanel = new AsignacionPanel(this, as, ps);
        reportePanel = new ReportePanel(this, rs);
        menuPanel = new MenuPanel(this);
    }

    public JPanel getTareaPanel(){return tareaPanel;}
    public JPanel getProyectoPanel(){return proyectoPanel;}
    public JPanel getEmpleadoPanel(){return empleadoPanel;}
    public AsignacionPanel getAsignacionPanel(){return asignacionPanel;}
    public JPanel getReportePanel(){return reportePanel;}
    public JPanel getMenuPanel(){return menuPanel;}
}
