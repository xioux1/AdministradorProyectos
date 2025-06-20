package ui;

import app.AppManager;
import service.EmpleadoService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import ui.form.AbstractFormPanel;
import ui.form.CamposEmpleadoPanel;
import ui.form.CamposPanel;
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
        manager.mostrar(new EmpleadoForm(existente));
    }

    private class EmpleadoForm extends AbstractFormPanel {
        private final model.Empleado existente;
        private CamposEmpleadoPanel campos;

        EmpleadoForm(model.Empleado existente){
            super(EmpleadoPanel.this.manager, EmpleadoPanel.this);
            this.existente = existente;
        }

        @Override
        protected CamposPanel setCamposPanel(){
            campos = new CamposEmpleadoPanel(existente);
            return campos;
        }

        @Override
        protected void onOk(){
            try{
                String nombre = campos.getNombre();
                int costo = campos.getCostoHora();
                if(existente==null){
                    service.alta(nombre, costo);
                }else{
                    service.modificar(existente.getId(), nombre, costo);
                }
                refrescarTabla();
                manager.mostrar(EmpleadoPanel.this);
            }catch(ValidacionException ve){
                Dialogs.warn(this, ve.getMessage());
            }catch(ServiceException se){
                Dialogs.error(this,"No se pudo guardar.");
            }
        }

        @Override
        protected void onCancel(){
            manager.mostrar(EmpleadoPanel.this);
        }
    }
}
