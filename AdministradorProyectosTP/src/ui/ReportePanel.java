package ui;

import app.AppManager;
import service.ReporteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import ui.Dialogs;

public class ReportePanel extends JPanel {
    public ReportePanel(AppManager manager, ReporteService service) {
        setLayout(new BorderLayout(10,10));
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Proyecto","Horas","Costo"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        JTable tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        JButton volver = new JButton("Volver");
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.mostrar(manager.getMenuPanel());
            }
        });
        add(volver, BorderLayout.SOUTH);
        try{
            List<service.ReporteService.CostoProyecto> datos = service.resumenCostos();
            for(service.ReporteService.CostoProyecto c:datos){
                modelo.addRow(new Object[]{c.proyectoId,c.horas,c.costo});
            }
        }catch(Exception ex){
            Dialogs.error(ReportePanel.this,"No se pudo cargar");
        }
    }
}
