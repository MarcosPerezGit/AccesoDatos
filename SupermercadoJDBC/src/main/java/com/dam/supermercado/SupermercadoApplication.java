package com.dam.supermercado;

import com.dam.supermercado.model.Producto;
import com.dam.supermercado.service.ProductoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * ============================================
 * CLASE PRINCIPAL: SupermercadoApplication
 * ============================================
 * Versión con JDBC tradicional (sin Hibernate/JPA)
 *
 * COMPARACIÓN DE ENFOQUES:
 * ───────────────────────────────────────────────────────────────
 * │ Aspecto              │ JDBC Tradicional    │ JPA/Hibernate   │
 * ├───────────────────────────────────────────────────────────────┤
 * │ SQL                  │ Manual              │ Automático      │
 * │ Mapeo                │ RowMapper manual    │ @Entity/@Column │
 * │ Repositorio          │ Clase con métodos   │ Interface       │
 * │ Código necesario     │ Más                 │ Menos           │
 * │ Control              │ Total               │ Parcial         │
 * │ Curva aprendizaje    │ Más simple          │ Más compleja    │
 * │ Productividad        │ Menor               │ Mayor           │
 * ───────────────────────────────────────────────────────────────
 *
 * Esta versión te permite entender qué hace Hibernate "por debajo".
 *
 * @author Profesor DAM
 * @version 1.0 - Versión JDBC tradicional
 */
@SpringBootApplication
public class SupermercadoApplication {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║     APLICACIÓN SUPERMERCADO - SPRING BOOT + JDBC                 ║");
        System.out.println("║     Versión: JDBC Tradicional (SIN Hibernate/JPA)                ║");
        System.out.println("║     Módulo: 0486 - Acceso a Datos - 2º DAM                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ NOTA EDUCATIVA:                                                  │");
        System.out.println("│ Esta versión usa JdbcTemplate de Spring para ejecutar SQL.       │");
        System.out.println("│ NO hay Hibernate, NO hay JPA, NO hay ORM.                        │");
        System.out.println("│ Las consultas SQL se escriben manualmente en el repositorio.     │");
        System.out.println("│ El mapeo ResultSet -> Objeto se hace con RowMapper.              │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        System.out.println();

        // =====================================================
        // PASO 1: Iniciar Spring Boot y obtener el contexto
        // =====================================================
        System.out.println(">>> Iniciando aplicación Spring Boot...\n");

        ApplicationContext contexto = SpringApplication.run(SupermercadoApplication.class, args);

        System.out.println("\n>>> Contexto de Spring inicializado correctamente.");
        System.out.println(">>> Total de beans registrados: " + contexto.getBeanDefinitionCount());

        // =====================================================
        // PASO 2: Obtener el Bean de ProductoService
        // =====================================================
        System.out.println("\n>>> Obteniendo el bean ProductoService del contexto...");

        ProductoService productoService = contexto.getBean(ProductoService.class);

        System.out.println(">>> Bean ProductoService obtenido correctamente.");

        // =====================================================
        // PASO 3: Usar el servicio para obtener los productos
        // =====================================================
        System.out.println("\n>>> Consultando productos en la base de datos...");
        System.out.println("═".repeat(65));

        List<Producto> productos = productoService.obtenerTodosLosProductos();

        // =====================================================
        // PASO 4: Mostrar los productos obtenidos
        // =====================================================
        System.out.println("\n╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LISTADO DE PRODUCTOS                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");

        if (productos.isEmpty()) {
            System.out.println("\n⚠️  No se encontraron productos en la base de datos.");
            System.out.println("    Ejecuta el script SQL para insertar datos de ejemplo.");
        } else {
            System.out.println("\nTotal de productos encontrados: " + productos.size());
            System.out.println("-".repeat(65));

            // Mostrar cada producto
            for (Producto producto : productos) {
                System.out.println(producto);
            }

            System.out.println("-".repeat(65));

            // =====================================================
            // DEMOSTRACIÓN: Otras consultas del servicio
            // =====================================================
            System.out.println("\n>>> Demostrando otras funcionalidades del servicio...\n");

            // Obtener categorías
            System.out.println("📁 Categorías disponibles:");
            List<String> categorias = productoService.obtenerCategorias();
            categorias.forEach(cat -> System.out.println("   • " + cat));

            // Buscar por categoría
            System.out.println("\n🔍 Productos de la categoría 'Lácteos':");
            List<Producto> lacteos = productoService.obtenerProductosPorCategoria("Lácteos");
            lacteos.forEach(p -> System.out.println("   • " + p.getNombre() + " - " + p.getPrecio() + "€"));

            // Contar productos
            long totalProductos = productoService.contarProductos();
            System.out.println("\n📊 Estadísticas:");
            System.out.println("   • Total de productos en BD: " + totalProductos);
        }

        // =====================================================
        // Mensaje final comparativo
        // =====================================================
        System.out.println("\n═".repeat(65));
        System.out.println(">>> Aplicación finalizada correctamente.");
        System.out.println();
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.println("│ 💡 RECUERDA:                                                     │");
        System.out.println("│    En esta versión JDBC, todo el SQL está en ProductoRepository  │");
        System.out.println("│    Compara con la versión JPA donde el SQL es automático.        │");
        System.out.println("│    ¡Hibernate genera por ti todo lo que aquí escribimos manual!  │");
        System.out.println("└──────────────────────────────────────────────────────────────────┘");
        System.out.println("═".repeat(65));
    }
}