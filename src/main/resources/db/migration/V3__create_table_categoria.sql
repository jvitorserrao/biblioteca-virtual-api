CREATE TABLE categoria (
    id                BIGSERIAL PRIMARY KEY,
    nome              VARCHAR(100) NOT NULL,
    descricao         VARCHAR(255),
    area_conhecimento VARCHAR(100) NOT NULL,
    ativa             BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT uk_categoria_nome UNIQUE (nome)
);
