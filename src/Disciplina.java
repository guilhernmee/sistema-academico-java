public class Disciplina {
    // Disciplina representa uma matéria que o aluno cursa
// Relacionamento: pertence a UM curso (1:1)
// Quem guarda a lista de NotaFalta é o Aluno — mesmo padrão da classe Aluno

    private int codDisciplina;     // chave primária, mesmo padrão do codCurso em Curso
    private String nomeDisciplina;
    private Curso curso;           // disciplina pertence a um curso (FK no banco)

    // construtor padrão — mesmo padrão do Curso() e Aluno()
    public Disciplina() {}

    // construtor completo — mesmo padrão do Aluno(rgm, nome, ...) e Curso(codCurso, ...)
    public Disciplina(int codDisciplina, String nomeDisciplina, Curso curso) {
        this.codDisciplina = codDisciplina;
        this.nomeDisciplina = nomeDisciplina;
        this.curso = curso;
    }
    // getters e setters — mesmo padrão do Aluno e Curso
    public int getCodDisciplina() { return codDisciplina; }
    public void setCodDisciplina(int codDisciplina) { this.codDisciplina = codDisciplina; }

    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nomeDisciplina) { this.nomeDisciplina = nomeDisciplina; }

    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    // toString() sobrescrito — mesmo padrão do Curso que retorna só o nome
    // útil para exibir no JComboBox da tela de Notas e Faltas
    @Override
    public String toString() {
        return nomeDisciplina;
    }
}