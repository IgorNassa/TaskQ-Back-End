CREATE TABLE Auditoria(
    AuditoriaId integer GENERATED ALWAYS AS IDENTITY primary key not null,
    AuditoriaUsuarioId integer not null,
    AuditoriaAcao varchar(250) not null,
    AuditoriaTipoEntidade varchar(250) not null,
    AuditoriaEntidadeId integer not null,
    AuditoriaValorAntigo varchar(250),
    AuditoriaValorNovo varchar(250),
    AuditoriaCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_auditoria_usuario foreign key (AuditoriaUsuarioId) REFERENCES usuario(UsuarioId)
);