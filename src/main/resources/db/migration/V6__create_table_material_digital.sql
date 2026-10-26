CREATE TABLE material_digital (
    id           BIGSERIAL PRIMARY KEY,
    titulo       VARCHAR(200) NOT NULL,
    tipo         VARCHAR(20)  NOT NULL,
    url          VARCHAR(500) NOT NULL,
    descricao    TEXT,
    serie        INTEGER      NOT NULL,
    categoria_id BIGINT       NOT NULL,
    livro_id     BIGINT,
    CONSTRAINT fk_material_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id),
    CONSTRAINT fk_material_livro FOREIGN KEY (livro_id) REFERENCES livro (id)
);
