# resena-service · Reseñas de productos (MongoDB)

Tercer microservicio de la tienda. Guarda reseñas en **MongoDB** y pregunta a `producto-service` si el producto existe. Los otros dos servicios siguen en PostgreSQL, sin cambios.

| Dato | Valor |
|---|---|
| Puerto | 8088 |
| Base / colección | `resena_db` / `resenas` |
| Necesita | Java 21, MongoDB y `producto-service` en el 8086 |

## 1. Instalar MongoDB

Elige una opción:

- **Local:** instala MongoDB Community Server (como servicio de Windows, puerto 27017) y MongoDB Compass.
- **Nube:** crea un cluster gratuito en MongoDB Atlas y copia su cadena de conexión.

No hay que crear nada a mano: la base y la colección aparecen al guardar la primera reseña.

### Conexión

Local, sin configurar nada: `mongodb://localhost:27017/resena_db`

Con Atlas, define la variable antes de levantar el servicio. La cadena debe incluir `/resena_db`. No la subas a Git.

```powershell
$env:MONGODB_URI = "mongodb+srv://USUARIO:CLAVE@CLUSTER.mongodb.net/resena_db"
```

## 2. Levantar

```powershell
.\mvnw.cmd spring-boot:run
```

- Salud: http://localhost:8088/actuator/health (`UP` solo si MongoDB responde)
- Swagger: http://localhost:8088/swagger-ui/index.html

## 3. Probar

Crear una reseña (201):

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8088/api/resenas -ContentType "application/json; charset=utf-8" -Body '{"productoId":1,"autor":"Camila","estrellas":5,"comentario":"Muy buen teclado"}'
```

Listar todas, o solo las de un producto (200):

```powershell
Invoke-RestMethod http://localhost:8088/api/resenas
```

```powershell
Invoke-RestMethod "http://localhost:8088/api/resenas?productoId=1"
```

| Caso | Código |
|---|---|
| `estrellas` fuera de 1 a 5, o textos vacíos | 400 |
| El producto no existe | 404 |
| `producto-service` o MongoDB apagados | 503 |

### Ver los datos

En Compass: conecta a `mongodb://localhost:27017` y abre `resena_db` → `resenas`. En la pestaña de consultas puedes filtrar:

```json
{ "estrellas": { "$gte": 4 } }
```

## 4. PostgreSQL y MongoDB, lado a lado

| Concepto | `producto-service` | `resena-service` |
|---|---|---|
| Se guarda en | Tabla con filas | Colección con documentos |
| Clase | `@Entity` + `@Table` | `@Document` |
| Identificador | `Long` autoincremental | `String` (ObjectId) |
| Repositorio | `JpaRepository` | `MongoRepository` |
| Esquema | Scripts de Flyway | No hay: se crea al guardar |
| Reglas | Builder y `CHECK` en la base | Solo el Builder |
| Cambios | `@Transactional` los guarda al terminar | Hay que llamar a `save()` |

## 5. En la nube

Mismo `Dockerfile` que los otros servicios. Variables en Render:

| Variable | Valor |
|---|---|
| `MONGODB_URI` | Cadena de Atlas (con `/resena_db`) |
| `PRODUCTO_SERVICE_URL` | URL pública de `producto-service` |
| `CORS_ORIGINS` | URL del front |
