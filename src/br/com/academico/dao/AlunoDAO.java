package br.com.academico.dao;

import br.com.academico.model.Aluno;
import br.com.academico.model.Curso;
import br.com.academico.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;

public class AlunoDAO {

    public void salvar(Aluno aluno) throws Exception {
        String sql = "INSERT INTO tb_aluno (rgm, fk_cod_curso, nome, cpf, data_nascimento, email, endereco, municipio, uf, celular) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, aluno.getRgm());                            // ALTERADO: setInt
            stmt.setInt(2, aluno.getCurso().getCodCurso());
            stmt.setString(3, aluno.getNome());
            stmt.setString(4, aluno.getCpf());
            stmt.setDate(5, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(6, aluno.getEmail());
            stmt.setString(7, aluno.getEndereco());
            stmt.setString(8, aluno.getMunicipio());
            stmt.setString(9, aluno.getUf());
            stmt.setString(10, aluno.getCelular());

            stmt.executeUpdate();
            System.out.println("Aluno " + aluno.getNome() + " cadastrado com sucesso!");
        }
    }

    // ALTERADO: parâmetro rgm agora é int
    public Aluno consultar(int rgm) throws Exception {
        String sql = """
            SELECT a.*, c.cod_curso, c.nome_curso, c.campus, c.periodo
            FROM tb_aluno a
            INNER JOIN tb_curso c ON c.cod_curso = a.fk_cod_curso
            WHERE a.rgm = ?
            """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rgm);                                       // ALTERADO: setInt

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Curso curso = new Curso();
                    curso.setCodCurso(rs.getInt("cod_curso"));
                    curso.setNomeCurso(rs.getString("nome_curso"));
                    curso.setCampus(rs.getString("campus"));
                    curso.setPeriodo(Curso.Periodo.valueOf(rs.getString("periodo")));

                    Aluno alunoEncontrado = new Aluno();
                    alunoEncontrado.setRgm(rs.getInt("rgm"));          // ALTERADO: getInt
                    alunoEncontrado.setNome(rs.getString("nome"));
                    alunoEncontrado.setCpf(rs.getString("cpf"));
                    alunoEncontrado.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    alunoEncontrado.setEmail(rs.getString("email"));
                    alunoEncontrado.setEndereco(rs.getString("endereco"));
                    alunoEncontrado.setMunicipio(rs.getString("municipio"));
                    alunoEncontrado.setUf(rs.getString("uf"));
                    alunoEncontrado.setCelular(rs.getString("celular"));
                    alunoEncontrado.setCurso(curso);

                    return alunoEncontrado;
                }
            }
        }
        return null;
    }

    public void alterar(Aluno aluno) throws Exception {
        String sql = "UPDATE tb_aluno SET fk_cod_curso = ?, nome = ?, cpf = ?, data_nascimento = ?, email = ?, endereco = ?, municipio = ?, uf = ?, celular = ? WHERE rgm = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, aluno.getCurso().getCodCurso());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getCpf());
            stmt.setDate(4, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(5, aluno.getEmail());
            stmt.setString(6, aluno.getEndereco());
            stmt.setString(7, aluno.getMunicipio());
            stmt.setString(8, aluno.getUf());
            stmt.setString(9, aluno.getCelular());
            stmt.setInt(10, aluno.getRgm());                           // ALTERADO: setInt

            stmt.executeUpdate();
            System.out.println("Aluno " + aluno.getNome() + " alterado com sucesso!");
        }
    }

    // ALTERADO: parâmetro rgm agora é int
    public void excluir(int rgm) throws Exception {
        String sql = "DELETE FROM tb_aluno WHERE rgm = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rgm);                                       // ALTERADO: setInt
            stmt.executeUpdate();
            System.out.println("Aluno de RGM " + rgm + " excluído com sucesso!");
        }
    }
}