package ui;

import app.AppManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class AbstractCrudPanel<T> extends JPanel {
    protected final AppManager manager;
    protected final JTable tabla;
    protected final DefaultTableModel modelo;

    protected AbstractCrudPanel(AppManager manager, String[] columnas) {
        this.manager = manager;
        setLayout(new BorderLayout(10,10));
        modelo = new DefaultTableModel(columnas,0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        tabla.addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    int fila=tabla.getSelectedRow();
                    if(fila!=-1) onRowDoubleClick(fila);
                }
            }
        });
    }

    protected abstract List<T> obtenerDatos() throws Exception;
    protected abstract Object[] transformarFila(T t);
    protected abstract int idDeFila(int fila);
    protected abstract T buscar(int id) throws Exception;
    protected abstract void eliminar(int id) throws Exception;
    protected abstract void abrirFormulario(T existente);

    protected void onRowDoubleClick(int fila){
        int id = idDeFila(fila);
        try{ abrirFormulario( buscar(id) ); }
        catch(Exception e){ Dialogs.error(this,"No se pudo cargar."); }
    }

    protected void refrescarTabla(){
        new SwingWorker<List<T>,Void>(){
            @Override protected List<T> doInBackground(){
                try{return obtenerDatos();}catch(Exception e){throw new RuntimeException(e);} }
            @Override protected void done(){
                try{
                    List<T> datos=get();
                    modelo.setRowCount(0);
                    for(T t:datos) modelo.addRow(transformarFila(t));
                }catch(Exception e){Dialogs.error(AbstractCrudPanel.this,"No se pudo listar.");}
            }
        }.execute();
    }

    protected void eliminarSeleccionada(){
        int fila=tabla.getSelectedRow();
        if(fila==-1){Dialogs.warn(this,"Seleccioná un registro.");return;}
        if(JOptionPane.showConfirmDialog(this,"¿Seguro?","Confirmación",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            int id=idDeFila(fila);
            try{eliminar(id);refrescarTabla();Dialogs.info(this,"Eliminado.");}
            catch(Exception e){Dialogs.error(this,"No se pudo eliminar.");}
        }
    }
}
