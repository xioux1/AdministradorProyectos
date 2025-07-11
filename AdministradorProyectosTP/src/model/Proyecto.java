package model;

public class Proyecto {
    private int id;
    private String nombre;

    public Proyecto(String nombre) {
        this(0, nombre);
    }

    public Proyecto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}
