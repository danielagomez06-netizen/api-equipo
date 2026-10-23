CREATE TABLE programas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    codigo VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uq_programas_nombre UNIQUE (nombre),
    CONSTRAINT uq_programas_codigo UNIQUE (codigo)
);

CREATE TABLE salas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    capacidad INT NOT NULL,
    ubicacion VARCHAR(150),
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uq_salas_nombre UNIQUE (nombre),
    CONSTRAINT chk_salas_capacidad CHECK (capacidad > 0)
);

CREATE TABLE franjas_horarias (
    id BIGINT NOT NULL AUTO_INCREMENT,
    dia_semana VARCHAR(10) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT chk_franjas_horas CHECK (hora_fin > hora_inicio)
);

CREATE TABLE docentes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uq_docentes_correo UNIQUE (correo)
);

CREATE TABLE docente_programa (
    docente_id BIGINT NOT NULL,
    programa_id BIGINT NOT NULL,
    PRIMARY KEY (docente_id, programa_id),
    CONSTRAINT fk_dp_docente
        FOREIGN KEY (docente_id) REFERENCES docentes(id),
    CONSTRAINT fk_dp_programa
        FOREIGN KEY (programa_id) REFERENCES programas(id)
);

CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    codigo VARCHAR(30) NOT NULL,
    docente_id BIGINT NOT NULL,
    programa_id BIGINT NOT NULL,
    semestre INT NOT NULL,
    cantidad_estudiantes INT NOT NULL,
    asignable BOOLEAN NOT NULL DEFAULT TRUE,
    motivo_no_asignable VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    CONSTRAINT uq_cursos_codigo UNIQUE (codigo),
    CONSTRAINT fk_cursos_docente
        FOREIGN KEY (docente_id) REFERENCES docentes(id),
    CONSTRAINT fk_cursos_programa
        FOREIGN KEY (programa_id) REFERENCES programas(id),
    CONSTRAINT chk_cursos_semestre CHECK (semestre > 0),
    CONSTRAINT chk_cursos_estudiantes CHECK (cantidad_estudiantes > 0)
);