package ui;

import app.AppManager;
import service.ReporteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportePanel extends JPanel {
    public ReportePanel(AppManager manager, ReporteService service) {
        setLayout(new BorderLayout(10,10));
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Proyecto","Horas","Costo"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JButton volver = new JButton("Volver");
        volver.addActionListener(e->manager.mostrar(new MenuPanel(manager)));
        add(volver, BorderLayout.SOUTH);

        new SwingWorker<List<service.ReporteService.CostoProyecto>,Void>(){
            @Override protected List<service.ReporteService.CostoProyecto> doInBackground() throws Exception {
                return service.resumenCostos();
            }
            @Override protected void done(){
                try{List<service.ReporteService.CostoProyecto> datos=get();
                    for(service.ReporteService.CostoProyecto c:datos){
                        modelo.addRow(new Object[]{c.proyectoId,c.horas,c.costo});
                    }
                }catch(Exception ex){JOptionPane.showMessageDialog(ReportePanel.this,"No se pudo cargar","Error",JOptionPane.ERROR_MESSAGE);} }
        }.execute();
    }
}
