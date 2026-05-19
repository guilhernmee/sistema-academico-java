package br.com.academico.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Aluno {

    // ALTERADO: rgm agora é int (a pedido do professor).
    // Como int não preserva zero à esquerda, a regra de negócio passa a ser:
    // RGM = inteiro de 8 dígitos entre 10000000 e 99999999.
    private int rgm;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String endereco;
    private String municipio;
    private String uf;
    private String celular;

    private Curso curso;
    private List<NotaFalta> notasFaltas;

    public Aluno() {
        this.notasFaltas = new ArrayList<>();
    }

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

        this.notasFaltas = new ArrayList<>();
    }

    public void adicionarNotaFalta(NotaFalta nf) {
        if (nf != null) {
            notasFaltas.add(nf);
        }
    }

    public void removerNotaFalta(NotaFalta nf) {
        notasFaltas.remove(nf);
    }

    // getters e setters (rgm agora int)
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

    public List<NotaFalta> getNotasFaltas() { return notasFaltas; }
    public void setNotasFaltas(List<NotaFalta> notasFaltas) { this.notasFaltas = notasFaltas; }

    @Override
    public String toString() {
        return "model.Aluno{rgm=" + rgm + ", nome='" + nome + "'}";
    }
}