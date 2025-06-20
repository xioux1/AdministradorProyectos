package ui;

import app.AppManager;
import service.TareaService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import ui.form.AbstractFormPanel;
import ui.form.CamposTareaPanel;
import ui.form.CamposPanel;
import validacion.ValidacionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TareaPanel extends AbstractCrudPanel<model.Tarea> {

    private final TareaService service;

    public TareaPanel(AppManager manager, TareaService service) {
        super(manager, new String[]{"ID", "Título", "Horas Est.", "Horas Reales", "Estado", "Proyecto", "Empleado", "Costo"});
        this.service  = service;

        BotoneraPanel botones = new BotoneraPanel(
                "Agregar", "Eliminar", "Tablero",
                e -> abrirFormulario(null),
                e -> eliminarSeleccionada(),
                e -> manager.mostrar(new KanbanPanel(manager, service))
        );
        JPanel south = new JPanel(new BorderLayout());
        south.add(botones, BorderLayout.CENTER);
        JButton volver = new JButton("Volver");
        volver.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                manager.mostrar(manager.getMenuPanel());
            }
        });
        south.add(volver, BorderLayout.EAST);
        add(south, BorderLayout.SOUTH);

        // el doble clic para editar queda a cargo de AbstractCrudPanel

        refrescarTabla();
    }

    @Override
    protected List<model.Tarea> obtenerDatos() throws Exception {
        return service.listado();
    }

    @Override
    protected Object[] transformarFila(model.Tarea t) {
        return new Object[]{
                t.getId(), t.getTitulo(),
                t.getHorasEstimadas(), t.getHorasReales(),
                t.getEstado(),
                t.getProyecto().getId(), t.getEmpleado().getId(), t.getCostoHora()
        };
    }

    @Override
    protected int idDeFila(int fila) {
        return (int) modelo.getValueAt(fila,0);
    }

    @Override
    protected model.Tarea buscar(int id) throws Exception {
        return service.consulta(id);
    }

    @Override
    protected void eliminar(int id) throws Exception {
        service.baja(id);
    }


    @Override
    protected void abrirFormulario(model.Tarea existente) {
        manager.mostrar(new TareaForm(existente));
    }

    private class TareaForm extends AbstractFormPanel {
        private final model.Tarea existente;
        private CamposTareaPanel campos;

        TareaForm(model.Tarea existente) {
            super(TareaPanel.this.manager, TareaPanel.this);
            this.existente = existente;
        }

        @Override
        protected CamposPanel setCamposPanel() {
            java.util.List<model.HistorialEstado> hist = null;
            if (existente != null) {
                try { hist = service.historial(existente.getId()); }
                catch(ServiceException ignore) {}
            }
            campos = new CamposTareaPanel(existente, hist);
            return campos;
        }

        @Override
        protected void onOk() {
            try {
                String titulo = campos.getTitulo();
                String desc   = campos.getDescripcion();
                int est       = campos.getHorasEstimadas();
                int real      = campos.getHorasReales();
                java.time.LocalDate inicio = campos.getInicioSprint();
                java.time.LocalDate fin    = campos.getFinSprint();
                model.EstadoTarea estado   = campos.getEstado();
                int proyecto  = campos.getProyectoId();
                int empleado  = campos.getEmpleadoId();
                if (existente == null) {
                    service.alta(titulo, desc, est, real, inicio, fin, estado, proyecto, empleado);
                } else {
                    service.modificar(existente.getId(), titulo, desc, est, real,
                                      inicio, fin, estado, proyecto, empleado);
                }
                refrescarTabla();
                manager.mostrar(TareaPanel.this);
            } catch (NumberFormatException nfe) {
                Dialogs.warn(this,"Las horas deben ser números enteros.");
            } catch (ValidacionException ve) {
                Dialogs.warn(this, ve.getMessage());
            } catch (ServiceException se) {
                Dialogs.error(this,"No pudimos guardar la tarea. Probá de nuevo.");
            }
        }

        @Override
        protected void onCancel() {
            manager.mostrar(TareaPanel.this);
        }
    }
}
