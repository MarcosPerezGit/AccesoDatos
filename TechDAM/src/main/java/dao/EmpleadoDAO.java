package dao;

import model.Empleado;
import config.DatabaseConfig;
import java.sql.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

// Esta clase sirve para manejar todas las operaciones de los empleados en la base de datos
public class EmpleadoDAO {

    // Metodo para crear un empleado con datos que se meten por teclado
    public int crearEmpleado() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("---- Crear nuevo empleado ----");
        System.out.print("Nombre: ");
        String nombre = entrada.nextLine();

        System.out.print("Departamento: ");
        String departamento = entrada.nextLine();

        System.out.print("Salario: ");
        BigDecimal salario = new BigDecimal(entrada.nextLine());

        System.out.print("Fecha de contratación (yyyy-MM-dd): ");
        LocalDate fecha = LocalDate.parse(entrada.nextLine());

        System.out.print("¿Activo? (true/false): ");
        boolean activo = Boolean.parseBoolean(entrada.nextLine());

        // Creo el objeto Empleado con los datos introducidos
        Empleado empleado = new Empleado(0, nombre, departamento, salario, fecha, activo);
        // Llamo al metodo para insertar el empleado en la base de datos
        return crear(empleado);
    }

    // Metodo que inserta el objeto Empleado en la base de datos
    public int crear(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, departamento, salario, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Meto cada dato en la consulta usando el PreparedStatement
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDepartamento());
            ps.setBigDecimal(3, empleado.getSalario());
            ps.setDate(4, Date.valueOf(empleado.getFecha_contratacion()));
            ps.setBoolean(5, empleado.isActivo());
            ps.executeUpdate();

            // Consigo el id generado para el nuevo empleado para devolverlo
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Si falla, devuelve -1
    }

    // Metodo para obtener la lista (ArrayList) de todos los empleados
    public List<Empleado> obtenerTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Por cada fila de resultados, creo un objeto Empleado y lo añado a la lista
            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getInt("id_empleado"),
                        rs.getString("nombre"),
                        rs.getString("departamento"),
                        rs.getBigDecimal("salario"),
                        rs.getDate("fecha_contratacion").toLocalDate(),
                        rs.getBoolean("activo")
                );
                lista.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista; // Devuelvo la lista de empleados
    }

    // Metodo para obtener un empleado según su id. Devuelve Optional por si no existe ese id.
    public Optional<Empleado> obtenerPorId(int id) {
        String sql = "SELECT * FROM empleados WHERE id_empleado = ?";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empleado emp = new Empleado(
                            rs.getInt("id_empleado"),
                            rs.getString("nombre"),
                            rs.getString("departamento"),
                            rs.getBigDecimal("salario"),
                            rs.getDate("fecha_contratacion").toLocalDate(),
                            rs.getBoolean("activo")
                    );
                    // Si existe, lo devuelvo dentro de Optional
                    return Optional.of(emp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Si no encuentra nada, devuelve vacío
    }

    // Metodo para actualizar los datos de un empleado (pide datos por teclado)
    public boolean actualizarEmpleado() {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Introduce el ID del empleado a modificar: ");
        int id = entrada.nextInt();
        entrada.nextLine();

        Optional<Empleado> optEmpleado = obtenerPorId(id);
        if (!optEmpleado.isPresent()) {
            System.out.println("No existe un empleado con ese ID.");
            return false;
        }

        // Saco el empleado actual para mostrar sus datos como referencia
        Empleado empleadoActual = optEmpleado.get();

        // Los datos nuevos se los pongo solo si el usuario ha escrito algo,
        // si no, se mantienen los antiguos (así puedes actualizar solo lo que quieres)
        System.out.print("Nuevo nombre (" + empleadoActual.getNombre() + "): ");
        String nombre = entrada.nextLine();
        if (nombre.isEmpty()) nombre = empleadoActual.getNombre();

        System.out.print("Nuevo departamento (" + empleadoActual.getDepartamento() + "): ");
        String departamento = entrada.nextLine();
        if (departamento.isEmpty()) departamento = empleadoActual.getDepartamento();

        System.out.print("Nuevo salario (" + empleadoActual.getSalario() + "): ");
        String salarioStr = entrada.nextLine();
        BigDecimal salario = salarioStr.isEmpty() ? empleadoActual.getSalario() : new BigDecimal(salarioStr);

        System.out.print("Fecha de contratación (yyyy-MM-dd) (" + empleadoActual.getFecha_contratacion() + "): ");
        String fechaStr = entrada.nextLine();
        LocalDate fecha = fechaStr.isEmpty() ? empleadoActual.getFecha_contratacion() : LocalDate.parse(fechaStr);

        System.out.print("¿Activo? (true/false) (" + empleadoActual.isActivo() + "): ");
        String activoStr = entrada.nextLine();
        boolean activo = activoStr.isEmpty() ? empleadoActual.isActivo() : Boolean.parseBoolean(activoStr);

        // Creo el objeto actualizado y lo mando al metodo actualizar
        Empleado empleadoActualizado = new Empleado(id, nombre, departamento, salario, fecha, activo);
        return actualizar(empleadoActualizado);
    }

    // Metodo que actualiza un empleado en la base de datos
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre=?, departamento=?, salario=?, fecha_contratacion=?, activo=? WHERE id_empleado=?";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDepartamento());
            ps.setBigDecimal(3, empleado.getSalario());
            ps.setDate(4, Date.valueOf(empleado.getFecha_contratacion()));
            ps.setBoolean(5, empleado.isActivo());
            ps.setInt(6, empleado.getIdEmpleado());
            return ps.executeUpdate() > 0; // Devuelve true si se modificó alguna fila
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo para eliminar un empleado por teclado (pide el id al usuario)
    public boolean eliminarEmpleado() {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Introduce el ID del empleado a eliminar: ");
        int id = entrada.nextInt();
        entrada.nextLine();
        return eliminar(id);
    }

    // Metodo que borra el empleado que tenga el id que le paso
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id_empleado=?";
        try (Connection con = DatabaseConfig.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0; // True si borra algún empleado
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}