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

CREATE TABLE proyectos (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR (100),
                           presupuesto DECIMAL (10,2)
);
INSERT INTO empleados (nombre, salario) VALUES ('Ana', 24000), ('Luis', 28000);
INSERT INTO cuentas (saldo) VALUES (1000), (2000);

DELIMITER //
CREATE PROCEDURE obtener_empleado(IN empId INT)
BEGIN
SELECT id, nombre, salario FROM empleados WHERE id = empId;
END //
DELIMITER ;

SELECT * FROM empleados;

DELIMITER //
CREATE PROCEDURE incrementar_salario(
    IN empId INT,
    IN incremento DOUBLE,
    OUT nuevoSalario DOUBLE
)
BEGIN

UPDATE empleados
SET salario = salario + incremento
WHERE id = empId;

SELECT salario INTO nuevoSalario
FROM empleados
WHERE id = empId;
END //
DELIMITER ;