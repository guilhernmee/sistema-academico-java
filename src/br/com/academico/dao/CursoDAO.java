package br.com.academico.dao;

import br.com.academico.model.Curso;
import br.com.academico.util.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    // MUDANÇA AQUI: Agora o método lança Exception (adaptado à ConnectionFactory)
    public void salvar(Curso curso) throws Exception {
        String sql = "INSERT INTO tb_curso (nome_curso, campus, periodo) VALUES (?, ?, ?)";

        // O bloco try-with-resources continua limpando os recursos automaticamente
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, curso.getNomeCurso());
            stmt.setString(2, curso.getCampus());
            stmt.setString(3, curso.getPeriodo().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    curso.setCodCurso(rs.getInt(1));
                }
            }
        }
    }

    // MUDANÇA AQUI: throws Exception
    public List<Curso> listarTodos() throws Exception {
        String sql = "SELECT * FROM tb_curso";
        List<Curso> lista = new ArrayList<>();

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
                lista.add(curso);
            }
        }
        return lista;
    }

    // MUDANÇA AQUI: throws Exception
    public Curso buscarPorId(int codCurso) throws Exception {
        String sql = "SELECT * FROM tb_curso WHERE cod_curso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codCurso);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Curso.Periodo periodoEnum = Curso.Periodo.valueOf(rs.getString("periodo"));

                    return new Curso(
                            rs.getInt("cod_curso"),
                            rs.getString("nome_curso"),
                            rs.getString("campus"),
                            periodoEnum
                    );
                }
            }
        }
        return null;
    }

    // MUDANÇA AQUI: throws Exception
    public void atualizar(Curso curso) throws Exception {
        String sql = "UPDATE tb_curso SET nome_curso = ?, campus = ?, periodo = ? WHERE cod_curso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNomeCurso());
            stmt.setString(2, curso.getCampus());
            stmt.setString(3, curso.getPeriodo().name());
            stmt.setInt(4, curso.getCodCurso());

            stmt.executeUpdate();
        }
    }

    // MUDANÇA AQUI: throws Exception
    public void excluir(int codCurso) throws Exception {
        String sql = "DELETE FROM tb_curso WHERE cod_curso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codCurso);
            stmt.executeUpdate();
        }
    }
}