
CREATE DATABASE catalogo_db;


CREATE TABLE genero (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE filme (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    diretor VARCHAR(100),
    ano INT NOT NULL,
    nota NUMERIC(3,1),
    genero_id INT NOT NULL REFERENCES genero(id),
    CONSTRAINT chk_nota CHECK (nota IS NULL OR (nota >= 0 AND nota <= 10))
);


INSERT INTO genero (nome) VALUES ('Ação'), ('Comédia'), ('Drama'), ('Ficção Científica');
