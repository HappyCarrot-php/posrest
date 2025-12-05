# Guion sugerido: Video Operativo POSRest

Este guion te permitirá grabar un recorrido de negocio en menos de 5 minutos.

## 1. Introducción (20 s)
- Presenta la aplicación "POSRest" y el objetivo del video: registrar una venta simulada, mostrar el ticket y mantener inventario.

## 2. Inicio de sesión (30 s)
- Abre la app (`Main`) y usa el usuario demo `cajero@restaurante.com / cajero123`.
- Resalta la interfaz moderna del dashboard.

## 3. Alta/actualización de producto (45 s)
- Desde el menú principal elige **Productos**.
- Muestra cómo crear un nuevo producto o actualizar el precio de uno existente.
- Guarda los cambios y resalta el estado "Disponible".

## 4. Simulación de venta (90 s)
- Regresa al dashboard, abre **Nueva Venta**.
- Selecciona los productos (incluye el que acabas de editar para mostrar el cambio).
- Asigna cantidad, opcionalmente elige una mesa y agrega al carrito.
- Ingresa el monto pagado y presiona **Registrar Venta**.

## 5. Ticket y PDF (60 s)
- Al finalizar se abre `TicketFrame` con el comprobante.
- Pulsa **Exportar PDF** para mostrar la generación automática (el archivo se abre en el navegador).
- Comenta que el archivo se guarda en `%USERPROFILE%/posrest/tickets`.

## 6. JasperReports (45 s)
- Desde el dashboard abre **Reportes**.
- Cambia el filtro de período y presiona **Ver en Jasper**.
- Muestra la tabla y el resumen del reporte generado.

## 7. Cierre (20 s)
- Resume beneficios: control de inventario, ventas rápidas, tickets en PDF y reportes.
- Invita a consultar la documentación en `docs/` para más detalles.
