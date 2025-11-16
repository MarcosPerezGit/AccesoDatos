# Tareas

1. Conéctate a la base de datos empresa .

2. Ejecuta una consulta SELECT * FROM empleados .

3. Muestra por consola los datos de todos los empleados.

4. Usa ResultSetMetaData para imprimir:

	Numero de columnas. 
	Nombre y tipo de cada columna.

5. Usa DatabaseMetaData para imprimir:

Nombre del producto de base de datos.

Versión del motor.

Nombre del driver JDBC.

`**EmpresaDAO.java:**`

`package org.example;`  
  
`import conexion.ConexionBD;`  
  
`import java.sql.*;`  
`import java.util.Scanner;`  
  
`public class EmpresaDAO {`  
  
    `//Ejemplo de ejercicio sin menu y mostrando todo`  
  
`/*    public void listarEmpleados() {`  
  
        `// try-with-resources: abre la conexión, el Statement y el ResultSet; se cerrarán automáticamente        try (Connection con = ConexionBD.getConexion(); // obtiene una conexión a la BD             Statement st = con.createStatement(); // crea un Statement para ejecutar consultas             ResultSet rs = st.executeQuery("SELECT * FROM empleados")) { // ejecuta la consulta y obtiene los resultados`  
            `// DatabaseMetaData: información sobre la base de datos y el driver            System.out.println("USO DE DATABASEMETADATA");            DatabaseMetaData dbmeta = con.getMetaData();            System.out.println("Nombre Base de Datos: " + dbmeta.getDatabaseProductName());            System.out.println("Version del Motor: " + dbmeta.getDatabaseProductVersion());            System.out.println("Nombre del Driver: " + dbmeta.getDriverName());`  
            `// ResultsetMetaData: información sobre las columnas del resultado`  
            `System.out.println("USO DE RESUTLSETMETADATA");            ResultSetMetaData rsmd = rs.getMetaData();            int numeroColumnas = rsmd.getColumnCount();            System.out.println("Número de columnas " + numeroColumnas);            for (int i = 1; i <= numeroColumnas; i++) {                System.out.println("Nombre de la columna " + i + ": " + rsmd.getColumnName(i) + " - Tipo de dato: " + rsmd.getColumnTypeName(i));            }`  
            `// Itera sobre cada fila del ResultSet            while (rs.next()) {                // Imprime los valores de las columnas 'id', 'nombre' y 'salario' formateados                System.out.printf("ID: %d - Nombre: %s - Salario: %.2f%n",                        rs.getInt("id"), // obtiene la columna 'id' como entero                        rs.getString("nombre"), // obtiene la columna 'nombre' como cadena                        rs.getDouble("salario")); // obtiene la columna 'salario' como double            }`  
        `} catch (Exception e) { // captura cualquier excepción (p. ej. SQLException)            e.printStackTrace(); // imprime la traza de la excepción        } finally {            // Llamada final a la utilidad de cierre (mantiene la lógica original)            ConexionBD.cerrar();        }    }*/`  
    `public void listarEmpleados() {`  
        `try (Connection con = ConexionBD.getConexion();`  
             `Statement st = con.createStatement();`  
             `ResultSet rs = st.executeQuery("SELECT * FROM empleados")) {`  
  
            `while (rs.next()) {`  
                `System.out.printf("ID: %d - Nombre: %s - Salario: %.2f%n",`  
                        `rs.getInt("id"),`  
                        `rs.getString("nombre"),`  
                        `rs.getDouble("salario"));`  
            `}`  
  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
        `} finally {`  
            `ConexionBD.cerrar();`  
        `}`  
    `}`  
  
    `public void mostrarMetaDatosEmpleados() {`  
        `try (Connection con = ConexionBD.getConexion();`  
             `Statement st = con.createStatement();`  
             `ResultSet rs = st.executeQuery("SELECT * FROM empleados")) {`  
  
            `System.out.println("USO DE RESULTSETMETADATA");`  
            `ResultSetMetaData rsmd = rs.getMetaData();`  
            `int numeroColumnas = rsmd.getColumnCount();`  
            `System.out.println("Número de columnas: " + numeroColumnas);`  
            `for (int i = 1; i <= numeroColumnas; i++) {`  
                `System.out.println("Nombre de la columna " + i + ": " + rsmd.getColumnName(i) + " - Tipo de dato: " + rsmd.getColumnTypeName(i));`  
            `}`  
  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
        `} finally {`  
            `ConexionBD.cerrar();`  
        `}`  
    `}`  
  
    `public void mostrarInformacionBD() {`  
        `try (Connection con = ConexionBD.getConexion()) {`  
            `System.out.println("USO DE DATABASEMETADATA");`  
            `DatabaseMetaData dbmeta = con.getMetaData();`  
            `System.out.println("Nombre Base de Datos: " + dbmeta.getDatabaseProductName());`  
            `System.out.println("Version del Motor: " + dbmeta.getDatabaseProductVersion());`  
            `System.out.println("Nombre del Driver: " + dbmeta.getDriverName());`  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
        `} finally {`  
            `ConexionBD.cerrar();`  
        `}`  
    `}`  
  
    `public void menu() {`  
        `Scanner entrada = new Scanner(System.in);`  
        `int opcion = -1;`  
        `while (opcion != 0) {`  
            `System.out.println("\n---- Menú ----");`  
            `System.out.println("1. Listar Empleados");`  
            `System.out.println("2. Recuperar Metadatos de Empleados");`  
            `System.out.println("3. Mostrar Información de la Base de Datos");`  
            `System.out.println("0. Salir");`  
            `System.out.print("Elige opción: ");`  
            `if (entrada.hasNextInt()) {`  
                `opcion = entrada.nextInt();`  
                `entrada.nextLine(); // limpiamos buffer`  
            `} else {`  
                `System.out.println("Introduce un número válido.");`  
                `entrada.nextLine(); // limpiamos buffer`  
                `continue;`  
            `}`  
            `switch (opcion) {`  
                `case 1:`  
                    `listarEmpleados();`  
                    `break;`  
                `case 2:`  
                    `mostrarMetaDatosEmpleados();`  
                    `break;`  
                `case 3:`  
                    `mostrarInformacionBD();`  
                    `break;`  
                `case 0:`  
                    `System.out.println("Se ha cerrado la aplicacion");`  
                    `break;`  
                `default:`  
                    `System.out.println("Opcion no valida");`  
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

**Resultado:**

![[Pasted image 20251116190702.png]]

