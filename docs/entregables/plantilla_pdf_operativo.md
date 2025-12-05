# POSRest - Informe Operativo

## Objetivo
Presentar un resumen de uso del sistema POSRest para operaciones diarias de un restaurante.

## Flujo recomendado
1. **Inicio de sesión** con credenciales entregadas.
2. **Gestión de inventario** en `ProductoFrame` para dar de alta o actualizar precios.
3. **Registro de venta** desde `VentaFrame` agregando productos y mesas.
4. **Ticket** generado automáticamente con opción de exportar a PDF.
5. **Reportes Jasper** para analizar totales por periodo.

## Características clave
- Control de roles (administrador, cajero, mesero).
- Inventario organizado por categorías.
- Ventas con cálculo de totales y cambio.
- Tickets en PDF almacenados en `%USERPROFILE%/posrest/tickets`.
- Reporte `ventas_general.jrxml` invocado desde `ReporteFrame`.

## Datos demo incluidos
- Usuarios de prueba (`CREATE_DB.sql`).
- Productos y mesas precargados.
- Ventas históricas para demo de reportes.

## Pasos de instalación (resumen)
1. Configurar credenciales en `SupabaseConfig.java`.
2. Ejecutar `CREATE_DB.sql` en Supabase/PostgreSQL.
3. Ejecutar `mvn clean package` y `mvn exec:java -Dexec.mainClass="com.restaurante.Main"`.

## Observaciones
- Para producción se recomienda cifrar contraseñas y revisar políticas de acceso.
- JasperReports requiere drivers JDBC disponibles (ya declarados en `pom.xml`).
- El respaldo de BD se realiza vía `MenuPrincipalFrame` (botón "Respaldo BD").
