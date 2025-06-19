package ui;

import app.AppManager;
import service.EmpleadoService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import ui.componentes.FormBuilder;
import validacion.ValidacionException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmpleadoPanel extends AbstractCrudPanel<model.Empleado> {

    private final EmpleadoService service;

    public EmpleadoPanel(AppManager manager, EmpleadoService service) {
        super(manager, new String[]{"ID","Nombre","Costo Hora"});
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
    protected List<model.Empleado> obtenerDatos() throws Exception {
        return service.listado();
    }

    @Override
    protected Object[] transformarFila(model.Empleado e) {
        return new Object[]{e.getId(), e.getNombre(), e.getCostoHora()};
    }

    @Override
    protected int idDeFila(int fila) {
        return (int) modelo.getValueAt(fila,0);
    }

    @Override
    protected model.Empleado buscar(int id) throws Exception {
        return service.consulta(id);
    }

    @Override
    protected void eliminar(int id) throws Exception {
        service.baja(id);
    }

    @Override
    protected void abrirFormulario(model.Empleado existente){
        JTextField nombreTxt=new JTextField();
        JTextField costoTxt=new JTextField();
        if(existente!=null){
            nombreTxt.setText(existente.getNombre());
            costoTxt.setText(String.valueOf(existente.getCostoHora()));
        }
        JPanel form=new FormBuilder()
                .add("Nombre:", nombreTxt)
                .add("Costo Hora:", costoTxt)
                .build();

        int res=JOptionPane.showConfirmDialog(this,form,existente==null?"Agregar empleado":"Editar empleado",JOptionPane.OK_CANCEL_OPTION);
        if(res==JOptionPane.OK_OPTION){
            try{
                String nombre=nombreTxt.getText();
                int costo=Integer.parseInt(costoTxt.getText());
                if(existente==null){service.alta(nombre,costo);}else{service.modificar(existente.getId(),nombre,costo);}refrescarTabla();
            }catch(ValidacionException ve){Dialogs.warn(this,ve.getMessage());}
            catch(ServiceException se){Dialogs.error(this,"No se pudo guardar.");}
        }
    }
}
