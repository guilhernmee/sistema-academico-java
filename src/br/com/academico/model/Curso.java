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
        private int codCurso; //nossa chave primaria galera
        private String nomeCurso;
        private String campus;
        private Periodo periodo; // usa o enum Periodo definido acima para garantir q p valor sempre é válido

        public Curso() {} //vazio padrão ne galeres

        public Curso(int codCurso, String nomeCurso, String campus, Periodo periodo) {
            this.codCurso = codCurso;
            this.nomeCurso = nomeCurso;
            this.campus = campus;
            this.periodo = periodo;
        }

        public int getCodCurso() { return codCurso; }

        public void setCodCurso(int codCurso) { this.codCurso = codCurso; }

        public String getNomeCurso() { return nomeCurso; }

        public void setNome(String nomeCurso) { this.nomeCurso = nomeCurso;}

        public String getCampus() { return campus; }

        public void setCampus(String campus) { this.campus = campus; }

        public Periodo getPeriodo() { return periodo; }

        public void setPeriodo(Periodo periodo) { this.periodo = periodo; }

        // sobrescrevemos com o @Override, p definimos como o objeto será exibido como texto, assim como foi explicado no model.Aluno
        // vai ser útil para debug, logs e exibição nos componentes Swing q iremos usar como o JList ou JComboBox
        @Override
        public String toString() {
            return nomeCurso;
        }
    }