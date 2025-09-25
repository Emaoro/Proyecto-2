package pagos;

import modelo.Atleta;
import modelo.SesionEntrenamiento;

import java.util.Comparator;
import java.util.List;

public class Planilla {
    private static final double BASE = 500.0;
    private static final double POR_SESION_NACIONAL = 20.0;
    private static final double POR_SESION_INTERNACIONAL = 40.0;
    private static final double BONO_MEJORA = 50.0;

    public double calcularPago(Atleta a) {
        List<SesionEntrenamiento> sesiones = a.getSesiones();
        double total = BASE;

        for (SesionEntrenamiento s : sesiones) {
            if ("Nacional".equalsIgnoreCase(s.getUbicacion())) total += POR_SESION_NACIONAL;
            else total += POR_SESION_INTERNACIONAL;
        }

        if (!sesiones.isEmpty()) {
            SesionEntrenamiento ultima = sesiones.stream().max(Comparator.comparing(SesionEntrenamiento::getFecha)).orElse(null);
            if (ultima != null) {
                double mejorExceptoUltima = sesiones.stream().filter(s -> s != ultima).mapToDouble(SesionEntrenamiento::getValorRendimiento).max().orElse(Double.NEGATIVE_INFINITY);
                if (ultima.getValorRendimiento() > mejorExceptoUltima) total += BONO_MEJORA;
            }
        }

        return total;
    }
}
