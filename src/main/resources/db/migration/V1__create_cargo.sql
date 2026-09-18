
CREATE TABLE IF NOT EXISTS Cargo(
    CargoId integer GENERATED ALWAYS AS IDENTITY primary key not null,
    CargoNome varchar(250) not null
    );