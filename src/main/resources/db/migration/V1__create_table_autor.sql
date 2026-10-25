CREATE TABLE autor (
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(150) NOT NULL,
    nacionalidade   VARCHAR(80)  NOT NULL,
    data_nascimento DATE,
    biografia       TEXT
);
