Crear BD empresa.

`DROP DATABASE IF exists empresa;`
`CREATE DATABASE IF NOT EXISTS empresa;`
`USE empresa;`

`CREATE TABLE empleados (`
                           `id INT AUTO_INCREMENT PRIMARY KEY,`
                           `nombre VARCHAR(50),`
                           `salario DOUBLE`
`);`

`/* CREATE TABLE cuentas (`
                         `id INT AUTO_INCREMENT PRIMARY KEY,`
                         `saldo DOUBLE`
`);`
`*/`

`CREATE TABLE cuentas (`
`id INT AUTO_INCREMENT PRIMARY KEY,`
`titular VARCHAR(100),`
`saldo DECIMAL(10,2)`
`);`

`INSERT INTO cuentas (titular, saldo) VALUES`
`('Ana', 2000.00),`
`('Luis', 1500.00);`

`CREATE TABLE proyectos (`
                         `id INT AUTO_INCREMENT PRIMARY KEY,`
                         `nombre VARCHAR (100),`
                         `presupuesto DECIMAL (10,2)`
`);`
`INSERT INTO empleados (nombre, salario) VALUES ('Ana', 24000), ('Luis', 28000);`
`INSERT INTO cuentas (saldo) VALUES (1000), (2000);`

`CREATE TABLE logs (`
    `id INT AUTO_INCREMENT PRIMARY KEY,`
    `mensaje VARCHAR(255),`
    `momento TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
`);`

`DELIMITER //`
`CREATE PROCEDURE obtener_empleado(IN empId INT)`
`BEGIN`
`SELECT id, nombre, salario FROM empleados WHERE id = empId;`
`END //`
`DELIMITER ;`

`SELECT * FROM empleados;`
`SELECT * FROM cuentas;`
`SELECT * FROM logs;`

`DELIMITER //`
`CREATE PROCEDURE incrementar_salario(`
    `IN empId INT,`
    `IN incremento DOUBLE,`
    `OUT nuevoSalario DOUBLE`
`)`
`BEGIN`

    `UPDATE empleados`
    `SET salario = salario + incremento`
    `WHERE id = empId;`

    `SELECT salario INTO nuevoSalario`
    `FROM empleados`
    `WHERE id = empId;`
`END //`
`DELIMITER ;`


Usar Statement para listar empleados.

Usar PreparedStatement para buscar empleados por id.

Crear procedimiento obtener_empleado y llamarlo con CallableStatement .


`**ConexionBD.java:**`

package conexion;  
  
import java.io.InputStream;  
import java.sql.*;  
import java.util.Properties;  
  
/**  
 * Clase de utilidad para obtener y cerrar una única instancia de {@link Connection}.  
 * <p>  
 * Propósito académico:  
 * - Mostrar la obtención de una conexión JDBC usando {@link DriverManager} y un  
 * fichero de propiedades situado en el classpath (\`db.properties\`). * - Ilustrar el uso de try-with-resources para recursos IO (\`InputStream\`). * - Explicar el ciclo de vida básico de una Connection en un ejemplo sencillo. * <p>  
 * Observación didáctica importante:  
 * - Este ejemplo mantiene una única conexión estática para conservar la lógica * original. En aplicaciones concurrentes o productivas se recomienda usar un * pool de conexiones (p. ej. HikariCP) y evitar compartir una Connection estática. */public class ConexionBD {  
    // Conexión compartida por la clase; se inicializa perezosamente en getConexion().  
    private static Connection conexion = null;  
  
    /**  
     * Devuelve la conexión a la base de datos.     * <p>  
     * Flujo:  
     * 1. Si la conexión es nula o está cerrada, lee las propiedades desde     * el recurso \`db.properties\` del classpath y crea una nueva Connection     * mediante DriverManager.getConnection(...).     * 2. Si la conexión ya existe y está abierta, simplemente la devuelve.     * <p>  
     * Nota didáctica:  
     * - Se lanza \`Exception\` para simplificar el ejemplo y centrarse en la     * mecánica de obtención de la conexión. En código de producción conviene     * usar excepciones más específicas y un manejo robusto.     *     * @return instancia de {@link Connection}  
     * @throws Exception si ocurre algún error al leer la configuración o abrir la conexión  
     */    public static Connection getConexion() throws Exception {  
        // Comprueba si la conexión necesita ser creada o reestablecida.  
        if (conexion == null || conexion.isClosed()) {  
            Properties props = new Properties();  
  
            /*  
             * Lectura del fichero de configuración desde el classpath.             * try-with-resources garantiza el cierre del InputStream aunque se produzca             * una excepción durante la carga de propiedades.             *             * Observación: si el recurso no existe, getResourceAsStream(...) devuelve null;             * en este ejemplo se mantiene la lógica original (no se añade comprobación).             */            try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {  
                props.load(input);  
            }  
  
            // Obtención de la Connection usando los valores leídos del fichero de propiedades.  
            conexion = DriverManager.getConnection(  
                    props.getProperty("db.url"),  
                    props.getProperty("db.user"),  
                    props.getProperty("db.password"));  
            System.out.println("Conexión establecida con la BD");  
        }  
        return conexion;  
    }  
  
    /**  
     * Cierra la conexión si está abierta.     * <p>  
     * Buenas prácticas:  
     * - Proteger el cierre con un bloque try/catch para evitar que excepciones al     * cerrar la conexión propaguen errores no controlados.     * - En aplicaciones con pool de conexiones, cerrar la Connection suele devolverla     * al pool en lugar de cerrarla físicamente.     */    public static void cerrar() {  
        try {  
            if (conexion != null && !conexion.isClosed()) conexion.close();  
        } catch (SQLException e) {  
            // En este ejemplo docente imprimimos la traza; en producción usar un logger.  
            e.printStackTrace();  
        }  
    }  
}

`**pom.xml:**`
`<project xmlns="http://maven.apache.org/POM/4.0.0"`  
         `xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"`  
         `xsi:schemaLocation="http://maven.apache.org/POM/4.0.0`  
         `http://maven.apache.org/maven-v4_0_0.xsd">`  
  
    `<modelVersion>4.0.0</modelVersion>`  
  
    `<groupId>com.campusfp</groupId>`  
    `<artifactId>MiPrimerProyectoMaven</artifactId>`  
    `<version>1.0-SNAPSHOT</version>`  
  
    `<properties>`  
        `<maven.compiler.source>17</maven.compiler.source>`  
        `<maven.compiler.target>17</maven.compiler.target>`  
    `</properties>`  
  
    `<dependencies>`  
        `<!-- Driver MySQL -->`  
        `<dependency>`  
            `<groupId>mysql</groupId>`  
            `<artifactId>mysql-connector-java</artifactId>`  
            `<version>8.0.33</version>`  
        `</dependency>`  
    `</dependencies>`  
  
`</project>`

`**EmpleadosDAO:**`

`package org.example;`  
  
`import conexion.ConexionBD;`  
  
`import java.sql.*;`  
  
`public class EmpleadosDAO {`  
  
    `public void listarEmpleados() {`  
  
        `// try-with-resources: abre la conexión, el Statement y el ResultSet; se cerrarán automáticamente`  
        `try (Connection con = ConexionBD.getConexion(); // obtiene una conexión a la BD`  
             `Statement st = con.createStatement(); // crea un Statement para ejecutar consultas`  
             `ResultSet rs = st.executeQuery("SELECT id, nombre, salario FROM empleados")) { // ejecuta la consulta y obtiene los resultados`  
  
            `// Itera sobre cada fila del ResultSet            while (rs.next()) {`  
                `// Imprime los valores de las columnas 'id', 'nombre' y 'salario' formateados`  
                `System.out.printf("ID: %d - Nombre: %s - Salario: %.2f%n",`  
                        `rs.getInt("id"), // obtiene la columna 'id' como entero`  
                        `rs.getString("nombre"), // obtiene la columna 'nombre' como cadena`  
                        `rs.getDouble("salario")); // obtiene la columna 'salario' como double`  
            `}`  
  
        `} catch (Exception e) { // captura cualquier excepción (p. ej. SQLException)`  
            `e.printStackTrace(); // imprime la traza de la excepción`  
        `} finally {`  
            `// Llamada final a la utilidad de cierre (mantiene la lógica original)`  
            `ConexionBD.cerrar();`  
        `}`  
    `}`  
  
    `public void buscarEmpleadoPorId(int id) {`  
  
        `try (Connection con = ConexionBD.getConexion()) {`  
            `// Consulta parametrizada: selecciona todas las columnas de la tabla empleados`  
            `// para el empleado cuyo id coincida con el parámetro '?'            String sql = "SELECT * FROM empleados WHERE id = ?";`  
  
            `// Prepara la sentencia SQL con parámetros para evitar inyección y mejorar rendimiento`  
            `PreparedStatement ps = con.prepareStatement(sql);`  
  
            `// Asigna el valor 1 al primer parámetro (índice 1) de la consulta`  
            `ps.setInt(1, id);`  
  
            `// Ejecuta la consulta y obtiene el conjunto de resultados`  
            `// ResultSet en su propio try-with-resources            try (ResultSet rs = ps.executeQuery()) {`  
                `// Si hay una fila en el ResultSet, obtiene y muestra el nombre y salario`  
                `if (rs.next()) {`  
                    `System.out.println("Empleado: " + rs.getString("nombre") + " - Salario: " + rs.getDouble("salario"));`  
                `}`  
            `} // rs se cierra aquí`  
  
            `// Al finalizar el try-with-resources, la conexión se cerrará automáticamente.            // El PreparedStatement y ResultSet deberían cerrarse explícitamente,        } catch (Exception e) { // captura cualquier excepción (p. ej. SQLException u otras)`  
            `e.printStackTrace(); // imprime la traza de la excepción en la salida de errores`  
        `} // rs cerrado aquí`  
    `}`  
  
    `public void obtenerEmpleado(int id) {`  
        `String sql = "{call obtener_empleado(?)}";`  
        `try (Connection con = ConexionBD.getConexion();`  
             `CallableStatement cs = con.prepareCall(sql)) {`  
            `cs.setInt(1, id);`  
  
            `try(ResultSet rs = cs.executeQuery()){`  
                `if (rs.next()){`  
                    `System.out.println("Empleado: " + rs.getString("nombre") + " - Salario: " + rs.getDouble("salario"));`  
  
                `}`  
            `}`  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
    `}`  
    `}`  
`}`

**`Main.java:`**

`package org.example;`  
  
`import conexion.ConexionBD;`  
  
`import java.sql.CallableStatement;`  
`import java.sql.Connection;`  
`import java.sql.ResultSet;`  
`import java.sql.Statement;`  
  
`public class Main {`  
    `public static void main(String[] args) {`  
        `EmpleadosDAO dao = new EmpleadosDAO();`  
  
        `System.out.println("Listado de empleados (Statement):");`  
        `dao.listarEmpleados();`  
  
        `System.out.println("\nBuscar empleado por ID (PreparedStatement):");`  
        `dao.buscarEmpleadoPorId(1);`  
  
        `System.out.println("\nBuscar empleado por procedimiento almacenado (CallableStatement):");`  
        `dao.obtenerEmpleado(2);`  
    `}`  
  
`}`

**Resultado:**

![[Pasted image 20251116185344.png]]