# POSRest - Resumen Técnico

## Arquitectura
- **Lenguaje**: Java 11 (Swing) con patrón MVC.
- **Capas**:
  - **Model**: entidades del dominio (`Usuario`, `Producto`, `Venta`, etc.).
  - **DAO**: acceso a datos contra PostgreSQL/Supabase.
  - **Controller**: reglas de negocio (validaciones, cálculos, auditoría).
  - **View**: ventanas Swing modernas.
  - **Util**: conexión DB, JasperReports, PDF, validaciones.

## Componentes principales
- `VentaController`: orquesta registro de ventas, crea detalles y ticket.
- `TicketPdfGenerator`: genera comprobante PDF con OpenPDF y lo abre en el navegador.
- `JasperReportManager`: carga, compila y muestra reportes (`ventas_general.jrxml`).
- `MenuPrincipalFrame`: dashboard dinámico por rol con accesos principales.
- `CREATE_DB.sql`: script maestro con schema e inserts demo.

## Flujo de venta
1. Usuario inicia sesión (`LoginFrame`).
2. Desde el dashboard abre **Nueva Venta**.
3. Selecciona productos, cantidades y mesa.
4. `VentaController` calcula total, inserta venta y detalle.
5. Se genera ticket (`TicketDAO`, `TicketPdfGenerator`).
6. Auditoría registrada en `respaldo`.

## Reportes
- JasperReports consulta `ventas` con parámetros opcionales de fecha.
- `ReporteFrame` guarda el último rango para reutilizarlo en la vista Jasper.

## Generación de artefactos
- **Ticket PDF**: botón "Exportar PDF" en `TicketFrame`.
- **Reporte Jasper**: botón "Ver en Jasper" en `ReporteFrame`.

## Publicación
- Repositorio de referencia: <https://github.com/HappyCarrot-php/posrest>
- Para empaquetar: `mvn clean package` produce el JAR listo para distribución.
