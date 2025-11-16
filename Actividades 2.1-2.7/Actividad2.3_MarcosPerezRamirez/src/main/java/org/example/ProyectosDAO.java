package org.example;

import conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class ProyectosDAO {

    public void listarProyectos() {

        // try-with-resources: abre la conexión, el Statement y el ResultSet; se cerrarán automáticamente
        try (Connection con = ConexionBD.getConexion(); // obtiene una conexión a la BD
             Statement st = con.createStatement(); // crea un Statement para ejecutar consultas
             ResultSet rs = st.executeQuery("SELECT id, nombre, presupuesto FROM proyectos")) { // ejecuta la consulta y obtiene los resultados
            // Itera sobre cada fila del ResultSet
            while (rs.next()) {
                // Imprime los valores de las columnas 'id', 'nombre' y 'salario' formateados
                System.out.printf("ID: %d - Nombre: %s - Proyectos: %.2f%n",
                        rs.getInt("id"), // obtiene la columna 'id' como entero
                        rs.getString("nombre"), // obtiene la columna 'nombre' como cadena
                        rs.getDouble("presupuesto")); // obtiene la columna 'salario' como double
            }
        } catch (Exception e) { // captura cualquier excepción (p. ej. SQLException)
            e.printStackTrace(); // imprime la traza de la excepción
        } finally {
            ConexionBD.cerrar();
        }
    }

    public void insertarProyecto(String nombre, double presupuesto) {
        try (Connection con = ConexionBD.getConexion()) {
            String sql = "INSERT INTO proyectos (nombre, presupuesto) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setDouble(2, presupuesto);
            int ejecucion = ps.executeUpdate();
            if (ejecucion > 0) {
                System.out.println("Proyecto insertado: " + nombre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarPresupuesto(int id, double nuevoPresupuesto) {
        try (Connection con = ConexionBD.getConexion()) {
            String sql = "UPDATE proyectos SET presupuesto = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, nuevoPresupuesto);
            ps.setInt(2, id);
            int ejecucion = ps.executeUpdate();
            if (ejecucion > 0) {
                System.out.println("Presupuesto actualizado para el proyecto ID: " + id);
            } else {
                System.out.println("Presupuesto no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void elimiarProyectoporId(int id){
        try (Connection con = ConexionBD.getConexion()){
            String sql = "DELETE FROM proyectos WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id);
            int ejecucion = ps.executeUpdate();
            if (ejecucion > 0){
                System.out.println("Proyecto eliminado ID: " + id);
            } else {
                System.out.println("Proyecto no encontrado");
        }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void mostrarMenu() {
        Scanner entrada = new Scanner(System.in);
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--------- Menú Proyectos ---------");
            System.out.println("1. Listar proyectos");
            System.out.println("2. Insertar proyecto");
            System.out.println("3. Actualizar presupuesto");
            System.out.println("4. Eliminar proyecto por id");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            if (entrada.hasNextInt()) {
                opcion = entrada.nextInt();
                entrada.nextLine();
            } else {
                System.out.println("Introduce un número válido.");
                entrada.nextLine(); // limpiamos buffer
                continue;
            }

            switch (opcion) {
                case 1:
                    listarProyectos();
                    break;
                case 2:
                    System.out.print("Nombre del proyecto: ");
                    String nombre = entrada.nextLine();
                    System.out.print("Presupuesto: ");
                    double presupuesto;
                    if (entrada.hasNextDouble()) {
                        presupuesto = entrada.nextDouble();
                        entrada.nextLine();
                        insertarProyecto(nombre, presupuesto);
                    } else {
                        System.out.println("Introduce un presupuesto válido.");
                        entrada.nextLine();
                    }
                    break;
                case 3:
                    System.out.print("ID del proyecto a modificar: ");
                    int idActualizar;
                    if (entrada.hasNextInt()) {
                        idActualizar = entrada.nextInt();
                        entrada.nextLine();
                    } else {
                        System.out.println("Introduce un ID válido.");
                        entrada.nextLine();
                        break;
                    }
                    System.out.print("Nuevo presupuesto: ");
                    double nuevoPresupuesto;
                    if (entrada.hasNextDouble()) {
                        nuevoPresupuesto = entrada.nextDouble();
                        entrada.nextLine();
                        actualizarPresupuesto(idActualizar, nuevoPresupuesto);
                    } else {
                        System.out.println("Introduce un presupuesto válido.");
                        entrada.nextLine();
                    }
                    break;
                case 4:
                    System.out.print("ID del proyecto a eliminar: ");
                    int idEliminar;
                    if (entrada.hasNextInt()) {
                        idEliminar = entrada.nextInt();
                        entrada.nextLine();
                        elimiarProyectoporId(idEliminar);
                    } else {
                        System.out.println("Introduce un ID válido.");
                        entrada.nextLine();
                    }
                    break;
                case 0:
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        entrada.close();
    }
}