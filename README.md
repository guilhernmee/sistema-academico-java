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

### Aba — Curso
Campos disponíveis:
- Curso *(ComboBox)*
- Campus *(ComboBox)*
- Período: Matutino / Vespertino / Noturno *(RadioButton)*

### Aba — Notas e Faltas
Campos disponíveis:
- RGM, Nome do aluno *(somente leitura)*
- Disciplina *(ComboBox)*
- Semestre, Nota, Faltas

### Aba — Boletim
Exibe um resumo com:
- RGM, Nome, Curso
- Disciplinas, Notas e Faltas do aluno

---

## 🗂️ Menus do Sistema

| Menu | Opções |
|------|--------|
| **Aluno** | Salvar, Alterar, Consultar, Excluir, Sair |
| **Notas e Faltas** | Lançar |
| **Ajuda** | Sobre |

---

## 🗄️ Banco de Dados

O banco de dados foi modelado com as seguintes diretrizes:

- Uso de **PRIMARY KEY (PK)** em todas as tabelas
- Uso de **FOREIGN KEY (FK)** para garantir integridade referencial
- **Exclusão em cascata**: ao excluir um aluno, suas notas e faltas também são removidas automaticamente
- **Restrição de unicidade**: não é permitido cadastrar dois alunos com o mesmo RGM

### Tabelas principais

```sql
-- Tabela de Alunos
CREATE TABLE aluno (
    rgm         INT PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    dt_nascimento DATE,
    cpf         VARCHAR(14) UNIQUE,
    email       VARCHAR(100),
    endereco    VARCHAR(150),
    municipio   VARCHAR(80),
    uf          CHAR(2),
    celular     VARCHAR(15)
);

-- Tabela de Cursos
CREATE TABLE curso (
    id_curso    INT PRIMARY KEY AUTO_INCREMENT,
    nome_curso  VARCHAR(100) NOT NULL,
    campus      VARCHAR(80),
    periodo     ENUM('Matutino', 'Vespertino', 'Noturno')
);

-- Tabela de Notas e Faltas
CREATE TABLE notas_faltas (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    rgm         INT NOT NULL,
    disciplina  VARCHAR(100),
    semestre    VARCHAR(10),
    nota        DECIMAL(4,2),
    faltas      INT,
    FOREIGN KEY (rgm) REFERENCES aluno(rgm) ON DELETE CASCADE
);
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
│   │   ├── Aluno.java
│   │   ├── Curso.java
│   │   └── NotasFaltas.java
│   ├── dao/
│   │   ├── AlunoDAO.java
│   │   ├── CursoDAO.java
│   │   └── NotasFaltasDAO.java
│   ├── view/
│   │   ├── TelaPrincipal.java
│   │   ├── TelaAluno.java
│   │   └── TelaBoletim.java
│   └── connection/
│       └── ConnectionFactory.java
├── database/
│   └── script.sql
└── README.md
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java JDK 8 ou superior
- MySQL Server instalado e em execução
- IDE de sua preferência (Eclipse, IntelliJ, NetBeans)
- Driver JDBC do MySQL (`mysql-connector-java`)

### Passos

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/sistema-academico.git
   ```

2. **Configure o banco de dados**
   - Crie o banco no MySQL:
     ```sql
     CREATE DATABASE sistema_academico;
     ```
   - Execute o script localizado em `database/script.sql`

3. **Configure a conexão**
   - Edite o arquivo `ConnectionFactory.java` com suas credenciais:
     ```java
     private static final String URL  = "jdbc:mysql://localhost:3306/sistema_academico";
     private static final String USER = "seu_usuario";
     private static final String PASS = "sua_senha";
     ```

4. **Execute o projeto**
   - Compile e rode a classe `TelaPrincipal.java`

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

---

## 👨‍💻 Autor

Desenvolvido como parte da **Atividade 2 — Sistema Acadêmico (1,5 pontos)** da disciplina de Programação Orientada a Objetos.

---

## 📄 Licença

Este projeto é de uso acadêmico.
