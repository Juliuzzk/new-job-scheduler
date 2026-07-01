SELECT id, nombre, email FROM clientes WHERE fecha_creacion >= DATEADD(DAY, -7, GETDATE());
SELECT COUNT(*) AS nuevos_clientes FROM clientes WHERE fecha_creacion >= DATEADD(DAY, -7, GETDATE());
