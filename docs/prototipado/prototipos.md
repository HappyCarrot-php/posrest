# Prototipado de Interfaces POSRest

Este documento describe el prototipado funcional logrado directamente con interfaces Swing. Cada pantalla dispone de un "mock" navegable dentro de la aplicación.

## LoginFrame
- Objetivo: validar credenciales antes de acceder al sistema.
- Componentes principales: campos de usuario/contraseña, botón iniciar sesión, mensaje contextual.

## MenuPrincipalFrame
- Objetivo: servir como tablero de accesos rápidos.
- Componentes: tarjetas con accesos a ventas, reportes, mantenimiento de catálogos y respaldo.

## VentaFrame
- Objetivo: emular el flujo de captación de pedidos en barra.
- Componentes: lista de productos, panel de carrito, totales dinámicos, botones de cobro e impresión.

## ReporteFrame
- Objetivo: validar los indicadores clave antes de producir los reportes formales.
- Componentes: filtro de período, tarjetas de resumen, tabla de ventas, botón "Ver en Jasper".

## TicketFrame
- Objetivo: previsualizar el comprobante del cliente y permitir exportarlo.
- Componentes: visor tipo ticket, botón de impresión simulada y nueva opción para generar PDF automático en navegador.

> El prototipado se mantiene vivo en el código fuente para permitir iteraciones rápidas: cualquier cambio visual debe reflejarse tanto en la lógica Swing como en estas notas para conservar trazabilidad de diseño.
