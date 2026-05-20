package br.com.academico.dao;

import br.com.academico.model.Aluno;
import br.com.academico.model.Curso;
import br.com.academico.model.Disciplina;
import br.com.academico.model.NotaFalta;
import br.com.academico.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotaFaltaDAO {

    public void salvar(NotaFalta notaFalta) throws Exception {
        String sql = "INSERT INTO tb_nota_falta (fk_rgm, fk_cod_disciplina, semestre, nota, faltas) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, notaFalta.getAluno().getRgm());             // ALTERADO: setInt
            stmt.setInt(2, notaFalta.getDisciplina().getCodDisciplina());
            stmt.setString(3, notaFalta.getSemestre().toString());
            stmt.setDouble(4, notaFalta.getNota());
            stmt.setInt(5, notaFalta.getFaltas());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    notaFalta.setCodNotaFalta(rs.getInt(1));
                }
            }
        }
    }

    public NotaFalta buscarPorId(int codNotaFalta) throws Exception {
        String sql = "SELECT nf.pk_cod_nota_falta, nf.semestre, nf.nota, nf.faltas, " +
                "a.rgm AS rgm, a.nome AS nome_aluno, a.cpf, a.data_nascimento, a.email, a.endereco, a.municipio, a.uf, a.celular, " +
                "d.cod_disciplina, d.nome_disciplina, " +
                "c.cod_curso, c.nome_curso, c.campus, c.periodo " +
                "FROM tb_nota_falta nf " +
                "JOIN tb_aluno a ON nf.fk_rgm = a.rgm " +
                "JOIN tb_disciplina d ON nf.fk_cod_disciplina = d.cod_disciplina " +
                "JOIN tb_curso c ON a.fk_cod_curso = c.cod_curso " +
                "WHERE nf.pk_cod_nota_falta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codNotaFalta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarObjeto(rs);
                }
            }
        }
        return null;
    }

    // ALTERADO: parâmetro rgm agora é int
    public List<NotaFalta> listarPorAluno(int rgm) throws Exception {
        String sql = "SELECT nf.pk_cod_nota_falta, nf.semestre, nf.nota, nf.faltas, " +
                "a.rgm AS rgm, a.nome AS nome_aluno, a.cpf, a.data_nascimento, a.email, a.endereco, a.municipio, a.uf, a.celular, " +
                "d.cod_disciplina, d.nome_disciplina, " +
                "c.cod_curso, c.nome_curso, c.campus, c.periodo " +
                "FROM tb_nota_falta nf " +
                "JOIN tb_aluno a ON nf.fk_rgm = a.rgm " +
                "JOIN tb_disciplina d ON nf.fk_cod_disciplina = d.cod_disciplina " +
                "JOIN tb_curso c ON a.fk_cod_curso = c.cod_curso " +
                "WHERE nf.fk_rgm = ? ORDER BY nf.semestre ASC";

        List<NotaFalta> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rgm);                                       // ALTERADO: setInt
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarObjeto(rs));
                }
            }
        }
        return lista;
    }

    public void atualizar(NotaFalta notaFalta) throws Exception {
        String sql = "UPDATE tb_nota_falta SET fk_rgm = ?, fk_cod_disciplina = ?, semestre = ?, nota = ?, faltas = ? " +
                "WHERE pk_cod_nota_falta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notaFalta.getAluno().getRgm());             // ALTERADO: setInt
            stmt.setInt(2, notaFalta.getDisciplina().getCodDisciplina());
            stmt.setString(3, notaFalta.getSemestre().toString());
            stmt.setDouble(4, notaFalta.getNota());
            stmt.setInt(5, notaFalta.getFaltas());
            stmt.setInt(6, notaFalta.getCodNotaFalta());

            stmt.executeUpdate();
        }
    }

    public void excluir(int codNotaFalta) throws Exception {
        String sql = "DELETE FROM tb_nota_falta WHERE pk_cod_nota_falta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codNotaFalta);
            stmt.executeUpdate();
        }
    }

    private NotaFalta.Semestre converterStringParaEnum(String textoBanco) {
        for (NotaFalta.Semestre sem : NotaFalta.Semestre.values()) {
            if (sem.toString().equals(textoBanco)) {
                return sem;
            }
        }
        throw new IllegalArgumentException("Semestre inválido no banco: " + textoBanco);
    }

    private NotaFalta montarObjeto(ResultSet rs) throws Exception {
        Curso.Periodo periodoEnum = Curso.Periodo.valueOf(rs.getString("periodo"));
        Curso curso = new Curso(
                rs.getInt("cod_curso"),
                rs.getString("nome_curso"),
                rs.getString("campus"),
                periodoEnum
        );

        Aluno aluno = new Aluno();
        aluno.setRgm(rs.getInt("rgm"));                                // ALTERADO: getInt
        aluno.setNome(rs.getString("nome_aluno"));
        aluno.setCurso(curso);
        aluno.setCpf(rs.getString("cpf"));
        aluno.setEmail(rs.getString("email"));

        Disciplina disciplina = new Disciplina(
                rs.getInt("cod_disciplina"),
                rs.getString("nome_disciplina"),
                curso
        );

        NotaFalta.Semestre semestreEnum = converterStringParaEnum(rs.getString("semestre"));

        return new NotaFalta(
                rs.getInt("pk_cod_nota_falta"),
                aluno,
                disciplina,
                semestreEnum,
                rs.getDouble("nota"),
                rs.getInt("faltas")
        );
    }
}