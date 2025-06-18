package app;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class AppManager {
    private final JFrame frame = new JFrame("Administrador de Proyectos");
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
}