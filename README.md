# 🎓 Sistema Acadêmico — Java + MySQL

> Sistema de cadastro acadêmico desenvolvido em **Java (Swing)** com banco de dados **MySQL**, como parte de atividade avaliativa da disciplina de Programação Orientada a Objetos.

---

## 📋 Sobre o Projeto

O Sistema Acadêmico permite o gerenciamento completo de alunos, incluindo dados pessoais, informações de curso, notas, faltas e boletim. A interface gráfica foi desenvolvida com **Java Swing** utilizando o componente **JTabbedPane** para organização em abas, e o banco de dados relacional **MySQL** para persistência dos dados.

---

## 🖥️ Telas do Sistema

O sistema é organizado em abas e menus, conforme as interfaces abaixo:

### Aba — Dados Pessoais
Campos disponíveis:
- RGM, Nome
- Data de Nascimento, CPF *(JFormattedTextField)*
- E-mail, Endereço
- Município, UF, Celular *(JFormattedTextField)*

### Aba — br.com.academico.model.Curso
Campos disponíveis:
- br.com.academico.model.Curso *(ComboBox)*
- Campus *(ComboBox)*
- Período: Matutino / Vespertino / Noturno *(RadioButton)*

### Aba — Notas e Faltas
Campos disponíveis:
- RGM, Nome do aluno *(somente leitura)*
- br.com.academico.model.Disciplina *(ComboBox)*
- Semestre, Nota, Faltas

### Aba — Boletim
Exibe um resumo com:
- RGM, Nome, br.com.academico.model.Curso
- Disciplinas, Notas e Faltas do aluno

---

## 🗂️ Menus do Sistema

| Menu | Opções |
|------|--------|
| **br.com.academico.model.Aluno** | Salvar, Alterar, Consultar, Excluir, Sair |
| **Notas e Faltas** | Lançar |
| **Ajuda** | Sobre |

---

## 🗄️ Banco de Dados

O banco de dados foi modelado com as seguintes diretrizes:

- Banco de dados: **`academico`**
- Usuário padrão: **`academico`** / Senha: **`1234`**
- Uso de **PRIMARY KEY (PK)** em todas as tabelas
- Uso de **FOREIGN KEY (FK)** para garantir integridade referencial
- **Exclusão em cascata**: ao excluir um aluno, suas notas e faltas são removidas automaticamente
- **Restrição de unicidade**: não é permitido cadastrar dois alunos com o mesmo RGM ou CPF
- **Triggers** para validação de data de nascimento e formato do semestre
- **View `vw_boletim`** para consolidação dos dados do boletim

### Tabelas

```sql
-- Tabela de Cursos
CREATE TABLE tb_curso (
    cod_curso   INT NOT NULL AUTO_INCREMENT,
    nome_curso  ENUM(
        'ANALISE E DESENVOLVIMENTO DE SISTEMAS',
        'CIENCIA DA COMPUTACAO',
        'ENGENHARIA DE SOFTWARE'
    ) NOT NULL,
    campus      VARCHAR(30) NOT NULL,
    periodo     ENUM('MATUTINO','VESPERTINO','NOTURNO') NOT NULL,
    CONSTRAINT tb_curso_pk PRIMARY KEY (cod_curso),
    UNIQUE INDEX uq_curso_horario_campus (nome_curso, campus, periodo)
);

-- Tabela de Alunos
CREATE TABLE tb_aluno (
    rgm              INT          NOT NULL,
    fk_cod_curso     INT          NOT NULL,
    nome             VARCHAR(120) NOT NULL,
    cpf              VARCHAR(14)  NOT NULL,
    data_nascimento  DATE         NOT NULL,
    email            VARCHAR(100) NOT NULL,
    endereco         VARCHAR(150) NOT NULL,
    municipio        VARCHAR(80)  NOT NULL,
    uf               CHAR(2)      NOT NULL,
    celular          VARCHAR(15)  NOT NULL,
    CONSTRAINT tb_aluno_pk PRIMARY KEY (rgm),
    UNIQUE INDEX uq_cpf (cpf)
);

-- Tabela de Disciplinas
CREATE TABLE tb_disciplina (
    cod_disciplina   INT NOT NULL AUTO_INCREMENT,
    nome_disciplina  ENUM(
        'PROGRAMACAO ORIENTADA A OBJETOS',
        'BANCO DE DADOS',
        'CALCULO DIFERENCIAL E INTEGRAL II',
        'ESTRUTURA DE DADOS'
    ) NOT NULL,
    fk_cod_curso   INT NOT NULL,
    carga_horaria  INT NOT NULL,
    creditos       INT NOT NULL,
    CONSTRAINT tb_disciplina_pk PRIMARY KEY (cod_disciplina)
);

-- Tabela de Notas e Faltas
CREATE TABLE tb_nota_falta (
    pk_cod_nota_falta  INT          NOT NULL AUTO_INCREMENT,
    fk_rgm             INT          NOT NULL,
    fk_cod_disciplina  INT          NOT NULL,
    semestre           VARCHAR(6)   NOT NULL,
    nota               DECIMAL(4,2) NOT NULL,
    faltas             INT          NOT NULL,
    CONSTRAINT tb_nota_falta_pk PRIMARY KEY (pk_cod_nota_falta),
    UNIQUE INDEX uq_aluno_disciplina_semestre (fk_rgm, fk_cod_disciplina, semestre)
);
```

### Relacionamentos (Foreign Keys)

| Tabela | FK | Referência | ON DELETE | ON UPDATE |
|--------|-----|-----------|-----------|-----------|
| `tb_aluno` | `fk_cod_curso` | `tb_curso.cod_curso` | RESTRICT | CASCADE |
| `tb_disciplina` | `fk_cod_curso` | `tb_curso.cod_curso` | RESTRICT | CASCADE |
| `tb_nota_falta` | `fk_cod_disciplina` | `tb_disciplina.cod_disciplina` | RESTRICT | CASCADE |
| `tb_nota_falta` | `fk_rgm` | `tb_aluno.rgm` | **CASCADE** | CASCADE |

### Triggers

| Trigger | Tabela | Evento | Função |
|---------|--------|--------|--------|
| `trg_valida_nascimento_insert` | `tb_aluno` | BEFORE INSERT | Rejeita data de nascimento futura |
| `trg_valida_nascimento_update` | `tb_aluno` | BEFORE UPDATE | Rejeita data de nascimento futura |
| `trg_nota_falta_insert` | `tb_nota_falta` | BEFORE INSERT | Valida semestre e arredonda nota |
| `trg_nota_falta_update` | `tb_nota_falta` | BEFORE UPDATE | Valida semestre e arredonda nota |

> **Arredondamento de nota:** `CEIL(nota - 0.25)` — ex: 7,75 → 8 | 7,74 → 7

### View

```sql
-- vw_boletim: usada pela aba Boletim para consolidar os dados do aluno
-- Critério de aprovação: nota >= 5.00 E faltas <= 15
CREATE VIEW vw_boletim AS
SELECT a.rgm, a.nome, c.nome_curso, d.nome_disciplina,
       nf.semestre, nf.nota, nf.faltas,
       CASE WHEN nf.nota >= 5.00 AND nf.faltas <= 15
            THEN 'Aprovado' ELSE 'Reprovado'
       END AS situacao
FROM tb_nota_falta nf
JOIN tb_aluno      a ON a.rgm            = nf.fk_rgm
JOIN tb_disciplina d ON d.cod_disciplina = nf.fk_cod_disciplina
JOIN tb_curso      c ON c.cod_curso      = a.fk_cod_curso;
```

---

## ⚙️ Tecnologias Utilizadas

| Tecnologia | Finalidade |
|------------|-----------|
| Java (JDK 8+) | Linguagem principal |
| Java Swing | Interface gráfica (GUI) |
| JTabbedPane | Organização em abas |
| JFormattedTextField | Campos CPF, Celular e Data de Nascimento |
| MySQL | Banco de dados relacional |
| JDBC | Conexão Java ↔ MySQL |

---

## 📁 Estrutura do Projeto

```
SistemaAcademico/
├── src/
│   ├── model/
│   │   ├── br.com.academico.model.Aluno.java
│   │   ├── br.com.academico.model.Curso.java
│   │   └── NotasFaltas.java
│   ├── dao/
│   │   ├── AlunoDAO.java
│   │   ├── CursoDAO.java
│   │   ├── DisciplinaDAO.java
│   │   ├── NotaFaltaDAO.java
│   │   └── BoletimDAO.java
│   ├── view/
│   │   ├── JanelaTeste.java
│   └── util/
│       └── Conexao.java
└── database/
    └── script.sql
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java JDK 8 ou superior
- MySQL Server instalado e em execução
- IDE de sua preferência (IntelliJ, Eclipse, NetBeans)
- Driver JDBC do MySQL (`mysql-connector-java`)

### Passos

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/sistema-academico-java.git
   ```

2. **Configure o banco de dados**
   ```sql
   CREATE DATABASE IF NOT EXISTS academico;
   ```
   Execute o script localizado em `database/script.sql`

3. **Configure a conexão**

   Edite o arquivo `Conexao.java` com suas credenciais:
   ```java
   private static final String URL     = "jdbc:mysql://localhost:3306/academico";
   private static final String USUARIO = "academico";
   private static final String SENHA   = "1234";
   ```

4. **Adicione o MySQL Connector/J**

   No IntelliJ: `File → Project Structure → Libraries → + → From Maven`
   Pesquise: `mysql:mysql-connector-java:8.0.33`

5. **Execute o projeto**

   Compile e rode a classe `TelaPrincipal.java`

---

## ✅ Funcionalidades

- [x] Cadastro de alunos com validação de RGM único
- [x] Campos CPF, Celular e Data de Nascimento com `JFormattedTextField`
- [x] Organização da interface com `JTabbedPane`
- [x] Seleção de curso, campus e período
- [x] Lançamento de notas e faltas por disciplina e semestre
- [x] Boletim com consolidação dos dados do aluno
- [x] Operações: Salvar, Alterar, Consultar e Excluir
- [x] Exclusão em cascata de notas e faltas ao remover aluno
- [x] Validação de data de nascimento via trigger no banco
- [x] Arredondamento automático de nota via trigger no banco

---

## 👨‍💻 Autores

- Ágatha Ribeiro
  
- Bruno Oliveira Theodoro
  
- Dandhara Fernandes De Campos Lima
  
- Ellen Mayumi Borges
  
- Felipe Neres Vieira
  
- Guilherme dos Santos Matos
  
- Iris Pfister Pascoal
  
- Pedro Fidelis Mandoti

- Victor Leandro da Silva

Desenvolvido como parte da **Atividade 2 — Sistema Acadêmico** da disciplina de Programação Orientada a Objetos.


---

## 📄 Licença

Este projeto é de uso acadêmico.
