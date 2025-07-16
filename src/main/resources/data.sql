INSERT INTO empleados (legajo, nombre, email) VALUES
                                                  (1001, 'Juan Pérez', 'juan.perez@excusas-sa.com'),
                                                  (1002, 'María García', 'maria.garcia@excusas-sa.com'),
                                                  (1003, 'Carlos López', 'carlos.lopez@excusas-sa.com'),
                                                  (1004, 'Ana Martínez', 'ana.martinez@excusas-sa.com'),
                                                  (1005, 'Luis Rodríguez', 'luis.rodriguez@excusas-sa.com');

INSERT INTO encargados (legajo, nombre, email, tipo_encargado, modo) VALUES
                                                                         (2001, 'Roberto Supervisor', 'roberto.supervisor@excusas-sa.com', 'SupervisorArea', 'NORMAL'),
                                                                         (2002, 'Laura Gerente', 'laura.gerente@excusas-sa.com', 'GerenteRRHH', 'PRODUCTIVO'),
                                                                         (2003, 'Miguel CEO', 'miguel.ceo@excusas-sa.com', 'CEO', 'NORMAL');

INSERT INTO excusas (motivo, tipo_excusa, fecha_creacion, aceptada, encargado_que_manejo, empleado_legajo) VALUES
                                                                                                               ('QUEDARSE_DORMIDO', 'ExcusaTrivial', '2024-01-15 08:30:00', true, 'Roberto Supervisor', 1001),
                                                                                                               ('CUIDADO_FAMILIAR', 'ExcusaCuidadoFamiliar', '2024-01-16 09:00:00', true, 'Laura Gerente', 1002),
                                                                                                               ('PERDIDA_SUMINISTRO', 'ExcusaPerdidaSuministro', '2024-01-17 07:45:00', false, 'Miguel CEO', 1003),
                                                                                                               ('PERDI_TRANSPORTE', 'ExcusaTrivial', '2024-01-18 08:15:00', true, 'Roberto Supervisor', 1004);

INSERT INTO prontuarios (fecha_creacion, empleado_legajo, excusa_id) VALUES
    ('2024-01-17 07:45:00', 1003, 3);
