DROP DATABASE IF EXISTS empresa27;
CREATE DATABASE IF NOT EXISTS empresa27;
USE empresa27;

CREATE TABLE empleados (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           salario DOUBLE NOT NULL
);

CREATE TABLE proyectos (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           presupuesto DOUBLE NOT NULL
);

CREATE TABLE asignaciones (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              empleado_id INT NOT NULL,
                              proyecto_id INT NOT NULL,
                              FOREIGN KEY (empleado_id) REFERENCES empleados(id),
                              FOREIGN KEY (proyecto_id) REFERENCES proyectos(id)
);

INSERT INTO empleados (nombre, salario) VALUES ('Juan Perez', 25000);
INSERT INTO empleados (nombre, salario) VALUES ('Maria Gomez', 30000);
INSERT INTO empleados (nombre, salario) VALUES ('Luis Rodríguez', 28000);

INSERT INTO proyectos (nombre, presupuesto) VALUES ('Proyecto Alpha', 100000);
INSERT INTO proyectos (nombre, presupuesto) VALUES ('Proyecto Beta', 75000);
INSERT INTO proyectos (nombre, presupuesto) VALUES ('Proyecto Gamma', 50000);

INSERT INTO asignaciones (empleado_id, proyecto_id) VALUES (1, 1);
INSERT INTO asignaciones (empleado_id, proyecto_id) VALUES (2, 2);
INSERT INTO asignaciones (empleado_id, proyecto_id) VALUES (3, 3);

DELIMITER //
CREATE PROCEDURE asignar_empleado_proyecto(
    IN empId INT,
    IN proyId INT
)
BEGIN
INSERT INTO asignaciones (empleado_id, proyecto_id)
VALUES (empId, proyId);
END //
DELIMITER ;


-- Ver todos los empleados y sus salarios
SELECT * FROM empleados;

-- Ver todos los proyectos y sus presupuestos
SELECT * FROM proyectos;

-- Ver todas las asignaciones empleado-proyecto
SELECT * FROM asignaciones;

-- Ver empleados asignados a cada proyecto
SELECT e.id AS empleado_id, e.nombre AS empleado, p.id AS proyecto_id, p.nombre AS proyecto
FROM asignaciones a
         JOIN empleados e ON a.empleado_id = e.id
         JOIN proyectos p ON a.proyecto_id = p.id;

-- Estadística de presupuesto restante por proyecto
SELECT nombre, presupuesto FROM proyectos;

-- Salario de empleados tras actualizaciones
SELECT nombre, salario FROM empleados;