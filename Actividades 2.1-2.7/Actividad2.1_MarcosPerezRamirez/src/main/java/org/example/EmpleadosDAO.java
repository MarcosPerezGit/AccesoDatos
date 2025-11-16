package org.example;

import conexion.ConexionBD;

import java.sql.*;

public class EmpleadosDAO {

    public void listarEmpleados() {

        // try-with-resources: abre la conexión, el Statement y el ResultSet; se cerrarán automáticamente
        try (Connection con = ConexionBD.getConexion(); // obtiene una conexión a la BD
             Statement st = con.createStatement(); // crea un Statement para ejecutar consultas
             ResultSet rs = st.executeQuery("SELECT id, nombre, salario FROM empleados")) { // ejecuta la consulta y obtiene los resultados

            // Itera sobre cada fila del ResultSet
            while (rs.next()) {
                // Imprime los valores de las columnas 'id', 'nombre' y 'salario' formateados
                System.out.printf("ID: %d - Nombre: %s - Salario: %.2f%n",
                        rs.getInt("id"), // obtiene la columna 'id' como entero
                        rs.getString("nombre"), // obtiene la columna 'nombre' como cadena
                        rs.getDouble("salario")); // obtiene la columna 'salario' como double
            }

        } catch (Exception e) { // captura cualquier excepción (p. ej. SQLException)
            e.printStackTrace(); // imprime la traza de la excepción
        } finally {
            // Llamada final a la utilidad de cierre (mantiene la lógica original)
            ConexionBD.cerrar();
        }
    }

    public void buscarEmpleadoPorId(int id) {

        try (Connection con = ConexionBD.getConexion()) {
            // Consulta parametrizada: selecciona todas las columnas de la tabla empleados
            // para el empleado cuyo id coincida con el parámetro '?'
            String sql = "SELECT * FROM empleados WHERE id = ?";

            // Prepara la sentencia SQL con parámetros para evitar inyección y mejorar rendimiento
            PreparedStatement ps = con.prepareStatement(sql);

            // Asigna el valor 1 al primer parámetro (índice 1) de la consulta
            ps.setInt(1, id);

            // Ejecuta la consulta y obtiene el conjunto de resultados
            // ResultSet en su propio try-with-resources
            try (ResultSet rs = ps.executeQuery()) {
                // Si hay una fila en el ResultSet, obtiene y muestra el nombre y salario
                if (rs.next()) {
                    System.out.println("Empleado: " + rs.getString("nombre") + " - Salario: " + rs.getDouble("salario"));
                }
            } // rs se cierra aquí

            // Al finalizar el try-with-resources, la conexión se cerrará automáticamente.
            // El PreparedStatement y ResultSet deberían cerrarse explícitamente,
        } catch (Exception e) { // captura cualquier excepción (p. ej. SQLException u otras)
            e.printStackTrace(); // imprime la traza de la excepción en la salida de errores
        } // rs cerrado aquí
    }

    public void obtenerEmpleado(int id) {
        String sql = "{call obtener_empleado(?)}";
        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, id);

            try(ResultSet rs = cs.executeQuery()){
                if (rs.next()){
                    System.out.println("Empleado: " + rs.getString("nombre") + " - Salario: " + rs.getDouble("salario"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
    }
    }
}
