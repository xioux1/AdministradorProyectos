package model;

import java.time.LocalDate;

import model.EstadoTarea;
public class Tarea {
    private int id;
    private String titulo;
    private String descripcion;
    private int horasEstimadas;
    private int horasReales;
    private LocalDate inicioSprint;
    private LocalDate finSprint;
    private EstadoTarea estado;
    
    public Tarea(String titulo, String descripcion, int horasEstimadas, int horasReales) {
        this(0, titulo, descripcion, horasEstimadas, horasReales, null, null, EstadoTarea.BACKLOG);
    }

    public Tarea(int id, String titulo, String descripcion, int horasEstimadas, int horasReales) {
        this(id, titulo, descripcion, horasEstimadas, horasReales, null, null, EstadoTarea.BACKLOG);
    }

    public Tarea(int id, String titulo, String descripcion,
                 int horasEstimadas, int horasReales,
                 LocalDate inicioSprint, LocalDate finSprint,
                 EstadoTarea estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.horasEstimadas = horasEstimadas;
        this.horasReales = horasReales;
        this.inicioSprint = inicioSprint;
        this.finSprint = finSprint;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getHorasEstimadas() { return horasEstimadas; }
    public void setHorasEstimadas(int horasEstimadas) { this.horasEstimadas = horasEstimadas; }

    public int getHorasReales() { return horasReales; }
    public void setHorasReales(int horasReales) { this.horasReales = horasReales; }

    public LocalDate getInicioSprint() { return inicioSprint; }
    public void setInicioSprint(LocalDate inicioSprint) { this.inicioSprint = inicioSprint; }

    public LocalDate getFinSprint() { return finSprint; }
    public void setFinSprint(LocalDate finSprint) { this.finSprint = finSprint; }

    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }

    @Override
    public String toString() { return titulo; }
}
