package modelo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Persistencia {
    private static final String URL = "jdbc:mariadb://localhost:3306/sistema_atletas";
    private static final String USER = "root";
    private static final String PASS = "Ema";

    static {
        initDB();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void initDB() {
        String serverUrl = "jdbc:mariadb://localhost:3306/";
        try (Connection conn = DriverManager.getConnection(serverUrl, USER, PASS);
             Statement st = conn.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS sistema_atletas");
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo crear DB automáticamente, verifica manualmente. " + e.getMessage());
        }

        String sqlAtletas = "CREATE TABLE IF NOT EXISTS atletas (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nombre VARCHAR(255)," +
                "edad INT," +
                "disciplina VARCHAR(100)," +
                "departamento VARCHAR(100)," +
                "nacionalidad VARCHAR(100)," +
                "fecha_ingreso DATE" +
                ")";
        String sqlSesiones = "CREATE TABLE IF NOT EXISTS sesiones (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "atleta_id INT," +
                "fecha DATE," +
                "tipo VARCHAR(150)," +
                "valor_rendimiento DOUBLE," +
                "ubicacion VARCHAR(50)," +
                "pais VARCHAR(100)," +
                "FOREIGN KEY (atleta_id) REFERENCES atletas(id) ON DELETE CASCADE" +
                ")";

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sqlAtletas);
            st.execute(sqlSesiones);
        } catch (SQLException e) {
            System.err.println("No se pudo inicializar tablas: " + e.getMessage());
        }
    }

    public static void guardarAtletaDB(Atleta atleta) {
        String sql = "INSERT INTO atletas (nombre, edad, disciplina, departamento, nacionalidad, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, atleta.getNombreCompleto());
            ps.setInt(2, atleta.getEdad());
            ps.setString(3, atleta.getDisciplina().name());
            ps.setString(4, atleta.getDepartamento());
            ps.setString(5, atleta.getNacionalidad());
            ps.setDate(6, Date.valueOf(atleta.getFechaIngreso()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    atleta.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                actualizarAtletaDB(atleta);
            } else {
                e.printStackTrace();
            }
        }
    }

    public static void actualizarAtletaDB(Atleta atleta) {
        String sql = "UPDATE atletas SET nombre=?, edad=?, disciplina=?, departamento=?, nacionalidad=?, fecha_ingreso=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, atleta.getNombreCompleto());
            ps.setInt(2, atleta.getEdad());
            ps.setString(3, atleta.getDisciplina().name());
            ps.setString(4, atleta.getDepartamento());
            ps.setString(5, atleta.getNacionalidad());
            ps.setDate(6, Date.valueOf(atleta.getFechaIngreso()));
            ps.setInt(7, atleta.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void guardarSesionDB(int atletaId, SesionEntrenamiento s) {
        String sql = "INSERT INTO sesiones (atleta_id, fecha, tipo, valor_rendimiento, ubicacion, pais) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, atletaId);
            ps.setDate(2, Date.valueOf(s.getFecha()));
            ps.setString(3, s.getTipo());
            ps.setDouble(4, s.getValorRendimiento());
            ps.setString(5, s.getUbicacion());
            ps.setString(6, s.getPais());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Atleta> listarAtletasDB() {
        List<Atleta> res = new ArrayList<>();
        String sql = "SELECT * FROM atletas";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int edad = rs.getInt("edad");
                Disciplina disciplina = Disciplina.valueOf(rs.getString("disciplina"));
                String departamento = rs.getString("departamento");
                String nacionalidad = rs.getString("nacionalidad");
                LocalDate fechaIngreso = rs.getDate("fecha_ingreso").toLocalDate();
                Atleta a = new Atleta(id, nombre, edad, disciplina, departamento, nacionalidad, fechaIngreso);

                List<SesionEntrenamiento> sesiones = listarSesionesPorAtleta(id);
                sesiones.forEach(a::agregarSesion);
                res.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<SesionEntrenamiento> listarSesionesPorAtleta(int atletaId) {
        List<SesionEntrenamiento> res = new ArrayList<>();
        String sql = "SELECT fecha, tipo, valor_rendimiento, ubicacion, pais FROM sesiones WHERE atleta_id = ? ORDER BY fecha";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, atletaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    String tipo = rs.getString("tipo");
                    double valor = rs.getDouble("valor_rendimiento");
                    String ubicacion = rs.getString("ubicacion");
                    String pais = rs.getString("pais");
                    res.add(new SesionEntrenamiento(fecha, tipo, valor, ubicacion, pais));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
