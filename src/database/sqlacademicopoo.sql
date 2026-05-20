-- Created by Redgate Data Modeler (https://datamodeler.redgate-platform.com)
-- Last modification date: 2026-05-17


CREATE TABLE tb_curso (
    cod_curso  int  NOT NULL AUTO_INCREMENT,
    nome_curso enum(
        'ANALISE E DESENVOLVIMENTO DE SISTEMAS',
        'CIENCIA DA COMPUTACAO',
        'ENGENHARIA DE SOFTWARE'
    ) NOT NULL,
    campus  varchar(30)  NOT NULL,
    periodo enum('MATUTINO','VESPERTINO','NOTURNO')  NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX uq_curso_horario_campus (nome_curso, campus, periodo),
    CONSTRAINT tb_curso_pk PRIMARY KEY (cod_curso)
);

CREATE TABLE tb_aluno (
    rgm             char(8)      NOT NULL CHECK (rgm REGEXP '^[0-9]{8}$'),
    fk_cod_curso    int          NOT NULL,
    nome            varchar(120) NOT NULL,
    cpf             varchar(14)  NOT NULL CHECK (cpf REGEXP '^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$'),
    data_nascimento date         NOT NULL,
    email           varchar(100) NOT NULL,
    endereco        varchar(150) NOT NULL,
    municipio       varchar(80)  NOT NULL,
    uf              char(2)      NOT NULL,
    celular         varchar(15)  NOT NULL CHECK (celular REGEXP '^\\([0-9]{2}\\)[0-9]{4,5}-[0-9]{4}$'),
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX uq_cpf (cpf),
    CONSTRAINT tb_aluno_pk PRIMARY KEY (rgm)
);

CREATE TABLE tb_disciplina (
    cod_disciplina  int  NOT NULL AUTO_INCREMENT,
    nome_disciplina enum(
        'PROGRAMACAO ORIENTADA A OBJETOS',
        'BANCO DE DADOS',
        'CALCULO DIFERENCIAL E INTEGRAL II',
        'ESTRUTURA DE DADOS'
    ) NOT NULL,
    fk_cod_curso  int  NOT NULL,
    carga_horaria int  NOT NULL,
    creditos      int  NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT tb_disciplina_pk PRIMARY KEY (cod_disciplina)
);

CREATE TABLE tb_nota_falta (
    pk_cod_nota_falta int          NOT NULL AUTO_INCREMENT,
    fk_rgm            char(8)      NOT NULL,
    fk_cod_disciplina int          NOT NULL,
    semestre          varchar(6)   NOT NULL,
    nota              decimal(4,2) NOT NULL CHECK (nota >= 0.00 AND nota <= 10.00),
    faltas            int          NOT NULL CHECK (faltas >= 0),
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX uq_aluno_disciplina_semestre (fk_rgm, fk_cod_disciplina, semestre),
    INDEX idx_nota_falta_rgm        (fk_rgm),
    INDEX idx_nota_falta_disciplina (fk_cod_disciplina),
    CONSTRAINT tb_nota_falta_pk PRIMARY KEY (pk_cod_nota_falta)
);



ALTER TABLE tb_aluno ADD CONSTRAINT fk_id_curso_aluno
    FOREIGN KEY fk_id_curso_aluno (fk_cod_curso)
    REFERENCES tb_curso (cod_curso)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE tb_disciplina ADD CONSTRAINT fk_id_curso_disciplina
    FOREIGN KEY fk_id_curso_disciplina (fk_cod_curso)
    REFERENCES tb_curso (cod_curso)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE tb_nota_falta ADD CONSTRAINT fk_id_disciplina_nota_falta
    FOREIGN KEY fk_id_disciplina_nota_falta (fk_cod_disciplina)
    REFERENCES tb_disciplina (cod_disciplina)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE tb_nota_falta ADD CONSTRAINT fk_rgm_nota_falta
    FOREIGN KEY fk_rgm_nota_falta (fk_rgm)
    REFERENCES tb_aluno (rgm)
    ON DELETE CASCADE
    ON UPDATE CASCADE;



DELIMITER $$

-- Valida data_nascimento no INSERT (CURRENT_DATE não é suportado em CHECK no MySQL)
CREATE TRIGGER trg_valida_nascimento_insert
BEFORE INSERT ON tb_aluno
FOR EACH ROW
BEGIN
    IF NEW.data_nascimento > CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'data_nascimento não pode ser uma data futura.';
    END IF;
END$$

-- Valida data_nascimento no UPDATE
CREATE TRIGGER trg_valida_nascimento_update
BEFORE UPDATE ON tb_aluno
FOR EACH ROW
BEGIN
    IF NEW.data_nascimento > CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'data_nascimento não pode ser uma data futura.';
    END IF;
END$$

-- Valida semestre (formato YYYY-1 ou YYYY-2) e arredonda nota no INSERT
-- Arredondamento: CEIL(nota - 0.25) → >= x.75 sobe para o inteiro seguinte
--   Ex.: 1.75 → CEIL(1.50) = 2  |  1.74 → CEIL(1.49) = 1
CREATE TRIGGER trg_nota_falta_insert
BEFORE INSERT ON tb_nota_falta
FOR EACH ROW
BEGIN
    IF NEW.semestre NOT REGEXP '^[0-9]{4}-[12]$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Semestre inválido. Use o formato YYYY-1 ou YYYY-2.';
    END IF;

    IF NEW.nota < 0 OR NEW.nota > 10 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nota deve estar entre 0 e 10.';
    END IF;

    SET NEW.nota = CEIL(NEW.nota - 0.25);
END$$

-- Valida semestre e arredonda nota no UPDATE
CREATE TRIGGER trg_nota_falta_update
BEFORE UPDATE ON tb_nota_falta
FOR EACH ROW
BEGIN
    IF NEW.semestre NOT REGEXP '^[0-9]{4}-[12]$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Semestre inválido. Use o formato YYYY-1 ou YYYY-2.';
    END IF;

    IF NEW.nota < 0 OR NEW.nota > 10 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Nota deve estar entre 0 e 10.';
    END IF;

    SET NEW.nota = CEIL(NEW.nota - 0.25);
END$$

DELIMITER ;


CREATE VIEW vw_boletim AS
SELECT
    a.rgm,
    a.nome,
    c.nome_curso,
    d.nome_disciplina,
    nf.semestre,
    nf.nota,
    nf.faltas,
    CASE
        WHEN nf.nota >= 5.00 AND nf.faltas <= 15 THEN 'Aprovado'
        ELSE 'Reprovado'
    END AS situacao
FROM tb_nota_falta nf
JOIN tb_aluno      a ON a.rgm            = nf.fk_rgm
JOIN tb_disciplina d ON d.cod_disciplina = nf.fk_cod_disciplina
JOIN tb_curso      c ON c.cod_curso      = a.fk_cod_curso;

-- End of file.