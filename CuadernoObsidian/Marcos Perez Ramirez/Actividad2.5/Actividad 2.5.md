
### Objetivo

Aprender a invocar procedimientos almacenados con parámetros de entrada y salida desde Java usando CallableStatement .
Tareas
1.	En la base de datos empresa , crea un procedimiento llamado incrementar_salario que:
 Reciba como parámetros de entrada el id de un empleado y un incremento salarial ( IN ).
		Devuelva el nuevo salario ( OUT ).
2.	Crea una clase Java llamada PruebaCallable que:
Conecte con la BD empresa .
Llame al procedimiento incrementar_salario .
Muestre el nuevo salario por consola.
3.	Comprueba que el salario del empleado se actualiza en la base de datos.

`**EmpresaDAO:**`

`package org.example;`  
  
`import conexion.ConexionBD;`  
`import java.sql.CallableStatement;`  
`import java.sql.Connection;`  
  
`public class EmpresaDAO {`  
  
    `public void incrementarSalario(int idEmpleado, double incremento) {`  
        `try (Connection con = ConexionBD.getConexion();`  
             `CallableStatement cs = con.prepareCall("{call incrementar_salario(?, ?, ?)}")) {`  
  
            `cs.setInt(1, idEmpleado);              // Parametro IN: ID empleado`  
            `cs.setDouble(2, incremento);           // Parametro IN: incremento salarial`  
            `cs.registerOutParameter(3, java.sql.Types.DOUBLE); // Parametro OUT: nuevo salario`  
  
            `cs.execute();`  
  
            `double nuevoSalario = cs.getDouble(3);`  
            `System.out.println("Nuevo salario para el empleado con ID " + idEmpleado + ": " + nuevoSalario);`  
  
        `} catch (Exception e) {`  
            `e.printStackTrace();`  
        `}`  
    `}`  
`}`

`**Main.java:**`

`package org.example;`  
  
`public class Main {`  
    `public static void main(String[] args) {`  
  
        `EmpresaDAO dao = new EmpresaDAO();`  
        `dao.incrementarSalario(1, 1500.0);`  
        `}`  
    `}`

**Resultado:**

![[Pasted image 20251116191026.png]]

