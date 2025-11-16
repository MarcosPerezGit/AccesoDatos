package org.example;

import conexion.ConexionBD;
import java.sql.*;
import java.util.Scanner;

public class EmpresaDAO {

    public void realizarTransferencia() {
        Scanner entrada = new Scanner(System.in);

        System.out.print("ID cuenta origen: ");
        int cuentaOrigen = entrada.nextInt();
        entrada.nextLine();

        System.out.print("ID cuenta destino: ");
        int cuentaDestino = entrada.nextInt();
        entrada.nextLine();

        System.out.print("Cantidad a transferir: ");
        double cantidad = entrada.nextDouble();
        entrada.nextLine();

        // try-with-resources en la conexión, fiel a tu estilo:
        try (Connection con = ConexionBD.getConexion()) {
            con.setAutoCommit(false);
            Savepoint savepoint = null;

            // Retirar del origen
            try (PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE cuentas SET saldo = saldo - ? WHERE id = ?");
                 PreparedStatement logPaso1 = con.prepareStatement("INSERT INTO logs (mensaje) VALUES (?)")) {
                ps1.setBigDecimal(1, new java.math.BigDecimal(cantidad));
                ps1.setInt(2, cuentaOrigen);
                ps1.executeUpdate();

                logPaso1.setString(1, "Retirados " + cantidad + "€ de la cuenta " + cuentaOrigen);
                logPaso1.executeUpdate();

                savepoint = con.setSavepoint("Paso1");
            }

            // Ingresar en el destino
            try (PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE cuentas SET saldo = saldo + ? WHERE id = ?");
                 PreparedStatement logPaso2 = con.prepareStatement(
                         "INSERT INTO logs (mensaje) VALUES (?)")) {
                ps2.setBigDecimal(1, new java.math.BigDecimal(cantidad));
                ps2.setInt(2, cuentaDestino);
                ps2.executeUpdate();

                logPaso2.setString(1, "Ingresados " + cantidad + "€ en la cuenta " + cuentaDestino);
                logPaso2.executeUpdate();

                con.commit();
                System.out.println("Transferencia realizada correctamente.");
            } catch (Exception e2) {
                if (savepoint != null) {
                    con.rollback(savepoint);
                    try (PreparedStatement logRollback = con.prepareStatement(
                            "INSERT INTO logs (mensaje) VALUES (?)")) {
                        logRollback.setString(1, "Rollback al savepoint tras fallo en segundo paso.");
                        logRollback.executeUpdate();
                        con.commit();
                    }
                } else {
                    con.rollback();
                    try (PreparedStatement logRollbackTotal = con.prepareStatement(
                            "INSERT INTO logs (mensaje) VALUES (?)")) {
                        logRollbackTotal.setString(1, "Rollback total por error en transferencia.");
                        logRollbackTotal.executeUpdate();
                        con.commit();
                    }
                }
                System.out.println("Error durante la transferencia, rollback realizado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConexionBD.cerrar();
        }
    }

    public static void main(String[] args) {
        EmpresaDAO dao = new EmpresaDAO();
        dao.realizarTransferencia();
    }
}
