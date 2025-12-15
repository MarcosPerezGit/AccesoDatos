package com.dam.supermercado.repository;

import com.dam.supermercado.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * ============================================
 * REPOSITORIO: ProductoRepository (JDBC)
 * ============================================
 * Clase que proporciona acceso a datos usando JDBC tradicional.
 *
 * DIFERENCIAS CON LA VERSIÓN JPA:
 * ─────────────────────────────────────────────
 * | VERSIÓN JPA/HIBERNATE    | VERSIÓN JDBC     |
 * ├─────────────────────────────────────────────┤
 * | Extiende JpaRepository   | Clase normal     |
 * | Métodos automáticos      | SQL manual       |
 * | @Entity en el modelo     | POJO simple      |
 * | Mapeo automático         | RowMapper manual |
 * | findAll() heredado       | SELECT * escrito |
 * ─────────────────────────────────────────────
 *
 * Esta versión permite ver exactamente qué ocurre "por debajo"
 * cuando usamos frameworks ORM como Hibernate.
 *
 * @author Profesor DAM
 * @version 1.0 - Versión JDBC tradicional
 */
@Repository
public class ProductoRepository {

    // ========================================
    // JDBCTEMPLATE
    // ========================================
    /**
     * JdbcTemplate es una clase de Spring que simplifica
     * el uso de JDBC. Gestiona automáticamente:
     * - Apertura/cierre de conexiones
     * - Creación de Statement/PreparedStatement
     * - Manejo de excepciones
     * - Liberación de recursos
     *
     * Sin JdbcTemplate tendríamos que hacer todo esto manualmente
     * con try-catch-finally y Connection/Statement/ResultSet.
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente el JdbcTemplate configurado.
     *
     * @param jdbcTemplate Template JDBC inyectado por Spring
     */
    @Autowired
    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ========================================
    // ROWMAPPER - MAPEO MANUAL DE RESULTADOS
    // ========================================
    /**
     * RowMapper es una interfaz funcional que define cómo
     * convertir cada fila del ResultSet en un objeto Producto.
     *
     * ESTO ES LO QUE HIBERNATE HACE AUTOMÁTICAMENTE
     * cuando usamos @Entity y @Column.
     *
     * Aquí lo hacemos manualmente para que los alumnos
     * entiendan el proceso de mapeo objeto-relacional.
     */
    private final RowMapper<Producto> productoRowMapper = new RowMapper<Producto>() {
        @Override
        public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Creamos un nuevo objeto Producto
            Producto producto = new Producto();

            // Extraemos cada columna del ResultSet y la asignamos al objeto
            // NOTA: Los nombres de columna deben coincidir con la BD
            producto.setId(rs.getLong("id"));
            producto.setNombre(rs.getString("nombre"));
            producto.setDescripcion(rs.getString("descripcion"));
            producto.setPrecio(rs.getBigDecimal("precio"));
            producto.setStock(rs.getInt("stock"));
            producto.setCategoria(rs.getString("categoria"));

            return producto;
        }
    };

    // ========================================
    // MÉTODOS CRUD
    // ========================================

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * Equivalente en JPA: productoRepository.findAll()
     * Aquí escribimos el SQL manualmente.
     *
     * @return Lista con todos los productos
     */
    public List<Producto> findAll() {
        // SQL escrito manualmente - en JPA esto es automático
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos";

        // query() ejecuta el SELECT y usa el RowMapper para convertir cada fila
        return jdbcTemplate.query(sql, productoRowMapper);
    }

    /**
     * Busca un producto por su ID.
     *
     * Equivalente en JPA: productoRepository.findById(id)
     *
     * @param id ID del producto a buscar
     * @return Optional con el producto o vacío si no existe
     */
    public Optional<Producto> findById(Long id) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos WHERE id = ?";

        // queryForObject espera exactamente un resultado
        // Si no encuentra nada, lanza excepción, por eso usamos try-catch
        try {
            Producto producto = jdbcTemplate.queryForObject(sql, productoRowMapper, id);
            return Optional.ofNullable(producto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * Equivalente en JPA: productoRepository.save(producto)
     *
     * @param producto Producto a guardar
     * @return Producto guardado con el ID generado
     */
    public Producto save(Producto producto) {
        String sql = "INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";

        // KeyHolder nos permite recuperar el ID generado automáticamente
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            // Creamos el PreparedStatement indicando que queremos recuperar las claves generadas
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Establecemos los parámetros (los ? del SQL)
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getCategoria());

            return ps;
        }, keyHolder);

        // Asignamos el ID generado al producto
        producto.setId(keyHolder.getKey().longValue());

        return producto;
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto Producto con los datos actualizados
     * @return Número de filas afectadas (debería ser 1)
     */
    public int update(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria = ? WHERE id = ?";

        // update() ejecuta INSERT, UPDATE o DELETE y devuelve el número de filas afectadas
        return jdbcTemplate.update(sql,
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria(),
                producto.getId()
        );
    }

    /**
     * Elimina un producto por su ID.
     *
     * Equivalente en JPA: productoRepository.deleteById(id)
     *
     * @param id ID del producto a eliminar
     * @return Número de filas afectadas (debería ser 1)
     */
    public int deleteById(Long id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * Cuenta el total de productos.
     *
     * Equivalente en JPA: productoRepository.count()
     *
     * @return Número total de productos
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM productos";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    // ========================================
    // MÉTODOS DE CONSULTA PERSONALIZADOS
    // ========================================

    /**
     * Busca productos por categoría.
     *
     * En JPA esto sería: findByCategoria(String categoria)
     * y el framework generaría el SQL automáticamente.
     * Aquí lo escribimos nosotros.
     *
     * @param categoria Categoría a buscar
     * @return Lista de productos de esa categoría
     */
    public List<Producto> findByCategoria(String categoria) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos WHERE categoria = ?";
        return jdbcTemplate.query(sql, productoRowMapper, categoria);
    }

    /**
     * Busca productos cuyo nombre contenga el texto especificado.
     *
     * En JPA: findByNombreContainingIgnoreCase(String nombre)
     *
     * @param nombre Texto a buscar
     * @return Lista de productos que coinciden
     */
    public List<Producto> findByNombreContaining(String nombre) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos WHERE LOWER(nombre) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, productoRowMapper, "%" + nombre + "%");
    }

    /**
     * Busca productos con precio menor o igual al especificado.
     *
     * @param precioMaximo Precio máximo
     * @return Lista de productos
     */
    public List<Producto> findByPrecioLessThanEqual(BigDecimal precioMaximo) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos WHERE precio <= ? ORDER BY precio";
        return jdbcTemplate.query(sql, productoRowMapper, precioMaximo);
    }

    /**
     * Obtiene productos con stock disponible.
     *
     * @return Lista de productos con stock > 0
     */
    public List<Producto> findByStockGreaterThanZero() {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos WHERE stock > 0";
        return jdbcTemplate.query(sql, productoRowMapper);
    }

    /**
     * Obtiene todas las categorías distintas.
     *
     * @return Lista de categorías únicas
     */
    public List<String> findAllCategorias() {
        String sql = "SELECT DISTINCT categoria FROM productos ORDER BY categoria";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * Obtiene productos ordenados por precio.
     *
     * @return Lista de productos ordenados de menor a mayor precio
     */
    public List<Producto> findAllOrderByPrecio() {
        String sql = "SELECT id, nombre, descripcion, precio, stock, categoria FROM productos ORDER BY precio ASC";
        return jdbcTemplate.query(sql, productoRowMapper);
    }
}
