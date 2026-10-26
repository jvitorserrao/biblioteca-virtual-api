CREATE TABLE livro (
    id             BIGSERIAL PRIMARY KEY,
    titulo         VARCHAR(200) NOT NULL,
    isbn           VARCHAR(13)  NOT NULL,
    ano_publicacao INTEGER      NOT NULL,
    numero_paginas INTEGER,
    sinopse        TEXT,
    link_acesso    VARCHAR(500),
    autor_id       BIGINT       NOT NULL,
    editora_id     BIGINT       NOT NULL,
    categoria_id   BIGINT       NOT NULL,
    CONSTRAINT uk_livro_isbn UNIQUE (isbn),
    CONSTRAINT fk_livro_autor FOREIGN KEY (autor_id) REFERENCES autor (id),
    CONSTRAINT fk_livro_editora FOREIGN KEY (editora_id) REFERENCES editora (id),
    CONSTRAINT fk_livro_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);
