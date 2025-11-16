package service;

import config.DatabaseConfig;
import java.sql.*;
import java.util.Scanner;

// Clase que contiene métodos que llaman a procedimientos almacenados de la base de datos
public class ProcedimientosService {

    // Metodo para actualizar el salario de los empleados de un departamento, llamando a un procedimiento almacenado
    public void actualizarSalarioDepartamento() {
        Scanner entrada = new Scanner(System.in);

        // Pido el nombre del departamento al usuario
        System.out.print("Introduce el departamento: ");
        String departamento = entrada.nextLine();

        // Pido el porcentaje de incremento, se pone con decimales por si acaso
        System.out.print("Introduce el porcentaje de incremento (Ejemplo: 3.5 para 3.5%): ");
        double porcentaje = Double.parseDouble(entrada.nextLine());

        // Defino la llamada al procedimiento almacenado (SQL)
        String sql = "{CALL actualizar_salario_departamento(?, ?, ?)}";
        try (Connection con = DatabaseConfig.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            // Seteo los parámetros de entrada y salida, los IN van con los datos leídos y el OUT lo registro para leerlo después
            cs.setString(1, departamento);  // Primer parámetro: nombre del departamento
            cs.setDouble(2, porcentaje);    // Segundo parámetro: porcentaje de incremento
            cs.registerOutParameter(3, Types.INTEGER); // Tercer parámetro: será un entero (total empleados actualizados)

            cs.execute(); // Ejecuto el procedimiento

            // Leo el número de empleados que se han actualizado (viene del parámetro OUT)
            int empleadosActualizados = cs.getInt(3);
            System.out.println("Empleados actualizados: " + empleadosActualizados);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo que llama a un procedimiento almacenado para obtener el número de empleados asignados a un proyecto
    public void empleadosPorProyecto() {
        Scanner entrada = new Scanner(System.in);

        // Pido al usuario el id del proyecto
        System.out.print("Introduce el ID del proyecto: ");
        int idProyecto = entrada.nextInt();
        entrada.nextLine();

        // Defino la llamada al procedimiento
        String sql = "{CALL empleados_por_proyecto(?, ?)}";
        try (Connection con = DatabaseConfig.getConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            // Seteo el id del proyecto como parámetro de entrada
            cs.setInt(1, idProyecto);        // Primer parámetro: id del proyecto
            cs.registerOutParameter(2, Types.INTEGER);  // Segundo parámetro: entero con el resultado

            cs.execute();

            // Leo el resultado
            int totalEmpleados = cs.getInt(2);
            System.out.println("Total empleados asignados al proyecto: " + totalEmpleados);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}