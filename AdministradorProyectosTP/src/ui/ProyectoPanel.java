package ui;

import app.AppManager;
import service.ProyectoService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import ui.componentes.FormBuilder;
import validacion.ValidacionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProyectoPanel extends AbstractCrudPanel<model.Proyecto> {

    private final ProyectoService service;

    public ProyectoPanel(AppManager manager, ProyectoService service) {
        super(manager, new String[]{"ID","Nombre"});
        this.service = service;

        BotoneraPanel botones = new BotoneraPanel(
                "Agregar","Eliminar","Volver",
                e->abrirFormulario(null),
                e->eliminarSeleccionada(),
                e->manager.mostrar(manager.getMenuPanel())
        );
        add(botones, BorderLayout.SOUTH);

        refrescarTabla();
    }

    @Override
    protected List<model.Proyecto> obtenerDatos() throws Exception {
        return service.listado();
    }

    @Override
    protected Object[] transformarFila(model.Proyecto p) {
        return new Object[]{p.getId(), p.getNombre()};
    }

    @Override
    protected int idDeFila(int fila) {
        return (int) modelo.getValueAt(fila,0);
    }

    @Override
    protected model.Proyecto buscar(int id) throws Exception {
        return service.consulta(id);
    }

    @Override
    protected void eliminar(int id) throws Exception {
        service.baja(id);
    }

    @Override
    protected void abrirFormulario(model.Proyecto existente){
        JTextField nombreTxt=new JTextField();
        if(existente!=null){nombreTxt.setText(existente.getNombre());}
        JPanel form=new FormBuilder()
                .add("Nombre:", nombreTxt)
                .build();

        int res=JOptionPane.showConfirmDialog(this,form,existente==null?"Agregar proyecto":"Editar proyecto",JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.OK_OPTION){
            try{
                String nombre=nombreTxt.getText();
                if(existente==null){service.alta(nombre);}else{service.modificar(existente.getId(),nombre);}refrescarTabla();
            }catch(ValidacionException ve){Dialogs.warn(this,ve.getMessage());}
            catch(ServiceException se){Dialogs.error(this,"No se pudo guardar.");}
        }
    }
}
