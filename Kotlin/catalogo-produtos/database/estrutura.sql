SET ROLE "catalogo-produtos";
SET STATEMENT_TIMEOUT TO '300s';

CREATE TABLE produto
(
    id uuid not null
        constraint produto_pk
            primary key,
    nome varchar(50),
    sku varchar(50),
    descricao varchar(100),
    unidade varchar(2),
    peso decimal,
    estoque decimal,
    status varchar(10)
);

ALTER TABLE produto
    owner to "catalogo-produtos";
