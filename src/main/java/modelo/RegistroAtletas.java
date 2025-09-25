package modelo;

import java.util.ArrayList;
import java.util.List;

public class RegistroAtletas {
    private static RegistroAtletas instancia;
    private List<Atleta> atletas;

    private RegistroAtletas() {
        atletas = new ArrayList<>();
    }

    public static RegistroAtletas getInstancia() {
        if (instancia == null) {
            instancia = new RegistroAtletas();
        }
        return instancia;
    }

    public void registrarAtleta(Atleta a) {
        atletas.add(a);
    }

    public void registrarAtletaSinPersistir(Atleta a) {
        if (!atletas.contains(a)) {
            atletas.add(a);
        }
    }

    public List<Atleta> getAtletas() {
        return atletas;
    }

    public Atleta buscarPorId(int id) {
        return atletas.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    public void limpiarTodo() {
        atletas.clear();
    }
}
