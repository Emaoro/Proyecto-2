package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Atleta extends Persona {
    private static int contadorId = 1;
    private int id;
    private Disciplina disciplina;
    private String departamento;
    private String nacionalidad;
    private LocalDate fechaIngreso;
    private List<SesionEntrenamiento> sesiones = new ArrayList<>();

    public Atleta(String nombre, int edad, Disciplina disciplina, String departamento, String nacionalidad, LocalDate fechaIngreso) {
        super(nombre, edad);
        this.id = contadorId++;
        this.disciplina = disciplina;
        this.departamento = departamento;
        this.nacionalidad = nacionalidad;
        this.fechaIngreso = fechaIngreso;
    }

    public Atleta(int id, String nombre, int edad, Disciplina disciplina, String departamento, String nacionalidad, LocalDate fechaIngreso) {
        super(nombre, edad);
        this.id = id;
        if (id >= contadorId) contadorId = id + 1;
        this.disciplina = disciplina;
        this.departamento = departamento;
        this.nacionalidad = nacionalidad;
        this.fechaIngreso = fechaIngreso;
    }

    public void agregarSesion(SesionEntrenamiento sesion) { sesiones.add(sesion); }
    public List<SesionEntrenamiento> getSesiones() { return Collections.unmodifiableList(sesiones); }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
        if (id >= contadorId) contadorId = id + 1;
    }

    public Disciplina getDisciplina() { return disciplina; }
    public String getDepartamento() { return departamento; }
    public String getNacionalidad() { return nacionalidad; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }

    @Override
    public String toString() {
        return id + " | " + nombreCompleto + " | " + edad + " | " + disciplina + " | " + departamento + " | " + nacionalidad + " | " + fechaIngreso;
    }
}
