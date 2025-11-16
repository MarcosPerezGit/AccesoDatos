package org.example;

import conexion.ConexionBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // Obtener y usar 3 conexiones distintas
        for (int i = 1; i <= 3; i++) {
            try (Connection con = ConexionBD.getConexion();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT nombre FROM empleados")) {

                System.out.println("Conexión " + i + ":");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("nombre"));
                }
                System.out.println("----------------------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Las conexiones se devuelven automáticamente al pool por try-with-resources
    }
}