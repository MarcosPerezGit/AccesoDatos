package com.dam.supermercado.model;

import java.math.BigDecimal;

/**
 * ============================================
 * MODELO: Producto (POJO)
 * ============================================
 * Clase simple que representa un producto del supermercado.
 *
 * DIFERENCIA CON LA VERSIÓN JPA:
 * - NO tiene anotación @Entity
 * - NO tiene anotación @Table
 * - NO tiene anotaciones @Column
 * - NO tiene @Id ni @GeneratedValue
 *
 * Es un POJO (Plain Old Java Object) puro.
 * El mapeo entre la BD y este objeto se hace MANUALMENTE
 * en el repositorio usando RowMapper.
 *
 * Esta es la forma tradicional de trabajar con JDBC,
 * antes de que existieran los frameworks ORM como Hibernate.
 *
 * @author Profesor DAM
 * @version 1.0 - Versión JDBC tradicional
 */
public class Producto {

    // ========================================
    // ATRIBUTOS
    // ========================================
    // Son campos normales de Java, sin anotaciones
    // El mapeo a columnas de BD se hace en el repositorio

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String categoria;

    // ========================================
    // CONSTRUCTORES
    // ========================================

    /**
     * Constructor vacío.
     * Necesario para crear objetos y luego usar los setters.
     */
    public Producto() {
    }

    /**
     * Constructor con todos los campos (excepto ID).
     * Útil para crear nuevos productos antes de insertarlos.
     *
     * @param nombre      Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio      Precio del producto
     * @param stock       Cantidad en stock
     * @param categoria   Categoría del producto
     */
    public Producto(String nombre, String descripcion, BigDecimal precio, Integer stock, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    /**
     * Constructor completo con ID.
     * Útil cuando recuperamos datos de la BD.
     *
     * @param id          ID del producto
     * @param nombre      Nombre del producto
     * @param descripcion Descripción del producto
     * @param precio      Precio del producto
     * @param stock       Cantidad en stock
     * @param categoria   Categoría del producto
     */
    public Producto(Long id, String nombre, String descripcion, BigDecimal precio, Integer stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    // ========================================
    // GETTERS Y SETTERS
    // ========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    // ========================================
    // MÉTODO toString()
    // ========================================

    @Override
    public String toString() {
        return String.format(
                "Producto [ID=%d, Nombre='%s', Precio=%s€, Stock=%d, Categoría='%s']",
                id, nombre, precio, stock, categoria
        );
    }
}
