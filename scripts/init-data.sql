-- Script para inicializar datos de prueba en H2

-- Insertar empleados de prueba
INSERT INTO empleados (legajo, nombre, email) VALUES 
(2001, 'Juan Pérez', 'juan.perez@empresa.com'),
(2002, 'María García', 'maria.garcia@empresa.com'),
(2003, 'Carlos López', 'carlos.lopez@empresa.com');

-- Insertar encargados de prueba
INSERT INTO encargados (legajo, nombre, email, tipo_encargado, modo) VALUES
(1001, 'Ana García', 'ana@excusas.com', 'RECEPCIONISTA', 'NORMAL'),
(1002, 'Carlos López', 'carlos@excusas.com', 'SUPERVISORAREA', 'PRODUCTIVO'),
(1003, 'María Rodríguez', 'maria@excusas.com', 'GERENTERRHH', 'NORMAL'),
(1004, 'Roberto Silva', 'roberto@excusas.com', 'CEO', 'NORMAL');

-- Insertar algunas excusas de ejemplo
INSERT INTO excusas (motivo, tipo_excusa, fecha_creacion, empleado_legajo) VALUES
('QUEDARSE_DORMIDO', 'ExcusaTrivial', CURRENT_TIMESTAMP, 2001),
('PERDIDA_SUMINISTRO', 'ExcusaPerdidaSuministro', CURRENT_TIMESTAMP, 2002),
('INCREIBLE_INVEROSIMIL', 'ExcusaInverosimil', CURRENT_TIMESTAMP, 2003);

-- Insertar prontuarios correspondientes a las excusas inverosímiles
INSERT INTO prontuarios (fecha_creacion, empleado_legajo, excusa_id) 
SELECT CURRENT_TIMESTAMP, e.empleado_legajo, e.id 
FROM excusas e 
WHERE e.tipo_excusa = 'ExcusaInverosimil';
