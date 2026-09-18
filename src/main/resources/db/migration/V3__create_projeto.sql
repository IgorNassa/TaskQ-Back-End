
CREATE TABLE IF NOT EXISTS Projeto(
    ProjetoId integer GENERATED ALWAYS AS IDENTITY primary key not null,
    ProjetoNome varchar(250) not null,
    ProjetoDescricao varchar(500),
    ProjetoStatus varchar(100) not null,
    ProjetoPrioridade varchar(100) not null,
    ProjetoDataInicio TIMESTAMP,
    ProjetoPrazo TIMESTAMP,
    ProjetoProprietarioId integer not null,
    ProjetoCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    ProjetoAtualizadoEm TIMESTAMP,
    CONSTRAINT fk_projeto_proprietario_id FOREIGN KEY (ProjetoProprietarioId) REFERENCES usuario(UsuarioId)
    );
