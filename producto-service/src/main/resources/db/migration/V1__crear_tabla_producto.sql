-- V1: tabla de productos (esta base pertenece SOLO a producto-service)
CREATE TABLE producto (
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100)   NOT NULL,
    precio NUMERIC(10, 2) NOT NULL,
    stock  INTEGER        NOT NULL,

    CONSTRAINT ck_producto_precio CHECK (precio > 0),
    CONSTRAINT ck_producto_stock CHECK (stock >= 0)
);
