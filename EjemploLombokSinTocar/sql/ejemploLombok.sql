DROP DATABASE IF EXISTS ejemploLombok;
CREATE DATABASE IF NOT EXISTS ejemploLombok;
USE ejemploLombok;

CREATE TABLE usuario(
                        id int auto_increment primary key not null,
                        username varchar(100) unique not null,
                        password varchar(255) not null,
                        activo tinyint(1) default 1
);

INSERT INTO usuario ( username, password) values ('Marcos','$2a$12$CzdU7n41SWsBnlPVerrdu.rAHZGeeYxXqkZYyyraM0YxrT6ry4b.2');