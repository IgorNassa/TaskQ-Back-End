
CREATE TABLE TransacaoXp(
    TransacaoXpId integer GENERATED ALWAYS AS IDENTITY primary key not null,
    TransacaoXpUsuarioId integer not null,
    TransacaoXpQuantidade integer not null,
    TransacaoXpMotivo varchar(250) not null,
    TransacaoXpTipoReferencia varchar(100),
    TransacaoXpReferenciaId integer,
    TransacaoXpCriadoEm TIMESTAMP not null DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transacao_xp_usuario foreign key (TransacaoXpUsuarioId) REFERENCES usuario(UsuarioId)
);