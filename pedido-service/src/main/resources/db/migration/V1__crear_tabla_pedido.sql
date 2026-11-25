-- V1: tabla de pedidos (esta base pertenece SOLO a pedido-service).
-- OJO: producto_id NO es FOREIGN KEY: el producto vive en la base de OTRO microservicio.
CREATE TABLE pedido (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    producto_id     BIGINT         NOT NULL,
    nombre_producto VARCHAR(100)   NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL,
    cantidad        INTEGER        NOT NULL,
    total           NUMERIC(12, 2) NOT NULL,
    creado_en       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT ck_pedido_cantidad CHECK (cantidad >= 1)
);
