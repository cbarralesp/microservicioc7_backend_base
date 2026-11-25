# Tienda IPSS · Microservicios (proyecto base)

## ¿Qué haremos?

Una tienda con **dos microservicios** en Java 21 y Spring Boot. Cada uno tiene **su propia base de datos** PostgreSQL y se comunican por HTTP.

| Servicio | Puerto | Base de datos | Estado |
|---|---|---|---|
| `producto-service` | 8086 | `producto_db` | ✅ Terminado: se levanta y se explica |
| `pedido-service` | 8087 | `pedido_db` | ✎ Se completa en clase |

Al hacer un pedido, `pedido-service` consulta a `producto-service`, revisa el stock, lo descuenta y guarda el pedido. Veremos capas, buenas prácticas y el patrón **Builder**.

Front en Angular: [microservicioc7_front](https://github.com/cbarralesp/microservicioc7_front)

## Conexión a la base de datos

Necesitas PostgreSQL (puerto 5432) y pgAdmin. El script se ejecuta en **3 veces**, porque pgAdmin no permite cambiar de base dentro de un script. Cada bloque se ejecuta completo con **F5** y se puede repetir sin errores.

### 1. Usuarios · Query Tool sobre la base `postgres`

```sql
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'producto') THEN
        CREATE USER producto WITH PASSWORD 'producto';
    ELSE
        ALTER USER producto WITH PASSWORD 'producto';
    END IF;
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'pedido') THEN
        CREATE USER pedido WITH PASSWORD 'pedido';
    ELSE
        ALTER USER pedido WITH PASSWORD 'pedido';
    END IF;
END
$$;
```

### 2. Crear las bases a mano

Clic derecho en **Databases > Create > Database...**

| Database | Owner |
|---|---|
| `producto_db` | `producto` |
| `pedido_db` | `pedido` |

### 3. Permisos · Query Tool sobre `producto_db`

```sql
ALTER DATABASE producto_db OWNER TO producto;
GRANT ALL PRIVILEGES ON DATABASE producto_db TO producto;
-- Desde PostgreSQL 15 el esquema public ya no es escribible por cualquiera: sin esto Flyway falla.
GRANT ALL ON SCHEMA public TO producto;
```

### 4. Permisos · Query Tool sobre `pedido_db`

```sql
ALTER DATABASE pedido_db OWNER TO pedido;
GRANT ALL PRIVILEGES ON DATABASE pedido_db TO pedido;
GRANT ALL ON SCHEMA public TO pedido;
```

### Datos de conexión

Ya vienen en el `application.yml` de cada servicio. Las tablas las crea **Flyway** al levantar la aplicación.

| Servicio | URL | Usuario / clave |
|---|---|---|
| `producto-service` | `jdbc:postgresql://localhost:5432/producto_db` | `producto` / `producto` |
| `pedido-service` | `jdbc:postgresql://localhost:5432/pedido_db` | `pedido` / `pedido` |

Son claves solo para el equipo local. En la nube se entregan con las variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` y `DB_PASSWORD`.

## Levantar producto-service

Requiere Java 21.

```powershell
cd producto-service
.\mvnw.cmd spring-boot:run
```

Swagger: http://localhost:8086/swagger-ui/index.html

## 🚩 Aquí comenzamos a completar `pedido-service`

1. **TODO 1** · `src/main/resources/application.yml`: URL y timeout para comunicarse con `producto-service`
2. **TODO 2** · `entity/Pedido.java`: Builder (validar y calcular el total)
3. **TODO 3** · `service/impl/PedidoServiceImpl.java`: terminar `crear`
4. **TODO 4** · `controller/PedidoController.java`: `listar` y `crear`

La aplicación no arranca hasta completar el TODO 1. Al inicio fallan 7 de los 9 tests (`.\mvnw.cmd test`); al terminar pasan todos.
