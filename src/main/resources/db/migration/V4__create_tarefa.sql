
CREATE TABLE IF NOT EXISTS Tarefa(
    TarefaId integer GENERATED ALWAYS AS IDENTITY primary key not null,
    TarefaTitulo varchar(100) not null,
    TarefaDescricao varchar(500),
    TarefaStatus varchar(50) not null,
    TarefaPrioridade varchar(250) not null,
    TarefaResponsavelId integer not null,
    TarefaCriadorId integer not null,
    TarefaPrazo TIMESTAMP,
    TarefaCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    TarefaCriadopor integer not null,
    TarefaAtualizadopor integer,
    TarefaProjetoId integer not null,
    TarefaAtualizadoEm TIMESTAMP,
    TarefaConcluidoEm TIMESTAMP,
    CONSTRAINT fk_projeto_relacionado foreign key (TarefaProjetoId) REFERENCES projeto(ProjetoId),
    CONSTRAINT fk_tarefa_responsavel foreign key (TarefaResponsavelId) REFERENCES usuario(UsuarioId),
    CONSTRAINT fk_tarefa_criador foreign key (TarefaCriadorId) REFERENCES usuario(UsuarioId)

    );