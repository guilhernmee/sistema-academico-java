-- =============================================================
-- Script DDL — Sistema Acadêmico POO
-- Gerado/corrigido com base nos arquivos Java do projeto:
--   Aluno.java · AlunoDAO.java · NotaFaltaDAO.java
--   Curso.java · CursoDAO.java · DisciplinaDAO.java
--   NotaFalta.java · ConnectionFactory.java · JanelaTeste.java
--
-- CORREÇÕES aplicadas em relação ao SQL original:
--   1. rgm e fk_rgm: char(8) → INT  (Aluno.java usa int; AlunoDAO usa setInt/getInt)
--   2. Banco chamado "academico"     (ConnectionFactory aponta para /academico)
--   3. Removido CHECK de REGEXP em rgm (incompatível com INT)
--   4. Mantidos todos os demais CHECKs, índices, FKs e triggers originais
--   5. Adicionados triggers de tb_aluno presentes no original
--   6. Mantida a view vw_boletim usada pela aba Boletim da JanelaTeste
-- =============================================================

CREATE DATABASE IF NOT EXISTS academico
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE academico;


-- -------------------------------------------------------------
-- 1. tb_curso
--    • AUTO_INCREMENT em cod_curso (CursoDAO.salvar recupera getGeneratedKeys)
--    • UNIQUE (nome_curso, campus, periodo) — JanelaTeste.obterOuCriarCurso
--      verifica se já existe antes de inserir
-- -------------------------------------------------------------
CREATE TABLE tb_curso (
                          cod_curso   INT          NOT NULL AUTO_INCREMENT,
                          nome_curso  ENUM(
        'ANALISE E DESENVOLVIMENTO DE SISTEMAS',
        'CIENCIA DA COMPUTACAO',
        'ENGENHARIA DE SOFTWARE'
    )                        NOT NULL,
                          campus      VARCHAR(30)  NOT NULL,
                          periodo     ENUM('MATUTINO','VESPERTINO','NOTURNO') NOT NULL,
                          created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
                          updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          UNIQUE INDEX uq_curso_horario_campus (nome_curso, campus, periodo),
                          CONSTRAINT tb_curso_pk PRIMARY KEY (cod_curso)
);


-- -------------------------------------------------------------
-- 2. tb_aluno
--    • rgm: INT (Aluno.java declara "private int rgm")
--    • AlunoDAO usa stmt.setInt(1, aluno.getRgm()) e rs.getInt("rgm")
--    • CHECK removido do rgm pois REGEXP não se aplica a INT;
--      a validação "[1-9][0-9]{7}" já é feita em JanelaTeste.lerRgm()
--    • Demais CHECKs (cpf, celular) e UNIQUE (cpf) mantidos
-- -------------------------------------------------------------
CREATE TABLE tb_aluno (
                          rgm              INT           NOT NULL,
                          fk_cod_curso     INT           NOT NULL,
                          nome             VARCHAR(120)  NOT NULL,
                          cpf              VARCHAR(14)   NOT NULL
                              CHECK (cpf REGEXP '^[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}$'),
    data_nascimento  DATE          NOT NULL,
    email            VARCHAR(100)  NOT NULL,
    endereco         VARCHAR(150)  NOT NULL,
    municipio        VARCHAR(80)   NOT NULL,
    uf               CHAR(2)       NOT NULL,
    celular          VARCHAR(15)   NOT NULL
                         CHECK (celular REGEXP '^\\([0-9]{2}\\)[0-9]{4,5}-[0-9]{4}$'),
    created_at  DATETIME  DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE INDEX uq_cpf (cpf),
    CONSTRAINT tb_aluno_pk PRIMARY KEY (rgm)
);


-- -------------------------------------------------------------
-- 3. tb_disciplina
--    • DisciplinaDAO.salvar insere carga_horaria=80 e creditos=4 (fixos)
--    • AUTO_INCREMENT em cod_disciplina (getGeneratedKeys no DAO)
--    • JanelaTeste.obterOuCriarDisciplina verifica existência antes de inserir
-- -------------------------------------------------------------
CREATE TABLE tb_disciplina (
                               cod_disciplina   INT  NOT NULL AUTO_INCREMENT,
                               nome_disciplina  ENUM(
        'PROGRAMACAO ORIENTADA A OBJETOS',
        'BANCO DE DADOS',
        'CALCULO DIFERENCIAL E INTEGRAL II',
        'ESTRUTURA DE DADOS'
    )                     NOT NULL,
                               fk_cod_curso  INT     NOT NULL,
                               carga_horaria INT     NOT NULL,   -- DisciplinaDAO insere 80
                               creditos      INT     NOT NULL,   -- DisciplinaDAO insere 4
                               created_at  DATETIME  DEFAULT CURRENT_TIMESTAMP,
                               updated_at  DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               CONSTRAINT tb_disciplina_pk PRIMARY KEY (cod_disciplina)
);


-- -------------------------------------------------------------
-- 4. tb_nota_falta
--    • fk_rgm: INT — NotaFaltaDAO usa stmt.setInt(1, notaFalta.getAluno().getRgm())
--    • UNIQUE (fk_rgm, fk_cod_disciplina, semestre) — JanelaTeste.salvarNota()
--      bloqueia duplicata antes de chamar o DAO
--    • CHECK nota e faltas mantidos (trigger também valida)
-- -------------------------------------------------------------
CREATE TABLE tb_nota_falta (
                               pk_cod_nota_falta  INT           NOT NULL AUTO_INCREMENT,
                               fk_rgm             INT           NOT NULL,
                               fk_cod_disciplina  INT           NOT NULL,
                               semestre           VARCHAR(6)    NOT NULL,
                               nota               DECIMAL(4,2)  NOT NULL
                                   CHECK (nota >= 0.00 AND nota <= 10.00),
                               faltas             INT           NOT NULL
                                   CHECK (faltas >= 0),
                               created_at  DATETIME  DEFAULT CURRENT_TIMESTAMP,
                               updated_at  DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                               UNIQUE INDEX uq_aluno_disciplina_semestre (fk_rgm, fk_cod_disciplina, semestre),
                               INDEX idx_nota_falta_rgm        (fk_rgm),
                               INDEX idx_nota_falta_disciplina (fk_cod_disciplina),
                               CONSTRAINT tb_nota_falta_pk PRIMARY KEY (pk_cod_nota_falta)
);


-- -------------------------------------------------------------
-- 5. Foreign Keys
--    • ON DELETE RESTRICT / ON UPDATE CASCADE em tb_aluno e tb_disciplina
--    • ON DELETE CASCADE em tb_nota_falta → tb_aluno
--      (JanelaTeste avisa: "Esta ação também excluirá todas as suas notas")
-- -------------------------------------------------------------
ALTER TABLE tb_aluno
    ADD CONSTRAINT fk_id_curso_aluno
        FOREIGN KEY fk_id_curso_aluno (fk_cod_curso)
    REFERENCES tb_curso (cod_curso)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE tb_disciplina
    ADD CONSTRAINT fk_id_curso_disciplina
        FOREIGN KEY fk_id_curso_disciplina (fk_cod_curso)
    REFERENCES tb_curso (cod_curso)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE tb_nota_falta
    ADD CONSTRAINT fk_id_disciplina_nota_falta
        FOREIGN KEY fk_id_disciplina_nota_falta (fk_cod_disciplina)
    REFERENCES tb_disciplina (cod_disciplina)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;

ALTER TABLE tb_nota_falta
    ADD CONSTRAINT fk_rgm_nota_falta
        FOREIGN KEY fk_rgm_nota_falta (fk_rgm)
    REFERENCES tb_aluno (rgm)          -- referencia INT (corrigido)
    ON DELETE CASCADE
    ON UPDATE CASCADE;


-- -------------------------------------------------------------
-- 6. Triggers
-- -------------------------------------------------------------
DELIMITER $$

-- Valida data_nascimento no INSERT
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

-- Valida semestre e arredonda nota no INSERT — apenas 4.5 sobe para 5
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

    IF NEW.nota = 4.5 THEN
        SET NEW.nota = 5;
END IF;
END$$

-- Valida semestre e arredonda nota no UPDATE — apenas 4.5 sobe para 5
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

    IF NEW.nota = 4.5 THEN
        SET NEW.nota = 5;
END IF;
END$$

DELIMITER ;


-- -------------------------------------------------------------
-- 7. View vw_boletim
--    Usada pela aba "Boletim" da JanelaTeste (buscarBoletim via NotaFaltaDAO)
--    Critério: nota >= 5.00 E faltas <= 15 → Aprovado
-- -------------------------------------------------------------
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


-- -------------------------------------------------------------
-- 8. Usuário do banco (ConnectionFactory: user=academico, pass=1234)
-- -------------------------------------------------------------
CREATE USER IF NOT EXISTS 'academico'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON academico.* TO 'academico'@'localhost';
FLUSH PRIVILEGES;

-- End of file.