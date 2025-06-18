package model;

import java.time.LocalDateTime;

public class HistorialEstado {
    private int id;
    private int tareaId;
    private EstadoTarea estado;
    private String responsable;
    private LocalDateTime fecha;

    public HistorialEstado(int id, int tareaId, EstadoTarea estado, String responsable, LocalDateTime fecha) {
        this.id = id;
        this.tareaId = tareaId;
        this.estado = estado;
        this.responsable = responsable;
        this.fecha = fecha;
    }

    public HistorialEstado(int tareaId, EstadoTarea estado, String responsable, LocalDateTime fecha) {
        this(0, tareaId, estado, responsable, fecha);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTareaId() { return tareaId; }
    public EstadoTarea getEstado() { return estado; }
    public String getResponsable() { return responsable; }
    public LocalDateTime getFecha() { return fecha; }
}
