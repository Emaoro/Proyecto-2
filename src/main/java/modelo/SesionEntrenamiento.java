package modelo;

import java.time.LocalDate;

public class SesionEntrenamiento {
    private LocalDate fecha;
    private String tipo;
    private double valorRendimiento;
    private String ubicacion; // "Nacional" | "Internacional"
    private String pais;

    public SesionEntrenamiento(LocalDate fecha, String tipo, double valorRendimiento, String ubicacion, String pais) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.valorRendimiento = valorRendimiento;
        this.ubicacion = ubicacion;
        this.pais = pais;
    }

    public LocalDate getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public double getValorRendimiento() { return valorRendimiento; }
    public String getUbicacion() { return ubicacion; }
    public String getPais() { return pais; }

    public String detalles() {
        return String.format("%s | %s | %.2f | %s | %s", fecha, tipo, valorRendimiento, ubicacion, pais);
    }

    @Override
    public String toString() {
        return detalles();
    }
}
