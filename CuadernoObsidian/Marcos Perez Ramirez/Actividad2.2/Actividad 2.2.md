# Objetivo

Practicar la configuración y el uso de un **pool de conexiones** en Java utilizando la librería **HikariCP**.

`**pom.xml:**`
`<project xmlns="http://maven.apache.org/POM/4.0.0"`  
         `xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"`  
         `xsi:schemaLocation="http://maven.apache.org/POM/4.0.0`  
         `http://maven.apache.org/maven-v4_0_0.xsd">`  
  
    `<modelVersion>4.0.0</modelVersion>`  
    `<groupId>org.example</groupId>`  
    `<artifactId>Actividad2.2_MarcosPerezRamirez</artifactId>`  
    `<version>1.0-SNAPSHOT</version>`  
    `<properties>`  
        `<maven.compiler.source>21</maven.compiler.source>`  
        `<maven.compiler.target>21</maven.compiler.target>`  
    `</properties>`  
  
    `<dependencies>`  
        `<!-- Dependencia del conector JDBC de MySQL -->`  
        `<dependency>`  
            `<groupId>mysql</groupId>`  
            `<artifactId>mysql-connector-java</artifactId>`  
            `<version>8.0.33</version>`  
        `</dependency>`  
  
        `<!-- Dependencia de HikariCP para el pool de conexiones -->`  
        `<dependency>`  
            `<groupId>com.zaxxer</groupId>`  
            `<artifactId>HikariCP</artifactId>`  
            `<version>5.1.0</version>`  
        `</dependency>`  
  
        `<!-- SLF4J: logging comúnmente requerido por HikariCP -->`  
        `<dependency>`  
            `<groupId>org.slf4j</groupId>`  
            `<artifactId>slf4j-api</artifactId>`  
            `<version>1.7.36</version>`  
        `</dependency>`  
        `<!-- SLF4J Simple para salida por consola (puedes elegir otro logger si lo prefieres) -->`  
    `</dependencies>`  
`</project>`

`**Main.java:**`

`package org.example;`  
  
`import conexion.ConexionBD;`  
  
`import java.sql.Connection;`  
`import java.sql.ResultSet;`  
`import java.sql.Statement;`  
  
`public class Main {`  
    `public static void main(String[] args) {`  
        `// Obtener y usar 3 conexiones distintas`  
        `for (int i = 1; i <= 3; i++) {`  
            `try (Connection con = ConexionBD.getConexion();`  
                 `Statement st = con.createStatement();`  
                 `ResultSet rs = st.executeQuery("SELECT nombre FROM empleados")) {`  
  
                `System.out.println("Conexión " + i + ":");`  
                `while (rs.next()) {`  
                    `System.out.println("- " + rs.getString("nombre"));`  
                `}`  
                `System.out.println("----------------------");`  
            `} catch (Exception e) {`  
                `e.printStackTrace();`  
            `}`  
        `}`  
        `// Las conexiones se devuelven automáticamente al pool por try-with-resources`  
    `}`  
`}`

**Resultado:**

![[Pasted image 20251116185723.png]]