CREATE TABLE editora (
    id     BIGSERIAL PRIMARY KEY,
    nome   VARCHAR(150) NOT NULL,
    cnpj   VARCHAR(14)  NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    site   VARCHAR(255),
    CONSTRAINT uk_editora_cnpj UNIQUE (cnpj)
);
