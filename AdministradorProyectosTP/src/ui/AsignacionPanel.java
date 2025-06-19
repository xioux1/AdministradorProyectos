package ui;

import app.AppManager;
import service.AsignacionService;
import service.ProyectoService;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import ui.Dialogs;

public class AsignacionPanel extends JPanel {
    private final AppManager manager;
    private final AsignacionService service;
    private final ProyectoService proyectoService;

    private final JComboBox<model.Proyecto> proyectoBox = new JComboBox<>();
    private final DefaultListModel<model.Empleado> asignadosModel = new DefaultListModel<>();
    private final DefaultListModel<model.Empleado> libresModel = new DefaultListModel<>();
    private final JList<model.Empleado> asignadosList = new JList<>(asignadosModel);
    private final JList<model.Empleado> libresList = new JList<>(libresModel);

    public AsignacionPanel(AppManager manager, AsignacionService service, ProyectoService proyectoService) {
        this.manager = manager;
        this.service = service;
        this.proyectoService = proyectoService;

        setLayout(new BorderLayout(10,10));

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Proyecto:"), BorderLayout.WEST);
        top.add(proyectoBox, BorderLayout.CENTER);
        proyectoBox.addActionListener(e -> cargarListas());
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1,2,10,10));
        center.add(crearPanel("Asignados", asignadosList));
        center.add(crearPanel("Libres", libresList));
        add(center, BorderLayout.CENTER);

        JButton asignar = new JButton("Asignar");
        JButton desasignar = new JButton("Desasignar");
        JButton volver = new JButton("Volver");
        JPanel south = new JPanel(new FlowLayout());
        south.add(asignar); south.add(desasignar); south.add(volver);
        add(south, BorderLayout.SOUTH);

        asignar.addActionListener(e -> asignarEmpleado());
        desasignar.addActionListener(e -> desasignarEmpleado());
        volver.addActionListener(e -> manager.mostrar(manager.getMenuPanel()));

        cargarProyectos();
    }

    private JScrollPane crearPanel(String titulo, JList<model.Empleado> list) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(titulo, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        return new JScrollPane(p);
    }

    private void cargarProyectos() {
        new SwingWorker<List<model.Proyecto>,Void>(){
            @Override protected List<model.Proyecto> doInBackground(){
                try{return proyectoService.listado();}catch(ServiceException e){throw new RuntimeException(e);}}
            @Override protected void done(){
                try{
                    List<model.Proyecto> ps=get();
                    proyectoBox.removeAllItems();
                    for(model.Proyecto p:ps) proyectoBox.addItem(p);
                    if(proyectoBox.getItemCount()>0) proyectoBox.setSelectedIndex(0);
                    cargarListas();
                }catch(Exception ex){Dialogs.error(AsignacionPanel.this,"No se pudo cargar proyectos");}
            }
        }.execute();
    }

    private void cargarListas(){
        model.Proyecto p=(model.Proyecto) proyectoBox.getSelectedItem();
        if(p==null) return;
        new SwingWorker<Void,Void>(){
            List<model.Empleado> asignados;
            List<model.Empleado> libres;
            @Override protected Void doInBackground(){
                try{
                    asignados=service.empleadosDelProyecto(p.getId());
                    libres=service.empleadosLibres();
                }catch(ServiceException e){throw new RuntimeException(e);}return null;}
            @Override protected void done(){
                try{
                    get();
                    asignadosModel.clear();
                    libresModel.clear();
                    for(model.Empleado e:asignados) asignadosModel.addElement(e);
                    for(model.Empleado e:libres) libresModel.addElement(e);
                }catch(Exception ex){Dialogs.error(AsignacionPanel.this,"No se pudo cargar empleados");} }
        }.execute();
    }

    private void asignarEmpleado(){
        model.Proyecto p=(model.Proyecto) proyectoBox.getSelectedItem();
        model.Empleado emp=libresList.getSelectedValue();
        if(p==null || emp==null){Dialogs.warn(this,"Seleccioná un empleado libre");return;}
        try{service.asignar(emp.getId(),p.getId());cargarListas();}
        catch(ServiceException e){Dialogs.error(this,"No se pudo asignar");}
    }

    private void desasignarEmpleado(){
        model.Proyecto p=(model.Proyecto) proyectoBox.getSelectedItem();
        model.Empleado emp=asignadosList.getSelectedValue();
        if(p==null || emp==null){Dialogs.warn(this,"Seleccioná un empleado asignado");return;}
        try{service.desasignar(emp.getId(),p.getId());cargarListas();}
        catch(ServiceException e){Dialogs.error(this,"No se pudo desasignar");}
    }
}
