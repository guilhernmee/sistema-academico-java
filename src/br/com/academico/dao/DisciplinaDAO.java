package br.com.academico.dao;

import br.com.academico.model.Curso;
import br.com.academico.model.Disciplina;
import br.com.academico.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    // CREATE: Salva a disciplina associando-a ao ID do curso
    public void salvar(Disciplina disciplina) throws SQLException {
        // Passando valores fixos para carga_horaria (80) e creditos (4) exigidos pelo banco
        String sql = "INSERT INTO tb_disciplina (nome_disciplina, fk_cod_curso, carga_horaria, creditos) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, disciplina.getNomeDisciplina());

            // Recupera o ID de dentro do objeto Curso associado
            stmt.setInt(2, disciplina.getCurso().getCodCurso());

            stmt.setInt(3, 80); // Carga horária padrão para o banco
            stmt.setInt(4, 4);  // Créditos padrão para o banco

            stmt.executeUpdate();

            // Recupera o ID gerado pelo AUTO_INCREMENT para a disciplina
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    disciplina.setCodDisciplina(rs.getInt(1));
                }
            }
        }
    }

    // READ: Busca uma disciplina por ID trazendo o objeto Curso 100% populado
    public Disciplina buscarPorId(int codDisciplina) throws SQLException {
        String sql = "SELECT d.cod_disciplina, d.nome_disciplina, c.cod_curso, c.nome_curso, c.campus, c.periodo " +
                "FROM tb_disciplina d " +
                "JOIN tb_curso c ON d.fk_cod_curso = c.cod_curso " +
                "WHERE d.cod_disciplina = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codDisciplina);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Instancia o Enum do período do curso
                    Curso.Periodo periodoEnum = Curso.Periodo.valueOf(rs.getString("periodo"));

                    // Monta o objeto Curso completo com o construtor correto
                    Curso curso = new Curso(
                            rs.getInt("cod_curso"),
                            rs.getString("nome_curso"),
                            rs.getString("campus"),
                            periodoEnum
                    );

                    // Retorna a disciplina preenchida com o objeto curso dentro dela
                    return new Disciplina(
                            rs.getInt("cod_disciplina"),
                            rs.getString("nome_disciplina"),
                            curso
                    );
                }
            }
        }
        return null;
    }

    // READ ALL: Lista todas as disciplinas fazendo o JOIN com curso
    public List<Disciplina> listarTodos() throws SQLException {
        String sql = "SELECT d.cod_disciplina, d.nome_disciplina, c.cod_curso, c.nome_curso, c.campus, c.periodo " +
                "FROM tb_disciplina d " +
                "JOIN tb_curso c ON d.fk_cod_curso = c.cod_curso";

        List<Disciplina> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Curso.Periodo periodoEnum = Curso.Periodo.valueOf(rs.getString("periodo"));

                Curso curso = new Curso(
                        rs.getInt("cod_curso"),
                        rs.getString("nome_curso"),
                        rs.getString("campus"),
                        periodoEnum
                );

                lista.add(new Disciplina(
                        rs.getInt("cod_disciplina"),
                        rs.getString("nome_disciplina"),
                        curso
                ));
            }
        }
        return lista;
    }

    // UPDATE: Atualiza o nome da disciplina ou o curso ao qual ela pertence
    public void atualizar(Disciplina disciplina) throws SQLException {
        String sql = "UPDATE tb_disciplina SET nome_disciplina = ?, fk_cod_curso = ? WHERE cod_disciplina = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, disciplina.getNomeDisciplina());
            stmt.setInt(2, disciplina.getCurso().getCodCurso());
            stmt.setInt(3, disciplina.getCodDisciplina());

            stmt.executeUpdate();
        }
    }

    // DELETE: Exclui a disciplina por ID
    public void excluir(int codDisciplina) throws SQLException {
        String sql = "DELETE FROM tb_disciplina WHERE cod_disciplina = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codDisciplina);
            stmt.executeUpdate();
        }
    }
}