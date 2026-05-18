package model;

public class Disciplina {
    // model.Disciplina representa uma matéria que o aluno cursa
// Relacionamento: pertence a UM curso (1:1)
// Quem guarda a lista de model.NotaFalta é o model.Aluno — mesmo padrão da classe model.Aluno

    private int codDisciplina;     // chave primária, mesmo padrão do codCurso em model.Curso
    private String nomeDisciplina;
    private Curso curso;           // disciplina pertence a um curso (FK no banco)

    // construtor padrão — mesmo padrão do model.Curso() e model.Aluno()
    public Disciplina() {}

    // construtor completo — mesmo padrão do model.Aluno(rgm, nome, ...) e model.Curso(codCurso, ...)
    public Disciplina(int codDisciplina, String nomeDisciplina, Curso curso) {
        this.codDisciplina = codDisciplina;
        this.nomeDisciplina = nomeDisciplina;
        this.curso = curso;
    }
    // getters e setters — mesmo padrão do model.Aluno e model.Curso
    public int getCodDisciplina() { return codDisciplina; }
    public void setCodDisciplina(int codDisciplina) { this.codDisciplina = codDisciplina; }

    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nomeDisciplina) { this.nomeDisciplina = nomeDisciplina; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    // toString() sobrescrito — mesmo padrão do model.Curso que retorna só o nome
    // útil para exibir no JComboBox da tela de Notas e Faltas
    @Override
    public String toString() {
        return nomeDisciplina;
    }
}