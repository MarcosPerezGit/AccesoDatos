Tareas
1.	Crea la BD con las tablas empleados , proyectos y asignaciones .
2.	Inserta empleados y proyectos con PreparedStatement.
3.	Implementa un procedimiento almacenado para asignar empleados a proyectos.
4.	Crea un programa que:
		Ejecute el procedimiento almacenado.
Realice una transacción que incremente el salario de un empleado y descuente ese importe del presupuesto de un proyecto.
		Use un pool de conexiones para obtener la conexión.
5.	Muestra el estado final de las tablas en consola.

Base de Datos:

`DROP DATABASE IF EXISTS empresa27;`
`CREATE DATABASE IF NOT EXISTS empresa27;`
`USE empresa27;`

`CREATE TABLE empleados (`
    `id INT AUTO_INCREMENT PRIMARY KEY,`
    `nombre VARCHAR(100) NOT NULL,`
    `salario DOUBLE NOT NULL`
`);`

`CREATE TABLE proyectos (`
    `id INT AUTO_INCREMENT PRIMARY KEY,`
    `nombre VARCHAR(100) NOT NULL,`
    `presupuesto DOUBLE NOT NULL`
`);`

`CREATE TABLE asignaciones (`
    `id INT AUTO_INCREMENT PRIMARY KEY,`
    `empleado_id INT NOT NULL,`
    `proyecto_id INT NOT NULL,`
    `FOREIGN KEY (empleado_id) REFERENCES empleados(id),`
    `FOREIGN KEY (proyecto_id) REFERENCES proyectos(id)`
`);`

`INSERT INTO empleados (nombre, salario) VALUES ('Juan Perez', 25000);`
`INSERT INTO empleados (nombre, salario) VALUES ('Maria Gomez', 30000);`
`INSERT INTO empleados (nombre, salario) VALUES ('Luis Rodríguez', 28000);`

`INSERT INTO proyectos (nombre, presupuesto) VALUES ('Proyecto Alpha', 100000);`
`INSERT INTO proyectos (nombre, presupuesto) VALUES ('Proyecto Beta', 75000);`
`INSERT INTO proyectos (nombre, presupuesto) VALUES ('Proyecto Gamma', 50000);`

`INSERT INTO asignaciones (empleado_id, proyecto_id) VALUES (1, 1);`
`INSERT INTO asignaciones (empleado_id, proyecto_id) VALUES (2, 2);`
`INSERT INTO asignaciones (empleado_id, proyecto_id) VALUES (3, 3);`

`DELIMITER //`
`CREATE PROCEDURE asignar_empleado_proyecto(`
    `IN empId INT,`
    `IN proyId INT`
`)`
`BEGIN`
    `INSERT INTO asignaciones (empleado_id, proyecto_id)`
    `VALUES (empId, proyId);`
`END //`
`DELIMITER ;`


`-- Ver todos los empleados y sus salarios`
`SELECT * FROM empleados;`

`-- Ver todos los proyectos y sus presupuestos`
`SELECT * FROM proyectos;`

`-- Ver todas las asignaciones empleado-proyecto`
`SELECT * FROM asignaciones;`

`-- Ver empleados asignados a cada proyecto`
`SELECT e.id AS empleado_id, e.nombre AS empleado, p.id AS proyecto_id, p.nombre AS proyecto`
`FROM asignaciones a`
`JOIN empleados e ON a.empleado_id = e.id`
`JOIN proyectos p ON a.proyecto_id = p.id;`

`-- Estadística de presupuesto restante por proyecto`
`SELECT nombre, presupuesto FROM proyectos;`

`-- Salario de empleados tras actualizaciones`
`SELECT nombre, salario FROM empleados;`

`**EmpresaDAO.java:**`

`package org.example;`  
  
`import conexion.ConexionBD;`  
`import java.sql.*;`  
`import java.util.Scanner;`  
  
`public class EmpresaDAO {`  
  
    `public void insertarEmpleado() {`  
        `Scanner entrada = new Scanner(System.in);`  
        `System.out.print("Nombre del empleado: ");`  
        `String nombre = entrada.nextLine();`  
        `System.out.print("Salario: ");`  
        `double salario = entrada.nextDouble();`  
        `entrada.nextLine();`  
  
        `try (Connection con = ConexionBD.getConexion();`  
             `PreparedStatement ps = con.prepareStatement("INSERT INTO empleados (nombre, salario) VALUES (?, ?)")) {`  
  
            `ps.setString(1, nombre);`  
            `ps.setDouble(2, salario);`  
            `ps.executeUpdate();`  
            `System.out.println("Empleado insertado: " + nombre);`  
        `} catch (Exception e) { e.printStackTrace(); }`  
        `finally { ConexionBD.cerrar(); }`  
    `}`  
  
    `public void insertarProyecto() {`  
        `Scanner entrada = new Scanner(System.in);`  
        `System.out.print("Nombre del proyecto: ");`  
        `String nombre = entrada.nextLine();`  
        `System.out.print("Presupuesto: ");`  
        `double presupuesto = entrada.nextDouble();`  
        `entrada.nextLine();`  
  
        `try (Connection con = ConexionBD.getConexion();`  
             `PreparedStatement ps = con.prepareStatement("INSERT INTO proyectos (nombre, presupuesto) VALUES (?, ?)")) {`  
  
            `ps.setString(1, nombre);`  
            `ps.setDouble(2, presupuesto);`  
            `ps.executeUpdate();`  
            `System.out.println("Proyecto insertado: " + nombre);`  
        `} catch (Exception e) { e.printStackTrace(); }`  
        `finally { ConexionBD.cerrar(); }`  
    `}`  
  
    `public void asignarEmpleadoAProyecto() {`  
        `Scanner entrada = new Scanner(System.in);`  
        `System.out.print("ID del empleado: ");`  
        `int empId = entrada.nextInt();`  
        `entrada.nextLine();`  
        `System.out.print("ID del proyecto: ");`  
        `int proyId = entrada.nextInt();`  
        `entrada.nextLine();`  
  
        `try (Connection con = ConexionBD.getConexion();`  
             `CallableStatement cs = con.prepareCall("{call asignar_empleado_proyecto(?, ?)}")) {`  
            `cs.setInt(1, empId);`  
            `cs.setInt(2, proyId);`  
            `cs.execute();`  
            `System.out.println("Empleado (ID " + empId + ") asignado al proyecto (ID " + proyId + ")");`  
        `} catch (Exception e) { e.printStackTrace(); }`  
        `finally { ConexionBD.cerrar(); }`  
    `}`  
  
    `public void incrementarSalarioYDescontarPresupuesto() {`  
        `Scanner entrada = new Scanner(System.in);`  
        `System.out.print("ID del empleado: ");`  
        `int empId = entrada.nextInt();`  
        `entrada.nextLine();`  
        `System.out.print("ID del proyecto: ");`  
        `int proyId = entrada.nextInt();`  
        `entrada.nextLine();`  
        `System.out.print("Incremento salarial: ");`  
        `double incremento = entrada.nextDouble();`  
        `entrada.nextLine();`  
  
        `try (Connection con = ConexionBD.getConexion()) {`  
            `con.setAutoCommit(false); // Transacción manual`  
            `try (`  
                    `PreparedStatement psSalario = con.prepareStatement("UPDATE empleados SET salario = salario + ? WHERE id = ?");`  
                    `PreparedStatement psPresupuesto = con.prepareStatement("UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id = ?")`  
            `) {`  
                `psSalario.setDouble(1, incremento);`  
                `psSalario.setInt(2, empId);`  
                `psSalario.executeUpdate();`  
  
                `psPresupuesto.setDouble(1, incremento);`  
                `psPresupuesto.setInt(2, proyId);`  
                `psPresupuesto.executeUpdate();`  
  
                `con.commit();`  
                `System.out.println("Transacción: salario incrementado y presupuesto descontado.");`  
            `} catch (Exception ex) {`  
                `con.rollback();`  
                `System.out.println("Error: rollback realizado.");`  
            `}`  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
        `} finally {`  
            `ConexionBD.cerrar();`  
        `}`  
    `}`  
  
    `public void mostrarTablas() {`  
        `try (Connection con = ConexionBD.getConexion();`  
             `Statement st = con.createStatement();`  
             `ResultSet rsEmp = st.executeQuery("SELECT * FROM empleados")) {`  
            `System.out.println("\nEmpleados:");`  
            `while (rsEmp.next()) {`  
                `System.out.printf("ID: %d, Nombre: %s, Salario: %.2f%n",`  
                        `rsEmp.getInt("id"),`  
                        `rsEmp.getString("nombre"),`  
                        `rsEmp.getDouble("salario"));`  
            `}`  
        `} catch (Exception e) { e.printStackTrace(); }`  
        `finally { ConexionBD.cerrar(); }`  
  
        `try (Connection con = ConexionBD.getConexion();`  
             `Statement st = con.createStatement();`  
             `ResultSet rsProy = st.executeQuery("SELECT * FROM proyectos")) {`  
            `System.out.println("\nProyectos:");`  
            `while (rsProy.next()) {`  
                `System.out.printf("ID: %d, Nombre: %s, Presupuesto: %.2f%n",`  
                        `rsProy.getInt("id"),`  
                        `rsProy.getString("nombre"),`  
                        `rsProy.getDouble("presupuesto"));`  
            `}`  
        `} catch (Exception e) { e.printStackTrace(); }`  
        `finally { ConexionBD.cerrar(); }`  
  
        `try (Connection con = ConexionBD.getConexion();`  
             `Statement st = con.createStatement();`  
             `ResultSet rsAsig = st.executeQuery("SELECT * FROM asignaciones")) {`  
            `System.out.println("\nAsignaciones:");`  
            `while (rsAsig.next()) {`  
                `System.out.printf("ID: %d, EmpleadoID: %d, ProyectoID: %d%n",`  
                        `rsAsig.getInt("id"),`  
                        `rsAsig.getInt("empleado_id"),`  
                        `rsAsig.getInt("proyecto_id"));`  
            `}`  
        `} catch (Exception e) { e.printStackTrace(); }`  
        `finally { ConexionBD.cerrar(); }`  
    `}`  
  
    `public void menu() {`  
        `Scanner entrada = new Scanner(System.in);`  
        `int opcion = -1;`  
        `while (opcion != 0) {`  
            `System.out.println("\n---- Menú Empresa ----");`  
            `System.out.println("1. Insertar empleado");`  
            `System.out.println("2. Insertar proyecto");`  
            `System.out.println("3. Asignar empleado a proyecto");`  
            `System.out.println("4. Incrementar salario y descontar presupuesto");`  
            `System.out.println("5. Mostrar estado final de las tablas");`  
            `System.out.println("0. Salir");`  
            `System.out.print("Elige opción: ");`  
            `if (entrada.hasNextInt()) {`  
                `opcion = entrada.nextInt();`  
                `entrada.nextLine();`  
            `} else {`  
                `System.out.println("Introduce un número válido.");`  
                `entrada.nextLine();`  
                `continue;`  
            `}`  
            `switch (opcion) {`  
                `case 1:`  
                    `insertarEmpleado();`  
                    `break;`  
                `case 2:`  
                    `insertarProyecto();`  
                    `break;`  
                `case 3:`  
                    `asignarEmpleadoAProyecto();`  
                    `break;`  
                `case 4:`  
                    `incrementarSalarioYDescontarPresupuesto();`  
                    `break;`  
                `case 5:`  
                    `mostrarTablas();`  
                    `break;`  
                `case 0:`  
                    `System.out.println("Programa finalizado.");`  
                    `break;`  
                `default:`  
                    `System.out.println("Opción no válida.");`  
            `}`  
        `}`  
        `entrada.close();`  
    `}`  
`}`

`**Main.java:**`

`package org.example;`  
  
`public class Main {`  
    `public static void main(String[] args) {`  
  
        `EmpresaDAO dao = new EmpresaDAO();`  
        `dao.menu();`  
    `}`  
`}`

Resultado:

![[Pasted image 20251116192248.png]]

![[Pasted image 20251116192301.png]]
![[Pasted image 20251116192315.png]]
![[Pasted image 20251116192358.png]]
