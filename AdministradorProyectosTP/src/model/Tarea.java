package model;

public class Tarea {
    private int id;
    private String titulo;
    private String descripcion;
    private int horasEstimadas;
    private int horasReales;
    
    public Tarea(String titulo, String descripcion, int horasEstimadas, int horasReales) {
        this(0, titulo, descripcion, horasEstimadas, horasReales);
    }


    public Tarea(int id, String titulo, String descripcion, int horasEstimadas, int horasReales) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.horasEstimadas = horasEstimadas;
        this.horasReales = horasReales;
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
}
