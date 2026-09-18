
CREATE TABLE IF NOT EXISTS Usuario(
    UsuarioId integer GENERATED ALWAYS AS IDENTITY  primary key not null,
    UsuarioNome varchar(250) not null,
    UsuarioEmail varchar(250) not null unique,
    UsuarioSenhaRest varchar(250) not null unique,
    UsuarioAvatarUrl varchar(500),
    UsuarioCargoID integer not null,
    UsuarioAtivo varchar(250) not null,
    UsuarioXp integer not null DEFAULT 0,
    UsuarioElo varchar(50) not null,
    UsuarioCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    UsuarioAtualizadoEm TIMESTAMP,
    UsuarioCriadopor integer not null,
    UsuarioAtualizadopor integer,
    CONSTRAINT fk_Cargo_escolhido foreign key (UsuarioCargoID) REFERENCES cargo(CargoId)

);