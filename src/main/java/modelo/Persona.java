package modelo;

public class Persona {
    protected String nombreCompleto;
    protected int edad;

    public Persona(String nombreCompleto, int edad) {
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public int getEdad() { return edad; }

    @Override
    public String toString() {
        return nombreCompleto + " (" + edad + " años)";
    }
}
