package ui;

import app.AppManager;
import service.TareaService;
import service.ServiceException;
import ui.componentes.BotoneraPanel;
import ui.componentes.FormBuilder;
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
        volver.addActionListener(e -> manager.mostrar(manager.getMenuPanel()));
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
        JTextField tituloTxt = new JTextField();
        JTextField descTxt   = new JTextField();
        JTextField estTxt    = new JTextField();
        JTextField realTxt   = new JTextField();

        JTextField inicioTxt = new JTextField();
        JTextField finTxt    = new JTextField();
        JComboBox<model.EstadoTarea> estadoBox = new JComboBox<>(model.EstadoTarea.values());

        JTextField proyectoTxt = new JTextField();
        JTextField empleadoTxt = new JTextField();
        JTextArea histArea = new JTextArea();
        histArea.setEditable(false);

        if (existente != null) {
            tituloTxt.setText(existente.getTitulo());
            descTxt.setText(existente.getDescripcion());
            estTxt.setText(String.valueOf(existente.getHorasEstimadas()));
            realTxt.setText(String.valueOf(existente.getHorasReales()));
            if (existente.getInicioSprint() != null)
                inicioTxt.setText(existente.getInicioSprint().toString());
            if (existente.getFinSprint() != null)
                finTxt.setText(existente.getFinSprint().toString());
            if (existente.getEstado() != null)
                estadoBox.setSelectedItem(existente.getEstado());

            proyectoTxt.setText(String.valueOf(existente.getProyecto().getId()));
            empleadoTxt.setText(String.valueOf(existente.getEmpleado().getId()));
            try {
                java.util.List<model.HistorialEstado> hs = service.historial(existente.getId());
                for(model.HistorialEstado h: hs){
                    histArea.append(h.getFecha()+" - "+h.getEstado()+" - "+h.getResponsable()+"\n");
                }
            } catch(ServiceException ignore) {}
        }

        JPanel form = new FormBuilder()
                .add("Título:", tituloTxt)
                .add("Descripción:", descTxt)
                .add("Horas Estimadas:", estTxt)
                .add("Horas Reales:", realTxt)
                .add("Inicio Sprint:", inicioTxt)
                .add("Fin Sprint:", finTxt)
                .add("Proyecto ID:", proyectoTxt)
                .add("Empleado ID:", empleadoTxt)
                .add("Estado:", estadoBox)
                .add("Historial:", new JScrollPane(histArea))
                .build();

        int res = JOptionPane.showConfirmDialog(
                this, form,
                existente == null ? "Agregar tarea" : "Editar tarea",
                JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String titulo = tituloTxt.getText();
                String desc   = descTxt.getText();
                int est       = Integer.parseInt(estTxt.getText());
                int real      = Integer.parseInt(realTxt.getText());
                java.time.LocalDate inicio = inicioTxt.getText().isBlank() ? null : java.time.LocalDate.parse(inicioTxt.getText());
                java.time.LocalDate fin    = finTxt.getText().isBlank() ? null : java.time.LocalDate.parse(finTxt.getText());
                model.EstadoTarea estado   = (model.EstadoTarea) estadoBox.getSelectedItem();

                int proyecto  = Integer.parseInt(proyectoTxt.getText());
                int empleado  = Integer.parseInt(empleadoTxt.getText());

                if (existente == null) {
                    service.alta(titulo, desc, est, real, inicio, fin, estado, proyecto, empleado);
                } else {
                    service.modificar(existente.getId(), titulo, desc, est, real,
                                      inicio, fin, estado, proyecto, empleado);
                }
                refrescarTabla();

            } catch (NumberFormatException nfe) {
                Dialogs.warn(this,"Las horas deben ser números enteros.");

            } catch (ValidacionException ve) {
                Dialogs.warn(this,ve.getMessage());

            } catch (ServiceException se) {
                Dialogs.error(this,"No pudimos guardar la tarea. Probá de nuevo.");
            }
        }
    }
}
