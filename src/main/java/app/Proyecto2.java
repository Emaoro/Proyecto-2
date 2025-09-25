package app;

import modelo.*;
import pagos.Planilla;
import persistencia.CsvUtil;
import persistencia.JsonUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Proyecto2 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RegistroAtletas registro = RegistroAtletas.getInstancia();
    private static final Estadistica estadistica = new Estadistica();
    private static final Planilla planilla = new Planilla();

    public static void main(String[] args) {
        List<Atleta> desdeBD = Persistencia.listarAtletasDB();
        for (Atleta a : desdeBD) {
            if (registro.buscarPorId(a.getId()) == null) {
                registro.registrarAtletaSinPersistir(a); 
            }
        }

        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1" -> registrarAtleta();
                case "2" -> registrarSesion();
                case "3" -> consultarEstadisticas();
                case "4" -> generarReportesCsv();
                case "5" -> guardarYCargarJson();
                case "6" -> procesarPago();
                case "7" -> mostrarTodosAtletas();
                case "8" -> salir = true;
                default -> System.out.println("Opción inválida");
            }
        }
        System.out.println("Saliendo con exito");
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Sistema Monitoreo - Comité Olímpico Guatemala ---");
        System.out.println("1. Registrar nuevo atleta");
        System.out.println("2. Registrar sesión de entrenamiento");
        System.out.println("3. Consultar estadísticas de un atleta");
        System.out.println("4. Generar reportes CSV");
        System.out.println("5. Guardar/Cargar JSON (respaldo)");
        System.out.println("6. Procesar planilla (pago)");
        System.out.println("7. Mostrar todos los atletas y sesiones con pago");
        System.out.println("8. Salir");
        System.out.print("Elige opción: ");
    }

    private static void registrarAtleta() {
        try {
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine();
            System.out.print("Edad: ");
            int edad = Integer.parseInt(scanner.nextLine());
            System.out.print("Disciplina (Atletismo, Natacion, Pesas, Ciclismo, Boxeo, Judo, Futbol, Basquetbol, Tenis): ");
            Disciplina disciplina = Disciplina.valueOf(scanner.nextLine());
            System.out.print("Departamento: ");
            String departamento = scanner.nextLine();
            System.out.print("Nacionalidad: ");
            String nacionalidad = scanner.nextLine();
            System.out.print("Fecha ingreso (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine());

            Atleta atleta = new Atleta(nombre, edad, disciplina, departamento, nacionalidad, fecha);
            registro.registrarAtleta(atleta);
        } catch (IllegalArgumentException e) {
            System.out.println("Dato inválido: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Formato fecha inválido");
        } catch (Exception e) {
            System.out.println("Error registrando atleta: " + e.getMessage());
        }
    }

    private static void registrarSesion() {
        try {
            System.out.print("Id del atleta: ");
            int id = Integer.parseInt(scanner.nextLine());
            Atleta a = registro.buscarPorId(id);
            if (a == null) {
                System.out.println("Atleta no encontrado en memoria. Intenta listar desde BD o registra primero");
                return;
            }
            System.out.print("Fecha (YYYY-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine());
            System.out.print("Tipo (Resistencia, Tecnica, Fuerza...): ");
            String tipo = scanner.nextLine();
            System.out.print("Valor rendimiento (numero): ");
            double valor = Double.parseDouble(scanner.nextLine());
            System.out.print("Ubicación (Nacional/Internacional): ");
            String ubicacion = scanner.nextLine();
            System.out.print("País (si es internacional): ");
            String pais = scanner.nextLine();

            SesionEntrenamiento s = SesionFactory.crearSesion(tipo, fecha, valor, ubicacion, pais);
            a.agregarSesion(s);
            Persistencia.guardarSesionDB(a.getId(), s);
            System.out.println("Sesión registrada");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void consultarEstadisticas() {
        try {
            System.out.print("Id atleta: ");
            int id = Integer.parseInt(scanner.nextLine());
            Atleta atleta = registro.buscarPorId(id);
            if (atleta == null) {
                System.out.println("Atleta no encontrado");
                return;
            }
            List<SesionEntrenamiento> sesiones = atleta.getSesiones();
            System.out.println("Historial:");
            sesiones.forEach(s -> System.out.println(" - " + s.detalles()));
            System.out.println("Promedio: " + estadistica.calcularPromedio(sesiones));
            System.out.println("Mejor: " + estadistica.mejorRendimiento(sesiones));
            System.out.println("Evolución: " + estadistica.evolucion(sesiones));
            System.out.println("Nacional: " + estadistica.contarNacional(sesiones) + " | Internacional: " + estadistica.contarInternacional(sesiones));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generarReportesCsv() {
        System.out.print("Id atleta: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Atleta atleta = registro.buscarPorId(id);
            if (atleta == null) {
                System.out.println("Atleta no encontrado");
                return;
            }
            CsvUtil.guardarAtletaCsv(atleta);
            System.out.println("Reportes CSV generados (atletas.csv + sesiones.csv)");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void guardarYCargarJson() {
        System.out.print("Id atleta a respaldar en JSON: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Atleta a = registro.buscarPorId(id);
            if (a == null) {
                System.out.println("Atleta no encontrado");
                return;
            }
            JsonUtil.guardarAtletaJson(a);
            System.out.println("Atleta guardado en atletas.json");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void procesarPago() {
        System.out.print("Id atleta: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Atleta atleta = registro.buscarPorId(id);
            if (atleta == null) {
                System.out.println("Atleta no encontrado");
                return;
            }
            double pago = planilla.calcularPago(atleta);
            System.out.printf("Pago calculado para %s: Q%.2f%n", atleta.getNombreCompleto(), pago);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void mostrarTodosAtletas() {
        List<Atleta> atletas = registro.getAtletas();
        if (atletas.isEmpty()) {
            System.out.println("No hay atletas registrados en memoria");
            return;
        }
        for (Atleta atleta : atletas) {
            System.out.println(atleta);
            if (!atleta.getSesiones().isEmpty()) {
                System.out.println("  Sesiones:");
                for (SesionEntrenamiento sesionEntrenamiento : atleta.getSesiones()) {
                    System.out.println("   - " + sesionEntrenamiento.detalles());
                }
            } else {
                System.out.println("  No tiene sesiones registradas");
            }
            double pago = planilla.calcularPago(atleta);
            System.out.printf("  Pago estimado: Q%.2f%n", pago);
        }
    }
}
