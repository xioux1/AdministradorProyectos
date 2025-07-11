package model;

public class Empleado {
    private int id;
    private String nombre;
    private int costoHora;
    
    //constructor con id sin inicializar
    public Empleado(String nombre, int costoHora) {
        this(0, nombre, costoHora);
    }
    //id especifico
    public Empleado(int id, String nombre, int costoHora) {
        this.id = id;
        this.nombre = nombre;
        this.costoHora = costoHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCostoHora() { return costoHora; }
    public void setCostoHora(int costoHora) { this.costoHora = costoHora; }
    
    @Override
    //clase parent object
    public String toString() { return nombre; }
}
