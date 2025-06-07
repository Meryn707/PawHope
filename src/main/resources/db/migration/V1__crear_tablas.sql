CREATE TABLE usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE animales (
    id_animal BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(255) NOT NULL,
    raza VARCHAR(255) NOT NULL,
    anios INT NOT NULL,
    meses INT NOT NULL,
    rango_edad VARCHAR(255),
    descripcion VARCHAR(1000) NOT NULL,
    imagen_url VARCHAR(255) NOT NULL,
    id_usuario BIGINT NOT NULL,
    imagen LONGBLOB,
    CONSTRAINT fk_responsable FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE solicitudes_adopcion (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   id_usuario BIGINT,
   id_animal BIGINT NOT NULL,
   estado VARCHAR(255) NOT NULL,
   nombre VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   telefono VARCHAR(255) NOT NULL,
   motivo VARCHAR(1000) NOT NULL,
   CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
   CONSTRAINT fk_animal FOREIGN KEY (id_animal) REFERENCES animales(id_animal)
);
