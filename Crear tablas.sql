CREATE TABLE estudiantes (
  id              INT           NOT NULL    IDENTITY    PRIMARY KEY,
  name           VARCHAR(100)  NOT NULL,
  lastname  VARCHAR(100),
);
create table cursos (
	id              INT           NOT NULL    IDENTITY    PRIMARY KEY,
  name           VARCHAR(100)  NOT NULL
);
CREATE TABLE notas
(
    estudianteID INT NOT NULL,
	cursoID INT NOT NULL,
	nota FLOAT,
	CONSTRAINT PK_EstudiantesCursos PRIMARY KEY CLUSTERED (estudianteID, cursoID)
);