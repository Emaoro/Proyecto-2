package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import modelo.Atleta;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static void guardarAtletaJson(Atleta atleta) {
        try (FileWriter writer = new FileWriter("atletas.json", true)) {
            gson.toJson(atleta, writer);
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Atleta cargarAtletaDesdeArchivo(String archivo) {
        try (FileReader fr = new FileReader(archivo)) {
            return gson.fromJson(fr, Atleta.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
