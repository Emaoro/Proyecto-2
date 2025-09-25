package modelo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Estadistica {
    public double calcularPromedio(List<SesionEntrenamiento> sesiones){
        return sesiones.stream().mapToDouble(SesionEntrenamiento::getValorRendimiento).average().orElse(0);
    }

    public double mejorRendimiento(List<SesionEntrenamiento> sesiones){
        return sesiones.stream().mapToDouble(SesionEntrenamiento::getValorRendimiento).max().orElse(0);
    }

    public java.util.List<Double> evolucion(List<SesionEntrenamiento> sesiones){
        return sesiones.stream()
                .sorted(Comparator.comparing(SesionEntrenamiento::getFecha))
                .map(SesionEntrenamiento::getValorRendimiento)
                .collect(Collectors.toList());
    }

    public long contarNacional(List<SesionEntrenamiento> sesiones){
        return sesiones.stream().filter(s -> "Nacional".equalsIgnoreCase(s.getUbicacion())).count();
    }

    public long contarInternacional(List<SesionEntrenamiento> sesiones){
        return sesiones.stream().filter(s -> !"Nacional".equalsIgnoreCase(s.getUbicacion())).count();
    }
}
