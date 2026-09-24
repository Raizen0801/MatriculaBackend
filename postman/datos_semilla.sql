-- datos_semilla.sql
INSERT INTO carreras (nombre, descripcion, estado, fecha_creacion)
VALUES ('Ingeniería de Sistemas', 'EP Ingeniería de Sistemas', 1, CURRENT_TIMESTAMP);
INSERT INTO carreras (nombre, descripcion, estado, fecha_creacion)
VALUES ('Ingeniería Civil', 'EP Ingeniería Civil', 1, CURRENT_TIMESTAMP);
INSERT INTO carreras (nombre, descripcion, estado, fecha_creacion)
VALUES ('Arquitectura', 'EP Arquitectura y Urbanismo', 1, CURRENT_TIMESTAMP);
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS401', 'Lenguaje de Programación II', 3, 4, 30, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS402', 'Base de Datos II', 4, 4, 25, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS403', 'Ingeniería de Requisitos', 3, 4, 2, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS404', 'Estadística Aplicada', 3, 4, 0, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS501', 'Arquitectura de Software', 4, 5, 20, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS502', 'Sistemas Operativos', 4, 5, 15, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS503', 'Redes de Computadoras', 4, 5, 20, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IC401', 'Mecánica de Suelos', 4, 4, 30, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería Civil';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'AR401', 'Taller de Diseño IV', 6, 4, 15, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Arquitectura';
INSERT INTO estudiantes (codigo, dni, nombres, apellidos, email, estado,
                         carrera_id, fecha_creacion)
SELECT '202410001', '71234567', 'Ana Lucía', 'Quispe Mamani', 'ana.quispe@upeu.edu.pe',
       1, id, CURRENT_TIMESTAMP FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO estudiantes (codigo, dni, nombres, apellidos, email, estado,
                         carrera_id, fecha_creacion)
SELECT '202410002', '72345678', 'Jorge Luis', 'Condori Apaza', 'jorge.condori@upeu.edu.pe',
       1, id, CURRENT_TIMESTAMP FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO estudiantes (codigo, dni, nombres, apellidos, email, estado,
                         carrera_id, fecha_creacion)
SELECT '202410003', '73456789', 'María Elena', 'Huamán Torres', 'maria.huaman@upeu.edu.pe',
       1, id, CURRENT_TIMESTAMP FROM carreras WHERE nombre = 'Ingeniería Civil';
INSERT INTO estudiantes (codigo, dni, nombres, apellidos, email, estado,
                         carrera_id, fecha_creacion)
SELECT '202410004', '74567890', 'Carlos Alberto', 'Mamani Flores', 'carlos.mamani@upeu.edu.pe',
       0, id, CURRENT_TIMESTAMP FROM carreras WHERE nombre = 'Ingeniería de Sistemas';
INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IC402', 'Tecnología del Concreto', 4, 4, 0, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería Civil';

INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'AR402', 'Historia de la Arquitectura II', 3, 4, 25, 0, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Arquitectura';

INSERT INTO cursos (codigo, nombre, creditos, ciclo, vacantes, estado, carrera_id, fecha_creacion)
SELECT 'IS504', 'Inteligencia Artificial', 4, 5, 25, 1, id, CURRENT_TIMESTAMP
FROM carreras WHERE nombre = 'Ingeniería de Sistemas';

INSERT INTO estudiantes (codigo, dni, nombres, apellidos, email, estado, carrera_id, fecha_creacion)
SELECT '202410005', '75678901', 'Valeria Sofia', 'Rojas Mendoza', 'valeria.rojas@upeu.edu.pe',
       1, id, CURRENT_TIMESTAMP FROM carreras WHERE nombre = 'Arquitectura';

INSERT INTO estudiantes (codigo, dni, nombres, apellidos, email, estado, carrera_id, fecha_creacion)
SELECT '202410006', '76789012', 'Diego Alonso', 'Vargas Paredes', 'diego.vargas@upeu.edu.pe',
       1, id, CURRENT_TIMESTAMP FROM carreras WHERE nombre = 'Ingeniería Civil';

COMMIT;

