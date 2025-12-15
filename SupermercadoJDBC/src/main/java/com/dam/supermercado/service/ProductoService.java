package com.dam.supermercado.service;

import com.dam.supermercado.model.Producto;
import com.dam.supermercado.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ============================================
 * SERVICIO: ProductoService (JDBC)
 * ============================================
 * Clase de servicio que contiene la lógica de negocio
 * para gestionar productos del supermercado.
 *
 * Esta clase es prácticamente igual que en la versión JPA.
 * La diferencia está en el REPOSITORIO que usa:
 * - Versión JPA: El repositorio hereda de JpaRepository
 * - Versión JDBC: El repositorio usa JdbcTemplate manualmente
 *
 * El servicio actúa como intermediario y no le importa
 * cómo el repositorio accede a los datos (principio de abstracción).
 *
 * @author Profesor DAM
 * @version 1.0 - Versión JDBC tradicional
 */
@Service
public class ProductoService {

    // ========================================
    // INYECCIÓN DE DEPENDENCIAS
    // ========================================

    private final ProductoRepository productoRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param productoRepository Repositorio JDBC de productos
     */
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ========================================
    // MÉTODOS CRUD BÁSICOS
    // ========================================

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * @return List<Producto> Lista con todos los productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        System.out.println("\n[SERVICE] Obteniendo todos los productos de la BD...");
        System.out.println("[SERVICE] Ejecutando: SELECT * FROM productos");

        List<Producto> productos = productoRepository.findAll();

        System.out.println("[SERVICE] Se encontraron " + productos.size() + " productos.");
        return productos;
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id ID del producto a buscar
     * @return Optional<Producto> Producto encontrado o vacío si no existe
     */
    public Optional<Producto> obtenerProductoPorId(Long id) {
        System.out.println("\n[SERVICE] Buscando producto con ID: " + id);
        System.out.println("[SERVICE] Ejecutando: SELECT * FROM productos WHERE id = " + id);
        return productoRepository.findById(id);
    }

    /**
     * Guarda un nuevo producto.
     *
     * @param producto Producto a guardar
     * @return Producto guardado con ID asignado
     */
    public Producto guardarProducto(Producto producto) {
        System.out.println("\n[SERVICE] Guardando producto: " + producto.getNombre());
        System.out.println("[SERVICE] Ejecutando: INSERT INTO productos...");
        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto Producto con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarProducto(Producto producto) {
        System.out.println("\n[SERVICE] Actualizando producto ID: " + producto.getId());
        int filasAfectadas = productoRepository.update(producto);
        return filasAfectadas > 0;
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarProducto(Long id) {
        System.out.println("\n[SERVICE] Eliminando producto con ID: " + id);
        System.out.println("[SERVICE] Ejecutando: DELETE FROM productos WHERE id = " + id);
        int filasAfectadas = productoRepository.deleteById(id);
        return filasAfectadas > 0;
    }

    // ========================================
    // MÉTODOS DE CONSULTA ADICIONALES
    // ========================================

    /**
     * Obtiene productos por categoría.
     *
     * @param categoria Categoría a buscar
     * @return Lista de productos de esa categoría
     */
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        System.out.println("\n[SERVICE] Buscando productos de categoría: " + categoria);
        System.out.println("[SERVICE] Ejecutando: SELECT * FROM productos WHERE categoria = '" + categoria + "'");
        return productoRepository.findByCategoria(categoria);
    }

    /**
     * Busca productos por nombre (búsqueda parcial).
     *
     * @param nombre Texto a buscar en el nombre
     * @return Lista de productos que coinciden
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        System.out.println("\n[SERVICE] Buscando productos con nombre que contenga: " + nombre);
        return productoRepository.findByNombreContaining(nombre);
    }

    /**
     * Obtiene productos con stock disponible.
     *
     * @return Lista de productos con stock > 0
     */
    public List<Producto> obtenerProductosDisponibles() {
        System.out.println("\n[SERVICE] Buscando productos con stock disponible...");
        return productoRepository.findByStockGreaterThanZero();
    }

    /**
     * Obtiene productos por debajo de un precio máximo.
     *
     * @param precioMaximo Precio máximo
     * @return Lista de productos
     */
    public List<Producto> obtenerProductosPorPrecioMaximo(BigDecimal precioMaximo) {
        System.out.println("\n[SERVICE] Buscando productos con precio <= " + precioMaximo);
        return productoRepository.findByPrecioLessThanEqual(precioMaximo);
    }

    /**
     * Cuenta el total de productos en la base de datos.
     *
     * @return Número total de productos
     */
    public long contarProductos() {
        return productoRepository.count();
    }

    /**
     * Obtiene todas las categorías disponibles.
     *
     * @return Lista de categorías únicas
     */
    public List<String> obtenerCategorias() {
        System.out.println("\n[SERVICE] Obteniendo lista de categorías...");
        System.out.println("[SERVICE] Ejecutando: SELECT DISTINCT categoria FROM productos");
        return productoRepository.findAllCategorias();
    }
}
