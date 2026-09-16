
CREATE TABLE Usuario(
    UsuarioId integer GENERATED ALWAYS AS IDENTITY  primary key not null,
    UsuarioNome varchar(250) not null,
    UsuarioSenha varchar(250) not null,
    UsuarioAvatarUrl varchar(250),
    UsuarioCargo varchar(250) not null,
    UsuarioAtivo BOOLEAN not null DEFAULT TRUE,
    UsuarioXp integer not null DEFAULT 0,
    UsuarioNivel integer not null DEFAULT 1,
    UsuarioCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    UsuarioAtualizadoEm TIMESTAMP

);
