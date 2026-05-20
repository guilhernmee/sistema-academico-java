package br.com.academico.model;

public class NotaFalta {
    // model.NotaFalta representa o registro de nota e falta de UM aluno em UMA disciplina
// Relacionamento:
//   pertence a UM aluno     (FK: rgm)           → ON DELETE CASCADE
//   pertence a UMA disciplina (FK: codDisciplina)
// Requisito do professor: "se um aluno for EXCLUIDO suas notas e faltas também deverão ser excluídas"
    // enum Semestre — mesmo padrão do enum Periodo dentro do model.Curso
    // define um conjunto FIXO de semestres válidos para evitar valores inválidos
    // cobre desde 2020 conforme exemplo do PDF (ex: 2020-1)

    public enum Semestre {
        S1_2020("2020-1"),
        S2_2020("2020-2"),
        S1_2021("2021-1"),
        S2_2021("2021-2"),
        S1_2022("2022-1"),
        S2_2022("2022-2"),
        S1_2023("2023-1"),
        S2_2023("2023-2"),
        S1_2024("2024-1"),
        S2_2024("2024-2"),
        S1_2025("2025-1"),
        S2_2025("2025-2"),
        S1_2026("2026-1"),
        S2_2026("2026-2");

        private final String descricao;

        Semestre(String descricao) {
            this.descricao = descricao;
        }

        // mesmo padrão do toString() do enum Periodo em model.Curso
        // útil para exibir no JComboBox da tela
        @Override
        public String toString() {
            return descricao;
        }
    }

    private int codNotaFalta;        // chave primária — mesmo padrão do codCurso e codDisciplina
    private Aluno aluno;             // FK → aluno (relacionamento 1:1)
    private Disciplina disciplina;   // FK → disciplina (relacionamento 1:1)
    private Semestre semestre;       // usa o enum Semestre para garantir que o valor sempre é válido
    private double nota;             // nota do aluno na disciplina — PDF mostra valores como 0,5
    private int faltas;              // número de faltas do aluno na disciplina

    // construtor padrão — mesmo padrão do model.Aluno() e model.Curso()
    public NotaFalta() {}

    // construtor completo — mesmo padrão do model.Aluno(rgm, nome, ...) e model.Curso(codCurso, ...)
    public NotaFalta(int codNotaFalta, Aluno aluno, Disciplina disciplina,
                     Semestre semestre, double nota, int faltas) {
        this.codNotaFalta = codNotaFalta;
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.semestre = semestre;
        this.nota = nota;
        this.faltas = faltas;
    }

    // getters e setters — mesmo padrão do model.Aluno e model.Curso
    public int getCodNotaFalta() { return codNotaFalta; }
    public void setCodNotaFalta(int codNotaFalta) { this.codNotaFalta = codNotaFalta; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Disciplina getDisciplina() { return disciplina; }
    public void setDisciplina(Disciplina disciplina) { this.disciplina = disciplina; }

    public Semestre getSemestre() { return semestre; }
    public void setSemestre(Semestre semestre) { this.semestre = semestre; }

    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }

    public int getFaltas() { return faltas; }
    public void setFaltas(int faltas) { this.faltas = faltas; }

    // toString() sobrescrito — mesmo padrão do model.Aluno que mostra os dados principais
    // útil para debug e exibição no Boletim
    @Override
    public String toString() {
        return "model.NotaFalta{" +
                "aluno=" + aluno.getNome() +
                ", disciplina=" + disciplina.getNomeDisciplina() +
                ", semestre=" + semestre +
                ", nota=" + nota +
                ", faltas=" + faltas + "}";
    }
}