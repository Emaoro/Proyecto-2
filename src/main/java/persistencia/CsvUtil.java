package persistencia;

import com.opencsv.CSVWriter;
import modelo.Atleta;
import modelo.SesionEntrenamiento;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvUtil {
    public static void guardarAtletaCsv(Atleta atleta) {
        File f = new File("atletas.csv");
        boolean nuevo = !f.exists();
        try (CSVWriter writer = new CSVWriter(new FileWriter(f, true))) {
            if (nuevo) {
                String[] header = {"id","nombre","edad","disciplina","departamento","nacionalidad","fecha_ingreso"};
                writer.writeNext(header);
            }
            String[] fila = {
                    String.valueOf(atleta.getId()),
                    atleta.getNombreCompleto(),
                    String.valueOf(atleta.getEdad()),
                    atleta.getDisciplina().name(),
                    atleta.getDepartamento(),
                    atleta.getNacionalidad(),
                    atleta.getFechaIngreso().toString()
            };
            writer.writeNext(fila);

            exportarSesiones(atleta.getId(), atleta.getSesiones());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exportarSesiones(int atletaId, List<SesionEntrenamiento> sesiones) throws IOException {
        File f = new File("sesiones.csv");
        boolean nuevo = !f.exists();
        try (CSVWriter writer = new CSVWriter(new FileWriter(f, true))) {
            if (nuevo) {
                writer.writeNext(new String[]{"atleta_id","fecha","tipo","valor_rendimiento","ubicacion","pais"});
            }
            for (SesionEntrenamiento s : sesiones) {
                String[] fila = {
                        String.valueOf(atletaId),
                        s.getFecha().toString(),
                        s.getTipo(),
                        String.valueOf(s.getValorRendimiento()),
                        s.getUbicacion(),
                        s.getPais()
                };
                writer.writeNext(fila);
            }
        }
    }
}
