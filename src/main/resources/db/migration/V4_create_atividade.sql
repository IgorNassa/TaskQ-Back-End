
CREATE TABLE Atividade(
    AtividadeId integer GENERATED ALWAYS AS IDENTITY primary key not null,
    AtividadeUsuarioId integer not null,
    AtividadeTipo varchar(100) not null,
    AtividadeDescricao varchar(500),
    AtividadeTipoEntidade varchar(100),
    AtividadeEntidadeId integer,
    AtividadeCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atividade_usuario foreign key (AtividadeUsuarioId) REFERENCES usuario(UsuarioId)
);