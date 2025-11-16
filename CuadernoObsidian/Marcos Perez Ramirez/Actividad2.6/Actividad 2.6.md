### Objetivo

Aprender a manejar transacciones en JDBC, incluyendo commit , rollback y savepoint .
Tareas
1.	En la base de datos empresa , crea la tabla cuentas :
CREATE TABLE cuentas (     id INT AUTO_INCREMENT PRIMARY KEY,     titular VARCHAR(100),     saldo DECIMAL(10,2) );
INSERT INTO cuentas (titular, saldo) VALUES
('Ana', 2000.00),
('Luis', 1500.00);
2.	Implementa en Java una clase TransferenciaBancaria que:
Realice una transferencia de 500€ de la cuenta 1 a la cuenta 2.
Use transacciones manuales ( setAutoCommit(false) ).
Aplique commit() si la operación se completa correctamente.
Aplique rollback() si ocurre un error.
3.	Extiende el ejercicio para que:
Se registre en una tabla logs cada paso de la transacción.
Si falla el registro del segundo paso, se use un savepoint para mantener el primero.

`**Base de Datos:**`

`DROP DATABASE IF exists empresa;`  
`CREATE DATABASE IF NOT EXISTS empresa;`  
`USE empresa;`  
  
`CREATE TABLE empleados (`  
                           `id INT AUTO_INCREMENT PRIMARY KEY,`  
                           `nombre VARCHAR(50),`  
                           `salario DOUBLE`  
`);`  
  
`/* CREATE TABLE cuentas (`  
                         `id INT AUTO_INCREMENT PRIMARY KEY,                         saldo DOUBLE);`  
`*/`  
  
`CREATE TABLE cuentas (`  
                         `id INT AUTO_INCREMENT PRIMARY KEY,`  
                         `titular VARCHAR(100),`  
                         `saldo DECIMAL(10,2)`  
`);`  
  
`INSERT INTO cuentas (titular, saldo) VALUES`  
                                         `('Ana', 2000.00),`  
                                         `('Luis', 1500.00);`  
  
`CREATE TABLE proyectos (`  
                           `id INT AUTO_INCREMENT PRIMARY KEY,`  
                           `nombre VARCHAR (100),`  
                           `presupuesto DECIMAL (10,2)`  
`);`  
`INSERT INTO empleados (nombre, salario) VALUES ('Ana', 24000), ('Luis', 28000);`  
`INSERT INTO cuentas (saldo) VALUES (1000), (2000);`  
  
`CREATE TABLE logs (`  
                      `id INT AUTO_INCREMENT PRIMARY KEY,`  
                      `mensaje VARCHAR(255),`  
                      `momento TIMESTAMP DEFAULT CURRENT_TIMESTAMP`  
`);`  
  
`DELIMITER //`  
`CREATE PROCEDURE obtener_empleado(IN empId INT)`  
`BEGIN`  
`SELECT id, nombre, salario FROM empleados WHERE id = empId;`  
`END //`  
`DELIMITER ;`  
  
`SELECT * FROM empleados;`  
`SELECT * FROM cuentas;`  
  
`DELIMITER //`  
`CREATE PROCEDURE incrementar_salario(`  
    `IN empId INT,`  
    `IN incremento DOUBLE,`  
    `OUT nuevoSalario DOUBLE`  
`)`  
`BEGIN`  
  
`UPDATE empleados`  
`SET salario = salario + incremento`  
`WHERE id = empId;`  
  
`SELECT salario INTO nuevoSalario`  
`FROM empleados`  
`WHERE id = empId;`  
`END //`  
`DELIMITER ;`

**`EmpresaDAO.java:`**

`package org.example;`  
  
`import conexion.ConexionBD;`  
`import java.sql.*;`  
`import java.util.Scanner;`  
  
`public class EmpresaDAO {`  
  
    `public void realizarTransferencia() {`  
        `Scanner entrada = new Scanner(System.in);`  
  
        `System.out.print("ID cuenta origen: ");`  
        `int cuentaOrigen = entrada.nextInt();`  
        `entrada.nextLine();`  
  
        `System.out.print("ID cuenta destino: ");`  
        `int cuentaDestino = entrada.nextInt();`  
        `entrada.nextLine();`  
  
        `System.out.print("Cantidad a transferir: ");`  
        `double cantidad = entrada.nextDouble();`  
        `entrada.nextLine();`  
  
        `// try-with-resources en la conexión, fiel a tu estilo:`  
        `try (Connection con = ConexionBD.getConexion()) {`  
            `con.setAutoCommit(false);`  
            `Savepoint savepoint = null;`  
  
            `// Retirar del origen`  
            `try (PreparedStatement ps1 = con.prepareStatement(`  
                    `"UPDATE cuentas SET saldo = saldo - ? WHERE id = ?");`  
                 `PreparedStatement logPaso1 = con.prepareStatement("INSERT INTO logs (mensaje) VALUES (?)")) {`  
                `ps1.setBigDecimal(1, new java.math.BigDecimal(cantidad));`  
                `ps1.setInt(2, cuentaOrigen);`  
                `ps1.executeUpdate();`  
  
                `logPaso1.setString(1, "Retirados " + cantidad + "€ de la cuenta " + cuentaOrigen);`  
                `logPaso1.executeUpdate();`  
  
                `savepoint = con.setSavepoint("Paso1");`  
            `}`  
  
            `// Ingresar en el destino`  
            `try (PreparedStatement ps2 = con.prepareStatement(`  
                    `"UPDATE cuentas SET saldo = saldo + ? WHERE id = ?");`  
                 `PreparedStatement logPaso2 = con.prepareStatement(`  
                         `"INSERT INTO logs (mensaje) VALUES (?)")) {`  
                `ps2.setBigDecimal(1, new java.math.BigDecimal(cantidad));`  
                `ps2.setInt(2, cuentaDestino);`  
                `ps2.executeUpdate();`  
  
                `logPaso2.setString(1, "Ingresados " + cantidad + "€ en la cuenta " + cuentaDestino);`  
                `logPaso2.executeUpdate();`  
  
                `con.commit();`  
                `System.out.println("Transferencia realizada correctamente.");`  
            `} catch (Exception e2) {`  
                `if (savepoint != null) {`  
                    `con.rollback(savepoint);`  
                    `try (PreparedStatement logRollback = con.prepareStatement(`  
                            `"INSERT INTO logs (mensaje) VALUES (?)")) {`  
                        `logRollback.setString(1, "Rollback al savepoint tras fallo en segundo paso.");`  
                        `logRollback.executeUpdate();`  
                        `con.commit();`  
                    `}`  
                `} else {`  
                    `con.rollback();`  
                    `try (PreparedStatement logRollbackTotal = con.prepareStatement(`  
                            `"INSERT INTO logs (mensaje) VALUES (?)")) {`  
                        `logRollbackTotal.setString(1, "Rollback total por error en transferencia.");`  
                        `logRollbackTotal.executeUpdate();`  
                        `con.commit();`  
                    `}`  
                `}`  
                `System.out.println("Error durante la transferencia, rollback realizado.");`  
            `}`  
  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
        `} finally {`  
            `ConexionBD.cerrar();`  
        `}`  
    `}`  
  
    `public static void main(String[] args) {`  
        `EmpresaDAO dao = new EmpresaDAO();`  
        `dao.realizarTransferencia();`  
    `}`  
`}`

`**Main.java:**`

`package org.example;`  
  
`public class Main {`  
    `public static void main(String[] args) {`  
  
        `EmpresaDAO dao = new EmpresaDAO();`  
        `dao.realizarTransferencia();`  
  
    `}`  
`}`

**Resultado:**

![[Pasted image 20251116191359.png]]

![[Pasted image 20251116191646.png]]


