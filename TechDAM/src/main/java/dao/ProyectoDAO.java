package dao;
import model.Proyecto;
import config.DatabaseConfig;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

// Esta clase sirve para manejar los proyectos en la base de datos (CRUD)
public class ProyectoDAO {

    // Metodo para crear un nuevo proyecto por teclado
    public int crearProyecto() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("---- Crear nuevo proyecto ----");
        System.out.print("Nombre: ");
        String nombre = entrada.nextLine();

        System.out.print("Fecha inicio (yyyy-MM-dd): ");
        LocalDate inicio = LocalDate.parse(entrada.nextLine());

        System.out.print("Fecha fin (yyyy-MM-dd): ");
        LocalDate fin = LocalDate.parse(entrada.nextLine());

        System.out.print("Presupuesto: ");
        BigDecimal presupuesto = new BigDecimal(entrada.nextLine());

        // Creo el objeto Proyecto con los datos introducidos
        Proyecto proyecto = new Proyecto(0, nombre, inicio, fin, presupuesto);
        // Inserto en base de datos usando el método crear
        return crear(proyecto);
    }

    // Metodo que inserta el proyecto en la base de datos y devuelve el id generado
    public int crear(Proyecto proyecto) {
        String sql = "INSERT INTO proyectos (nombre, fecha_inicio, fecha_fin, presupuesto) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proyecto.getNombre());
            ps.setDate(2, Date.valueOf(proyecto.getFecha_inicio()));
            ps.setDate(3, Date.valueOf(proyecto.getFecha_fin()));
            ps.setBigDecimal(4, proyecto.getPresupuesto());
            ps.executeUpdate();

            // Consigo el id generado del proyecto
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Si falla algo, devuelve -1
    }

    // Metodo que devuelve una lista (ArrayList) con todos los proyectos de la base de datos
    public List<Proyecto> obtenerTodos() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Por cada registro (fila), creo un objeto Proyecto y lo meto en la lista
            while (rs.next()) {
                Proyecto proy = new Proyecto(
                        rs.getInt("id_proyecto"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        rs.getDate("fecha_fin").toLocalDate(),
                        rs.getBigDecimal("presupuesto")
                );
                lista.add(proy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista; // Devuelvo la lista con todos los proyectos
    }

    // Metodo para sacar un proyecto por su id (usa Optional por si no existe)
    public Optional<Proyecto> obtenerPorId(int id) {
        String sql = "SELECT * FROM proyectos WHERE id_proyecto = ?";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Proyecto proy = new Proyecto(
                            rs.getInt("id_proyecto"),
                            rs.getString("nombre"),
                            rs.getDate("fecha_inicio").toLocalDate(),
                            rs.getDate("fecha_fin").toLocalDate(),
                            rs.getBigDecimal("presupuesto")
                    );
                    return Optional.of(proy);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Metodo para actualizar un proyecto por teclado, se puede dejar algún dato igual si pulsas Enter
    public boolean actualizarProyecto() {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Introduce el ID del proyecto a modificar: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        Optional<Proyecto> optProyecto = obtenerPorId(id);
        if (!optProyecto.isPresent()) {
            System.out.println("No existe un proyecto con ese ID.");
            return false;
        }
        Proyecto proyectoActual = optProyecto.get();

        // Permite cambiar solo lo que el usuario quiera, el resto se queda igual
        System.out.print("Nuevo nombre (" + proyectoActual.getNombre() + "): ");
        String nombre = entrada.nextLine();
        if (nombre.isEmpty()) nombre = proyectoActual.getNombre();

        System.out.print("Nueva fecha inicio (yyyy-MM-dd) (" + proyectoActual.getFecha_inicio() + "): ");
        String inicioStr = entrada.nextLine();
        LocalDate inicio = inicioStr.isEmpty() ? proyectoActual.getFecha_inicio() : LocalDate.parse(inicioStr);

        System.out.print("Nueva fecha fin (yyyy-MM-dd) (" + proyectoActual.getFecha_fin() + "): ");
        String finStr = entrada.nextLine();
        LocalDate fin = finStr.isEmpty() ? proyectoActual.getFecha_fin() : LocalDate.parse(finStr);

        System.out.print("Nuevo presupuesto (" + proyectoActual.getPresupuesto() + "): ");
        String presupuestoStr = entrada.nextLine();
        BigDecimal presupuesto = presupuestoStr.isEmpty() ? proyectoActual.getPresupuesto() : new BigDecimal(presupuestoStr);

        // Creo el objeto actualizado y llamo al método de actualizar en la base de datos
        Proyecto proyectoActualizado = new Proyecto(id, nombre, inicio, fin, presupuesto);
        return actualizar(proyectoActualizado);
    }

    // Metodo para actualizar un proyecto en la base de datos con un objeto Proyecto
    public boolean actualizar(Proyecto proyecto) {
        String sql = "UPDATE proyectos SET nombre=?, fecha_inicio=?, fecha_fin=?, presupuesto=? WHERE id_proyecto=?";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, proyecto.getNombre());
            ps.setDate(2, Date.valueOf(proyecto.getFecha_inicio()));
            ps.setDate(3, Date.valueOf(proyecto.getFecha_fin()));
            ps.setBigDecimal(4, proyecto.getPresupuesto());
            ps.setInt(5, proyecto.getIdProyecto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo para borrar un proyecto por teclado (pide el id)
    public boolean eliminarProyecto() {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Introduce el ID del proyecto a eliminar: ");
        int id = entrada.nextInt();
        entrada.nextLine();
        return eliminar(id);
    }

    // Metodo para borrar de la base de datos el proyecto por su id
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proyectos WHERE id_proyecto=?";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}