package config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilidad para gestionar un DataSource basado en HikariCP mediante inicialización
 * estática. Esta clase expone métodos estáticos para obtener una Connection y para
 * cerrar el pool cuando la aplicación deja de necesitarlo.
 * <p>
 * Propósito académico:
 * - Mostrar cómo inicializar un pool de conexiones al cargar la clase.
 * - Explicar la lectura de parámetros desde un fichero de propiedades.
 * - Enseñar la importancia de cerrar el pool al finalizar la aplicación.
 * <p>
 * Observaciones didácticas:
 * - La inicialización se realiza en un bloque static para disponer del pool desde
 * el arranque de la aplicación.
 * - En entornos productivos, el fichero de propiedades no debe contener credenciales
 * en texto plano y su carga debe incluir comprobaciones más exhaustivas.
 */
public class DatabaseConfig {
    // DataSource compartido por toda la aplicación
    private static HikariDataSource dataSource;

    /*
     * Bloque de inicialización estático:
     * Se ejecuta al cargar la clase y configura el pool leyendo propiedades desde
     * el recurso 'db.properties' situado en el classpath.
     *
     * Nota académica: el uso de un bloque estático simplifica el ejemplo; en aplicaciones
     * complejas la configuración del pool suele centralizarse en un módulo de inicialización
     * o en la DI del contenedor.
     */
    static {
        try {
            Properties props = new Properties();

            // try-with-resources para cerrar automáticamente el InputStream
            // Se utiliza ConexionBD.class.getClassLoader() para obtener el classloader.
            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
                // Carga de las propiedades desde el fichero de configuración.
                // Se espera que 'db.url', 'db.user' y 'db.password' estén presentes.
                props.load(input);
            }

            // Configuración mínima de HikariCP. Se explican los parámetros relevantes.
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));           // URL JDBC, p.ej. jdbc:mysql://host:3306/schema
            config.setUsername(props.getProperty("db.user"));         // Usuario de la BD
            config.setPassword(props.getProperty("db.password"));     // Contraseña de la BD

            // Parámetros de tuning básicos (ejemplo didáctico)
            config.setMaximumPoolSize(5);     // Número máximo de conexiones activas en el pool
            config.setMinimumIdle(2);        // Número mínimo de conexiones inactivas a mantener
            config.setIdleTimeout(10000);    // Tiempo (ms) tras el cual una conexión inactiva puede cerrarse
            config.setConnectionTimeout(10000); // Tiempo (ms) máximo a esperar por una conexión libre
            config.setPoolName("DAMPool");   // Nombre del pool para facilitar diagnóstico

            // Creación del DataSource (pool) a partir de la configuración anterior.
            dataSource = new HikariDataSource(config);
            System.out.println("Pool de conexiones HikariCP inicializado correctamente.");
        } catch (Exception e) {
            /*
             * En este ejemplo didáctico se lanza una RuntimeException para detener el
             * arranque si el pool no puede inicializarse. En entornos productivos se
             * recomienda un manejo más fino (log, métricas, reintento, fallback).
             */
            throw new RuntimeException("Error al inicializar el pool de conexiones", e);
        }
    }

    /**
     * Devuelve una Connection obtenida del pool.
     * <p>
     * Comportamiento pedagógico:
     * - La Connection debe cerrarse por el consumidor cuando deje de usarse.
     * - En un pool, Connection.close() no cierra la conexión física, sino que la devuelve al pool.
     *
     * @return Connection del pool HikariCP
     * @throws SQLException si no es posible obtener la conexión
     */
    public static Connection getConexion() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Cierra el HikariDataSource liberando recursos asociados (conexiones físicas, hilos, ...).
     * <p>
     * Uso recomendado:
     * - Invocar al finalizar la aplicación, por ejemplo en un shutdown hook o en el lifecycle
     * del contenedor que despliegue la aplicación.
     */
    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones cerrado.");
        }
    }
}
