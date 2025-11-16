package service;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.math.BigDecimal;
import java.util.List;

// Clase de servicios que implementa transacciones con la base de datos
public class TransaccionesService {

    // Metodo para transferir dinero de un proyecto a otro, usando transacciones
    public boolean transferirPresupuesto(int proyectoOrigenId, int proyectoDestinoId, BigDecimal monto) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConexion();
            conn.setAutoCommit(false); // Empiezo la transacción (desactivo el autocommit)

            // Quito dinero al proyecto origen
            String sqlRestar = "UPDATE proyectos SET presupuesto = presupuesto - ? WHERE id_proyecto = ?";
            try (PreparedStatement psRestar = conn.prepareStatement(sqlRestar)) {
                psRestar.setBigDecimal(1, monto);
                psRestar.setInt(2, proyectoOrigenId);
                psRestar.executeUpdate();
            }

            // Sumo el mismo dinero al proyecto destino
            String sqlSumar = "UPDATE proyectos SET presupuesto = presupuesto + ? WHERE id_proyecto = ?";
            try (PreparedStatement psSumar = conn.prepareStatement(sqlSumar)) {
                psSumar.setBigDecimal(1, monto);
                psSumar.setInt(2, proyectoDestinoId);
                psSumar.executeUpdate();
            }

            conn.commit(); // Confirmo la transacción si todo ha ido bien
            return true;
        } catch (SQLException e) {
            // Si falla algo hago rollback para no dejar la base de datos a medias
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Al final, siempre cierro la conexión
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    // Metodo para asignar varios empleados a un proyecto, usando savepoints por si algún insert falla
    public void asignarEmpleadosConSavepoint(int proyectoId, List<Integer> empleadoIds) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConexion();
            conn.setAutoCommit(false); // Desactivo el autocommit para empezar la transacción
            for (int empId : empleadoIds) {
                // Creo un savepoint antes de cada inserción
                Savepoint sp = conn.setSavepoint("SP_" + empId);
                try {
                    // Hago el insert en asignaciones
                    String sql = "INSERT INTO asignaciones (id_empleado, id_proyecto, fecha_asignacion) VALUES (?, ?, CURDATE())";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, empId);
                        ps.setInt(2, proyectoId);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    // Si este insert falla, hago rollback solo hasta el savepoint de este empleado, los anteriores sí quedan
                    conn.rollback(sp);
                }
            }
            conn.commit(); // Confirmo todos los inserts que no han tenido rollback
        } catch (SQLException e) {
            // Si hay un error general, hago rollback de todo lo que se haya intentado insertar
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            // Cierro la conexión sí o sí al final
            if (conn != null) {
                try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}