------------------------------------------------
-- TIPO tDomicilio
------------------------------------------------
CREATE OR REPLACE TYPE tDomicilio AS OBJECT (
    calle VARCHAR2(50),
    numero INT,
    piso INT,
    escalera INT,
    puerta CHAR(2),
    MEMBER FUNCTION getDomicilio RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tDomicilio AS
    MEMBER FUNCTION getDomicilio RETURN VARCHAR2 IS
    BEGIN
        RETURN calle || ' ' || numero || ' Piso: ' || piso ||
               ' Escalera: ' || escalera || ' Puerta: ' || puerta;
    END;
END;
/
------------------------------------------------
-- TABLA CLIENTE
------------------------------------------------
CREATE TABLE CLIENTE (
    NIF CHAR(9) PRIMARY KEY,
    NOMBRE VARCHAR2(50),
    DOMICILIO tDomicilio,
    TLF VARCHAR2(25),
    CIUDAD VARCHAR2(25)
);

------------------------------------------------
-- INSERTS CLIENTE
------------------------------------------------
INSERT INTO CLIENTE VALUES ('11111111A','ROSA PEREZ DELGADO',
    tDomicilio('Astro',25,3,1,'A'),'913678090','MADRID');

INSERT INTO CLIENTE VALUES ('22222222B','MARIANO RODOLFO DEL VALL',
    tDomicilio('Sol',7,3,2,'A'),'616667788','SEVILLA');

INSERT INTO CLIENTE VALUES ('33333333C','CARLOS MALDONADO PEREZ',
    tDomicilio('Luna',12,4,2,'A'),'617771166','SEVILLA');

INSERT INTO CLIENTE VALUES ('44444444C','PERICO DE LOS PALOTES LARGOS',
    tDomicilio('Oca',9,4,1,'A'),'67778877','MADRID');


------------------------------------------------
-- TIPO VARRAY tColores
------------------------------------------------
CREATE OR REPLACE TYPE tColores AS VARRAY(10) OF VARCHAR2(20);
/

------------------------------------------------
-- TIPO tProducto
------------------------------------------------
CREATE OR REPLACE TYPE tProducto AS OBJECT (
    codigo CHAR(4),
    descripcion VARCHAR2(100),
    colores tColores,
    precio FLOAT,
    stock INTEGER,
    minimo INTEGER,
    MEMBER FUNCTION getReponer RETURN INTEGER,
    MEMBER FUNCTION getRecaudacion RETURN FLOAT,
    MEMBER FUNCTION getColores RETURN VARCHAR2,
    MEMBER FUNCTION getColoresCount RETURN INTEGER,
    MEMBER FUNCTION getColoresFirst RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tProducto AS
    MEMBER FUNCTION getReponer RETURN INTEGER IS
    BEGIN
        IF stock < minimo THEN
            RETURN minimo - stock;
        ELSE
            RETURN 0;
        END IF;
    END;

    MEMBER FUNCTION getRecaudacion RETURN FLOAT IS
    BEGIN
        RETURN precio * stock;
    END;

    MEMBER FUNCTION getColores RETURN VARCHAR2 IS
        cadena VARCHAR2(200);
        i INT := 1;
    BEGIN
        cadena := 'Disponible en ';
        LOOP
            cadena := cadena || colores(i) || ' ';
            EXIT WHEN i = colores.COUNT;
            i := i + 1;
        END LOOP;
        RETURN cadena;
    END;

    MEMBER FUNCTION getColoresCount RETURN INTEGER IS
    BEGIN
        RETURN colores.COUNT;
    END;

    MEMBER FUNCTION getColoresFirst RETURN VARCHAR2 IS
    BEGIN
        RETURN colores(1);
    END;
END;
/
------------------------------------------------
-- TABLA PRODUCTO
------------------------------------------------
CREATE TABLE PRODUCTO OF tProducto;
ALTER TABLE PRODUCTO ADD PRIMARY KEY (codigo);

------------------------------------------------
-- INSERTS PRODUCTO
------------------------------------------------
INSERT INTO PRODUCTO VALUES ('CHA9','CHANDAL DE NIÑO 9-10 AÑOS',
    tColores('Rojo','Verde','Azul'),25.50,7,10);

INSERT INTO PRODUCTO VALUES ('CH11','CHANDAL DE NIÑO 11-12 AÑOS',
    tColores('Rojo','Rosa','Azul'),25.50,8,10);

INSERT INTO PRODUCTO VALUES ('CORH','CORTA VIENTOS HOMBRE',
    tColores('Negro','Gris'),15,3,5);

INSERT INTO PRODUCTO VALUES ('CORM','CORTA VIENTOS MUJER',
    tColores('Negro','Rojo'),15,5,7);

INSERT INTO PRODUCTO VALUES ('PA10','PANTALÓN CORTO 10-11 AÑOS',
    tColores('Rojo','Verde','Amarillo','Azul'),10,8,10);

INSERT INTO PRODUCTO VALUES ('PA12','PANTALÓN CORTO 12-13 AÑOS',
    tColores('Rojo','Verde','Amarillo','Azul'),10,4,10);

INSERT INTO PRODUCTO VALUES ('BAF5','BALÓN DE FÚTBOL Nº 5',
    tColores('Amarillo/verde','Blanco/negro'),7,5,8);

INSERT INTO PRODUCTO VALUES ('BAF4','BALÓN DE FÚTBOL Nº 4',
    tColores('Amarillo/verde','Blanco/negro'),6,4,8);

INSERT INTO PRODUCTO VALUES ('BAB5','BALÓN DE BALONCESTO Nº 5',
    tColores('Rojo/negro','Blanco/azul'),7,5,8);

INSERT INTO PRODUCTO VALUES ('BAB4','BALÓN DE BALONCESTO Nº 4',
    tColores('Rojo/negro','Blanco/azul'),6,3,8);

INSERT INTO PRODUCTO VALUES ('BI20','BICICLETA 20 PULGADAS',
    tColores('Rojo/blanco','Azul/blanco'),150,2,4);


------------------------------------------------
-- TIPO tFactura
------------------------------------------------
CREATE OR REPLACE TYPE tFactura AS OBJECT (
    numero INT,
    fecha DATE,
    nif CHAR(9),
    MEMBER FUNCTION getFactura RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tFactura AS
    MEMBER FUNCTION getFactura RETURN VARCHAR2 IS
    BEGIN
        RETURN 'Factura nº ' || numero || ' - ' || fecha || ' - ' || nif;
    END;
END;
/

------------------------------------------------
-- TABLA FACTURA
------------------------------------------------
CREATE TABLE FACTURA OF tFactura;
ALTER TABLE FACTURA ADD PRIMARY KEY (numero);
ALTER TABLE FACTURA ADD FOREIGN KEY (nif) REFERENCES CLIENTE(nif);

------------------------------------------------
-- INSERTS FACTURA
------------------------------------------------
INSERT INTO FACTURA VALUES (5000, DATE '2021-05-15', '11111111A');
INSERT INTO FACTURA VALUES (5001, DATE '2021-05-16', '11111111A');
INSERT INTO FACTURA VALUES (5002, DATE '2021-06-18', '22222222B');
INSERT INTO FACTURA VALUES (5003, DATE '2021-06-18', '22222222B');
INSERT INTO FACTURA VALUES (5004, DATE '2021-07-19', '33333333C');
INSERT INTO FACTURA VALUES (5005, DATE '2021-07-20', '33333333C');

------------------------------------------------
-- TIPO tDetalle
------------------------------------------------
CREATE OR REPLACE TYPE tDetalle AS OBJECT (
    idetalle INTEGER,
    numero INTEGER,
    codigo CHAR(4),
    unidades INTEGER,
    precio FLOAT,
    MEMBER FUNCTION subtotal RETURN FLOAT,
    MEMBER FUNCTION informa RETURN VARCHAR2
);
/

CREATE OR REPLACE TYPE BODY tDetalle AS
    MEMBER FUNCTION subtotal RETURN FLOAT IS
    BEGIN
        RETURN precio * unidades;
    END;

    MEMBER FUNCTION informa RETURN VARCHAR2 IS
    BEGIN
        RETURN 'Venta de ' || unidades || ' unidades del artículo ' || codigo;
    END;
END;
/

------------------------------------------------
-- TABLA DETALLE
------------------------------------------------
CREATE TABLE DETALLE OF tDetalle;

ALTER TABLE DETALLE ADD PRIMARY KEY (idetalle);
ALTER TABLE DETALLE ADD FOREIGN KEY (numero) REFERENCES FACTURA(numero);
ALTER TABLE DETALLE ADD FOREIGN KEY (codigo) REFERENCES PRODUCTO(codigo);

------------------------------------------------
-- INSERTS DETALLE
------------------------------------------------
INSERT INTO DETALLE VALUES (1, 5000, 'CHA9', 2, 25);
INSERT INTO DETALLE VALUES (2, 5000, 'CH11', 2, 25.5);
INSERT INTO DETALLE VALUES (3, 5000, 'BAF5', 2, 7);
INSERT INTO DETALLE VALUES (4, 5000, 'BAB5', 2, 6);
INSERT INTO DETALLE VALUES (5, 5001, 'BI20', 2, 140);
INSERT INTO DETALLE VALUES (6, 5001, 'BAB5', 3, 6);
INSERT INTO DETALLE VALUES (7, 5002, 'BI20', 1, 150);
INSERT INTO DETALLE VALUES (8, 5003, 'BAB4', 10, 5);
INSERT INTO DETALLE VALUES (9, 5004, 'PA10', 2, 9);
INSERT INTO DETALLE VALUES (10, 5004, 'PA12', 2, 10);
INSERT INTO DETALLE VALUES (11, 5004, 'BAB5', 2, 6);
INSERT INTO DETALLE VALUES (12, 5005, 'BI20', 2, 140);



