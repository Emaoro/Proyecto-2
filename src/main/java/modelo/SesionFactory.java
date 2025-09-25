package modelo;

import java.time.LocalDate;

public class SesionFactory {
    public static SesionEntrenamiento crearSesion(String tipo, LocalDate fecha, double valor, String ubicacion, String pais) {
        if (ubicacion == null || ubicacion.isBlank()) ubicacion = "Nacional";
        if (pais == null || pais.isBlank()) pais = "Guatemala";
        return new SesionEntrenamiento(fecha, tipo, valor, ubicacion, pais);
    }
}
