DROP DATABASE IF exists empresa;
CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

CREATE TABLE empleados (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(50),
                           salario DOUBLE
);

CREATE TABLE cuentas (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         saldo DOUBLE
);

INSERT INTO empleados (nombre, salario) VALUES ('Ana', 24000), ('Luis', 28000);
INSERT INTO cuentas (saldo) VALUES (1000), (2000);

DELIMITER //
CREATE PROCEDURE obtener_empleado(IN empId INT)
BEGIN
SELECT id, nombre, salario FROM empleados WHERE id = empId;
END //
DELIMITER ;