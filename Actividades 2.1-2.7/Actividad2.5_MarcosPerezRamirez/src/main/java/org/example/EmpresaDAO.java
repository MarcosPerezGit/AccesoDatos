package org.example;

import conexion.ConexionBD;
import java.sql.CallableStatement;
import java.sql.Connection;

public class EmpresaDAO {

    public void incrementarSalario(int idEmpleado, double incremento) {
        try (Connection con = ConexionBD.getConexion();
             CallableStatement cs = con.prepareCall("{call incrementar_salario(?, ?, ?)}")) {

            cs.setInt(1, idEmpleado);              // Parametro IN: ID empleado
            cs.setDouble(2, incremento);           // Parametro IN: incremento salarial
            cs.registerOutParameter(3, java.sql.Types.DOUBLE); // Parametro OUT: nuevo salario

            cs.execute();

            double nuevoSalario = cs.getDouble(3);
            System.out.println("Nuevo salario para el empleado con ID " + idEmpleado + ": " + nuevoSalario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
