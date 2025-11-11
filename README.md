# Sistema POS para Restaurante

Sistema completo de Punto de Venta (POS) para restaurantes desarrollado en **Java** con **Swing** y base de datos en **Supabase** (PostgreSQL).

## 📋 Características

- ✅ Sistema de login con roles (Administrador, Cajero, Mesero)
- ✅ Gestión completa de productos, mesas y usuarios
- ✅ Registro de ventas con generación automática de tickets
- ✅ Cálculo automático de totales y cambio
- ✅ Control de estado de mesas (libre/ocupada/reservada)
- ✅ Reportes de ventas por períodos
- ✅ Auditoría completa de operaciones
- ✅ Interfaz gráfica moderna y funcional

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVC (Modelo-Vista-Controlador)**:

```
src/main/java/com/restaurante/
├── config/          # Configuración de Supabase
├── dao/             # Acceso a datos (CRUD)
├── model/           # Entidades del sistema
├── controller/      # Lógica de negocio
├── view/            # Interfaces gráficas (Swing)
├── util/            # Utilidades y validaciones
└── Main.java        # Punto de entrada
```

## 🚀 Instalación y Configuración

### 1. Prerrequisitos

- **Java 11** o superior
- **Maven** 3.6+
- Cuenta en **Supabase** ([registrarse gratis](https://supabase.com))

### 2. Crear proyecto en Supabase

1. Ve a [https://app.supabase.com](https://app.supabase.com)
2. Crea un nuevo proyecto
3. Espera a que se inicialice (2-3 minutos)

### 3. Ejecutar script de base de datos

1. En tu proyecto de Supabase, ve a **SQL Editor**
2. Copia todo el contenido del archivo `CREATE_DB.sql`
3. Pégalo en el editor y haz clic en **Run**
4. Verifica que se hayan creado todas las tablas

### 4. Obtener credenciales de Supabase

1. Ve a **Settings** → **API** en tu proyecto Supabase
2. Copia:
   - **Project URL** (ejemplo: `https://xxxxx.supabase.co`)
   - **anon/public key** (clave larga que empieza con `eyJ...`)
3. Ve a **Settings** → **Database**
4. En **Connection string**, selecciona la pestaña **URI** y copia la contraseña

### 5. Configurar el proyecto

Edita el archivo `src/main/java/com/restaurante/config/SupabaseConfig.java`:

```java
public static final String SUPABASE_URL = "https://TU-PROJECT-URL.supabase.co";
public static final String SUPABASE_ANON_KEY = "TU-ANON-KEY";

public static String getDbPassword() {
    return "TU-PASSWORD-DE-BD";
}
```

Reemplaza:
- `TU-PROJECT-URL` con tu URL de proyecto
- `TU-ANON-KEY` con tu clave anon
- `TU-PASSWORD-DE-BD` con la contraseña de la base de datos

### 6. Compilar y ejecutar

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
mvn exec:java -Dexec.mainClass="com.restaurante.Main"
```

O desde tu IDE:
1. Importa el proyecto como proyecto Maven
2. Ejecuta la clase `Main.java`

## 👥 Usuarios de Prueba

El script `CREATE_DB.sql` crea tres usuarios por defecto:

| Correo | Contraseña | Rol |
|--------|------------|-----|
| admin@restaurante.com | admin123 | Administrador |
| cajero@restaurante.com | cajero123 | Cajero |
| mesero@restaurante.com | mesero123 | Mesero |

⚠️ **IMPORTANTE**: Cambia estas contraseñas en producción.

## 🔑 Permisos por Rol

### Administrador
- ✅ Acceso total al sistema
- ✅ Gestión de usuarios
- ✅ Gestión de productos
- ✅ Gestión de mesas
- ✅ Registro de ventas
- ✅ Reportes completos
- ✅ Auditoría

### Cajero
- ✅ Registro de ventas
- ✅ Generación de tickets
- ✅ Consulta de productos
- ✅ Consulta de mesas
- ✅ Reportes de ventas

### Mesero
- ✅ Registro de pedidos (ventas)
- ✅ Consulta de productos
- ✅ Consulta de mesas
- ✅ Cambio de estado de mesas

## 📊 Base de Datos

### Tablas principales

- **usuarios**: Usuarios del sistema
- **productos**: Catálogo de productos
- **mesas**: Gestión de mesas
- **ventas**: Registro de ventas
- **detalle_ventas**: Productos de cada venta
- **tickets**: Tickets generados
- **respaldo**: Auditoría de operaciones

### Relaciones

```
usuarios ──┐
           ├─→ ventas ──┐
mesas ─────┘            ├─→ detalle_ventas
                        └─→ tickets
productos ──────────────┘
```

## 🛠️ Tecnologías Utilizadas

- **Java 11** - Lenguaje de programación
- **Swing** - Interfaz gráfica
- **Maven** - Gestión de dependencias
- **JDBC** - Conexión a base de datos
- **PostgreSQL** - Base de datos (vía Supabase)
- **Supabase** - Backend as a Service

## 📦 Dependencias

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.6.0</version>
</dependency>
```

## 🔧 Solución de Problemas

### Error: "Driver de PostgreSQL no encontrado"
```bash
mvn clean install
```

### Error: "No se puede conectar a Supabase"
1. Verifica que tu configuración en `SupabaseConfig.java` sea correcta
2. Verifica tu conexión a internet
3. Asegúrate de que el proyecto Supabase esté activo

### Error: "Tabla no existe"
Ejecuta el script `CREATE_DB.sql` completo en el SQL Editor de Supabase

### La interfaz no se ve bien
El sistema usa el Look and Feel nativo del sistema operativo. Es normal que se vea diferente en Windows, macOS o Linux.

## 📝 Uso del Sistema

### 1. Login
- Ingresa con cualquiera de los usuarios de prueba
- El sistema te redirigirá al menú principal según tu rol

### 2. Registrar una Venta
1. Haz clic en **REGISTRAR VENTA**
2. Selecciona productos y cantidades
3. Opcionalmente selecciona una mesa
4. Ingresa el monto pagado
5. Haz clic en **REGISTRAR VENTA**
6. Se generará un ticket automáticamente

### 3. Gestionar Productos
1. Haz clic en **PRODUCTOS**
2. Para agregar: Llena el formulario y haz clic en **Guardar**
3. Para editar: Selecciona un producto de la tabla, modifica y haz clic en **Actualizar**
4. Para eliminar: Selecciona y haz clic en **Eliminar**

### 4. Ver Reportes
1. Haz clic en **REPORTES**
2. Selecciona el período (Hoy, Esta Semana, Este Mes, Todas)
3. Visualiza el resumen y detalle de ventas

## 🔐 Seguridad

- Las contraseñas se almacenan en texto plano en este ejemplo. **Para producción**, implementa hashing (BCrypt, SHA-256)
- La conexión a Supabase usa SSL por defecto
- Los roles limitan el acceso a funcionalidades según permisos

## 🤝 Contribuir

Este es un proyecto educativo y funcional. Puedes:
- Agregar encriptación de contraseñas
- Implementar impresión real de tickets
- Añadir más reportes y gráficas
- Mejorar la interfaz gráfica
- Agregar más validaciones

## 📄 Licencia

Este proyecto es de código abierto y puede ser usado libremente con fines educativos y comerciales.

## 📧 Soporte

Para problemas o dudas:
1. Verifica que hayas seguido todos los pasos de instalación
2. Revisa la sección de solución de problemas
3. Consulta la documentación de Supabase

## ✨ Créditos

Desarrollado como sistema completo y funcional de POS para restaurantes.

---

**¡Listo para usar!** 🎉
