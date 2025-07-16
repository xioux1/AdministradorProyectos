package model;

import java.time.LocalDateTime;
import model.Tarea;

public class HistorialEstado {
    private int id;
    private Tarea tarea;
    private EstadoTarea estado;
    private String responsable;
    private LocalDateTime fecha;

    public HistorialEstado(int id, Tarea tarea, EstadoTarea estado, String responsable, LocalDateTime fecha) {
        this.id = id;
        this.tarea = tarea;
        this.estado = estado;
        this.responsable = responsable;
        this.fecha = fecha;
    }

    public HistorialEstado(Tarea tarea, EstadoTarea estado, String responsable, LocalDateTime fecha) {
        this(0, tarea, estado, responsable, fecha);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Tarea getTarea() { return tarea; }
    public int getTareaId() { return tarea != null ? tarea.getId() : 0; }
    public EstadoTarea getEstado() { return estado; }
    public String getResponsable() { return responsable; }
    public LocalDateTime getFecha() { return fecha; }
}
