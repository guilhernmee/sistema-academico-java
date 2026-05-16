// importações necessárias para usar LocalDate (data sem hora) e listas
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Aluno {
    /**
     // atributos q vamos usar Aluno, rgm, nome, cpf, dataNascimento (localdate), email, endereço, municipio, uf e celular
     */
    // pov: dispenso comentarios nessa parte pois é o basico de POO
    private int rgm;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento; // LocalDate representa uma data (dia/mês/ano)
    private String email;
    private String endereco;
    private String municipio;
    private String uf;
    private String celular;

    /**
     abaixo criamos os atributos de relcionamento epresentam a ligação entre classes
     como vimos na matéria do clovinhos
    */

    private Curso curso; //um aluno está matriculado em UM curso (relacionamento 1:1)
    // O tipo é a própria classe Curso, que criamos no outro arquivo

    private List<NotaFalta> notasFaltas; // um aluno pode ter VÁRIAS notas e faltas (relacionamento 1:N)
    // List<NotaFalta> é uma lista genérica que aceita apenas objetos do tipo NotaFalta
    // arrayList é a implementação de List que UTILIZAMOS no primeiro projeto de caixa eletronico

    /**
     * Iniciamos com o construtor PADRÃO (sem parâmetros)
     * Usado quando queremos criar um aluno vazio e preencher os dados depois,
     */
    public Aluno() {
        // se alguém chamar getNotasFaltas() antes de adicionar qualquer nota precisamos incluir aqui tb
        this.notasFaltas = new ArrayList<>();
    }

    /**
     * SEGUNDO o construtor COMPLETO (com todos os parâmetros)
     * Usado quando já temos todos os dados disponíveis para criar o aluno
     */
    public Aluno(int rgm, String nome, String cpf, LocalDate dataNascimento,
                 String email, String endereco, String municipio,
                 String uf, String celular) {

        this.rgm = rgm;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.endereco = endereco;
        this.municipio = municipio;
        this.uf  = uf;
        this.celular = celular;

        // inicializa a lista mesmo no construtor completo
        this.notasFaltas = new ArrayList<>();
    }

    // após isso teremos q fazer os métodos que representam COMPORTAMENTOS/AÇÕES do aluno no sistema
    // vão ser responsaveis por encapsulam a lógica de manipulação dos dados dentro da própria classe

    /**
     * adiciona um registro de nota/falta à lista do aluno
     * a verificação "nf != null" evita adicionar um objeto inexistente (nulo),
     * o que causaria NullPointerException mais tarde ao tentarmos usar rs
     */
    public void adicionarNotaFalta(NotaFalta nf) {
        if (nf != null) { // adiciona se o objeto existir
            notasFaltas.add(nf); // add() é o método da List (que importamos) para inserir um item
        }
    }
    /**
     * remove um registro de nota/falta da lista do aluno
     * o método remove() da List compara os objetos e não gera erro se o item não for encontrado
     * @param nf Objeto NotaFalta a ser removido
     */
    public void removerNotaFalta(NotaFalta nf) {
        notasFaltas.remove(nf);
    }
    //após isso colocaremos os getters e setters (básico né galera, n vou comentar essa parte tb)
    public int getRgm() { return rgm; }

    public void setRgm(int rgm) { this.rgm = rgm; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }

    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getMunicipio() { return municipio; }

    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public String getUf() { return uf; }

    public void setUf(String uf) { this.uf = uf; }

    public String getCelular() { return celular; }

    public void setCelular(String celular) { this.celular = celular; }

    public Curso getCurso() { return curso; }

    public void setCurso(Curso curso) { this.curso = curso; }

    public List<NotaFalta> getNotasFaltas() { return notasFaltas; } // retorna a lista de notas e faltas

    public void setNotasFaltas(List<NotaFalta> notasFaltas) { this.notasFaltas = notasFaltas; }
    //substitui toda a lista de notas e faltas (vai ser útil ao carregar dados do banco)

    // o método toString() é herdado de Object ( p qm n sabe é a classe mãe de todas as classes Java rs)
    // sobrescrevemos com o @Override, p definimos como o objeto será exibido como texto
    // vai ser útil para debug, logs e exibição nos componentes Swing q iremos usar como o JList ou JComboBox

    @Override
    public String toString() {
        return "Aluno{rgm=" + rgm + ", nome='" + nome + "'}";
    }
}