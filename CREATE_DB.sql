-- ============================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- Sistema de Punto de Venta para Restaurante
-- Compatible con Supabase (PostgreSQL)
-- ============================================================

-- Eliminar tablas si existen (en orden inverso por dependencias)
DROP TABLE IF EXISTS respaldo CASCADE;
DROP TABLE IF EXISTS tickets CASCADE;
DROP TABLE IF EXISTS detalle_ventas CASCADE;
DROP TABLE IF EXISTS ventas CASCADE;
DROP TABLE IF EXISTS mesas CASCADE;
DROP TABLE IF EXISTS productos CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- ============================================================
-- TABLA: usuarios
-- Almacena información de los usuarios del sistema
-- ============================================================
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    contraseña_hash TEXT NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('administrador', 'mesero', 'cajero')),
    fecha_creacion TIMESTAMP DEFAULT NOW()
);

-- ============================================================
-- TABLA: productos
-- Catálogo de productos disponibles en el restaurante
-- ============================================================
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL CHECK (precio >= 0),
    disponible BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT NOW()
);

-- ============================================================
-- TABLA: mesas
-- Gestión de mesas del restaurante
-- ============================================================
CREATE TABLE mesas (
    id SERIAL PRIMARY KEY,
    numero INTEGER UNIQUE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'libre' CHECK (estado IN ('libre', 'ocupada', 'reservada'))
);

-- ============================================================
-- TABLA: ventas
-- Registro de ventas realizadas
-- ============================================================
CREATE TABLE ventas (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    id_mesa INTEGER REFERENCES mesas(id) ON DELETE SET NULL,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    fecha_venta TIMESTAMP DEFAULT NOW()
);

-- ============================================================
-- TABLA: detalle_ventas
-- Detalle de productos en cada venta
-- ============================================================
CREATE TABLE detalle_ventas (
    id SERIAL PRIMARY KEY,
    id_venta INTEGER NOT NULL REFERENCES ventas(id) ON DELETE CASCADE,
    id_producto INTEGER NOT NULL REFERENCES productos(id) ON DELETE RESTRICT,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10, 2) NOT NULL CHECK (subtotal >= 0)
);

-- ============================================================
-- TABLA: tickets
-- Tickets generados para cada venta
-- ============================================================
CREATE TABLE tickets (
    id SERIAL PRIMARY KEY,
    id_venta INTEGER NOT NULL REFERENCES ventas(id) ON DELETE CASCADE,
    folio VARCHAR(50) UNIQUE NOT NULL,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    cambio NUMERIC(10, 2) DEFAULT 0 CHECK (cambio >= 0),
    fecha_emision TIMESTAMP DEFAULT NOW()
);

-- ============================================================
-- TABLA: respaldo
-- Registro de auditoría de operaciones
-- ============================================================
CREATE TABLE respaldo (
    id SERIAL PRIMARY KEY,
    tipo_operacion VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT NOW()
);

-- ============================================================
-- ÍNDICES PARA MEJORAR RENDIMIENTO
-- ============================================================
CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_ventas_usuario ON ventas(id_usuario);
CREATE INDEX idx_detalle_venta ON detalle_ventas(id_venta);
CREATE INDEX idx_detalle_producto ON detalle_ventas(id_producto);
CREATE INDEX idx_tickets_venta ON tickets(id_venta);
CREATE INDEX idx_tickets_folio ON tickets(folio);
CREATE INDEX idx_respaldo_fecha ON respaldo(fecha);

-- ============================================================
-- DATOS INICIALES - Usuario administrador
-- ============================================================
-- Contraseña: admin123 (deberías cambiarla después)
INSERT INTO usuarios (nombre, correo, contraseña_hash, rol) 
VALUES ('Administrador', 'admin@restaurante.com', 'admin123', 'administrador');

-- Usuario cajero de ejemplo
INSERT INTO usuarios (nombre, correo, contraseña_hash, rol) 
VALUES ('Juan Pérez', 'cajero@restaurante.com', 'cajero123', 'cajero');

-- Usuario mesero de ejemplo
INSERT INTO usuarios (nombre, correo, contraseña_hash, rol) 
VALUES ('María González', 'mesero@restaurante.com', 'mesero123', 'mesero');

-- ============================================================
-- DATOS INICIALES - Productos de ejemplo
-- ============================================================
INSERT INTO productos (nombre, categoria, precio, disponible) VALUES
('Hamburguesa Clásica', 'Comida', 85.00, TRUE),
('Pizza Margarita', 'Comida', 120.00, TRUE),
('Ensalada César', 'Comida', 65.00, TRUE),
('Tacos al Pastor', 'Comida', 45.00, TRUE),
('Pasta Alfredo', 'Comida', 95.00, TRUE),
('Refresco Cola', 'Bebida', 25.00, TRUE),
('Agua Natural', 'Bebida', 15.00, TRUE),
('Cerveza', 'Bebida', 35.00, TRUE),
('Café Americano', 'Bebida', 30.00, TRUE),
('Jugo de Naranja', 'Bebida', 28.00, TRUE),
('Pastel de Chocolate', 'Postre', 55.00, TRUE),
('Helado', 'Postre', 40.00, TRUE),
('Flan Napolitano', 'Postre', 45.00, TRUE);

-- ============================================================
-- DATOS INICIALES - Mesas
-- ============================================================
INSERT INTO mesas (numero, estado) VALUES
(1, 'libre'),
(2, 'libre'),
(3, 'libre'),
(4, 'libre'),
(5, 'libre'),
(6, 'libre'),
(7, 'libre'),
(8, 'libre'),
(9, 'libre'),
(10, 'libre');

-- ============================================================
-- REGISTRO DE AUDITORÍA INICIAL
-- ============================================================
INSERT INTO respaldo (tipo_operacion, descripcion) 
VALUES ('INICIALIZACIÓN', 'Base de datos creada e inicializada correctamente');

-- ============================================================
-- VERIFICACIÓN
-- ============================================================
-- Descomentar las siguientes líneas para verificar la creación
-- SELECT 'Usuarios creados: ' || COUNT(*) FROM usuarios;
-- SELECT 'Productos creados: ' || COUNT(*) FROM productos;
-- SELECT 'Mesas creadas: ' || COUNT(*) FROM mesas;
