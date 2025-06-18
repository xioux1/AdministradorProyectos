package ui;

import app.AppManager;
import service.EmpleadoService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import validacion.ValidacionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmpleadoPanel extends JPanel {

    private final AppManager manager;
    private final EmpleadoService service;

    private final JTable tabla;
    private final DefaultTableModel modelo;

    public EmpleadoPanel(AppManager manager, EmpleadoService service) {
        this.manager = manager;
        this.service = service;

        setLayout(new BorderLayout(10,10));

        modelo = new DefaultTableModel(new Object[]{"ID","Nombre","Costo Hora"},0){
            @Override public boolean isCellEditable(int r,int c){return false;}
        };

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        BotoneraPanel botones = new BotoneraPanel(
                "Agregar","Eliminar","Volver",
                e->abrirFormulario(null),
                e->eliminarSeleccionada(),
                e->manager.mostrar(manager.getMenuPanel())
        );
        add(botones, BorderLayout.SOUTH);

        tabla.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount()==2){
                    int fila=tabla.getSelectedRow();
                    if(fila!=-1){
                        int id=(int)modelo.getValueAt(fila,0);
                        try{abrirFormulario(service.consulta(id));}
                        catch(ServiceException se){mostrarError("No se pudo cargar.");}
                    }
                }
            }
        });

        refrescarTabla();
    }

    private void mostrarWarn(String m){JOptionPane.showMessageDialog(this,m,"Aviso",JOptionPane.WARNING_MESSAGE);}
    private void mostrarError(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
    private void mostrarInfo(String m){JOptionPane.showMessageDialog(this,m,"Info",JOptionPane.INFORMATION_MESSAGE);}

    private void refrescarTabla(){
        new SwingWorker<List<model.Empleado>,Void>(){
            @Override protected List<model.Empleado> doInBackground(){
                try{return service.listado();}catch(ServiceException se){throw new RuntimeException(se);} }
            @Override protected void done(){
                try{List<model.Empleado> ps=get();modelo.setRowCount(0);for(model.Empleado p:ps){modelo.addRow(new Object[]{p.getId(),p.getNombre(),p.getCostoHora()});}}
                catch(Exception ex){mostrarError("No se pudo listar.");}
            }
        }.execute();
    }

    private void eliminarSeleccionada(){
        int fila=tabla.getSelectedRow();
        if(fila==-1){mostrarWarn("Seleccioná un empleado.");return;}
        if(JOptionPane.showConfirmDialog(this,"¿Seguro?","Confirmación",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            int id=(int)modelo.getValueAt(fila,0);
            try{service.baja(id);refrescarTabla();mostrarInfo("Empleado eliminado.");}
            catch(ServiceException se){mostrarError("No se pudo eliminar.");}
        }
    }

    private void abrirFormulario(model.Empleado existente){
        JTextField nombreTxt=new JTextField();
        JTextField costoTxt=new JTextField();
        if(existente!=null){
            nombreTxt.setText(existente.getNombre());
            costoTxt.setText(String.valueOf(existente.getCostoHora()));
        }
        JPanel form=new JPanel(new GridLayout(0,2,5,5));
        form.add(new JLabel("Nombre:"));form.add(nombreTxt);
        form.add(new JLabel("Costo Hora:"));form.add(costoTxt);

        int res=JOptionPane.showConfirmDialog(this,form,existente==null?"Agregar empleado":"Editar empleado",JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.OK_OPTION){
            try{
                String nombre=nombreTxt.getText();
                int costo=Integer.parseInt(costoTxt.getText());
                if(existente==null){service.alta(nombre,costo);}else{service.modificar(existente.getId(),nombre,costo);}refrescarTabla();
            }catch(ValidacionException ve){mostrarWarn(ve.getMessage());}
            catch(ServiceException se){mostrarError("No se pudo guardar.");}
        }
    }
}
