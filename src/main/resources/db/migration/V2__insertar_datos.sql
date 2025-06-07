-- Insertar un usuario
INSERT INTO usuarios (id_usuario, nombre, correo, contrasena)
VALUES (1, 'María Gómez', 'maria@example.com', 'MariaMec');

-- Insertar animales asociados al usuario
INSERT INTO animales (
    id_animal, nombre, especie, raza, anios, meses, rango_edad,
    descripcion, imagen_url, id_usuario, imagen
) VALUES
      (
          2, 'Milo', 'Gato', 'Siamés', 2, 3, 'Joven',
          'Milo es un gato tranquilo que disfruta de los mimos y las siestas largas.',
          'https://cdn.pixabay.com/photo/2015/03/27/13/16/siamese-cat-694730_1280.jpg', 1, NULL
      ),
      (
          3, 'Kira', 'Perro', 'Border Collie', 1, 8, 'Joven',
          'Kira es muy inteligente y activa, ideal para familias deportistas.',
          'https://cdn.pixabay.com/photo/2017/09/25/13/12/border-collie-2785074_1280.jpg', 1, NULL
      );
