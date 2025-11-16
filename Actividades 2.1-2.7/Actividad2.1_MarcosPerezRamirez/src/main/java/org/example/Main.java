package org.example;

import conexion.ConexionBD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        EmpleadosDAO dao = new EmpleadosDAO();

        System.out.println("Listado de empleados (Statement):");
        dao.listarEmpleados();

        System.out.println("\nBuscar empleado por ID (PreparedStatement):");
        dao.buscarEmpleadoPorId(1);

        System.out.println("\nBuscar empleado por procedimiento almacenado (CallableStatement):");
        dao.obtenerEmpleado(2);
    }

}
