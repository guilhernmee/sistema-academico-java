package br.com.academico.model;

public class Curso {

    // um ENUM é um tipo especial que define um conjunto >FIXO< de constantes
    public enum Periodo {
        MATUTINO,
        VESPERTINO,
        NOTURNO;

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    // ATRIBUTOS da nossa classe
    private int codCurso;
    private String nomeCurso;
    private String campus;
    private Periodo periodo;

    public Curso() {}

    public Curso(int codCurso, String nomeCurso, String campus, Periodo periodo) {
        this.codCurso = codCurso;
        this.nomeCurso = nomeCurso;
        this.campus = campus;
        this.periodo = periodo;
    }

    public int getCodCurso() { return codCurso; }

    public void setCodCurso(int codCurso) { this.codCurso = codCurso; }

    public String getNomeCurso() { return nomeCurso; }

    // CORRIGIDO: era setNome(), agora é setNomeCurso() para ser consistente com getNomeCurso()
    public void setNomeCurso(String nomeCurso) { this.nomeCurso = nomeCurso; }

    public String getCampus() { return campus; }

    public void setCampus(String campus) { this.campus = campus; }

    public Periodo getPeriodo() { return periodo; }

    public void setPeriodo(Periodo periodo) { this.periodo = periodo; }

    @Override
    public String toString() {
        return nomeCurso;
    }
}