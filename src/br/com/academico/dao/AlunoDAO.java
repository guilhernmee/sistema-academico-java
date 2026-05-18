package br.com.academico.dao;

import br.com.academico.model.Aluno;
import br.com.academico.model.Curso;
import br.com.academico.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

public class AlunoDAO {

    // metodo para salvar
    public void salvar(Aluno aluno) {
        String sql = "INSERT INTO tb_aluno (rgm, fk_cod_curso, nome, cpf, data_nascimento, email, endereco, municipio, uf, celular) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);

            stmt.setString(1, aluno.getRgm());
            stmt.setInt(2, aluno.getCurso().getCodCurso());
            stmt.setString(3, aluno.getNome());
            stmt.setString(4, aluno.getCpf());
            stmt.setDate(5, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(6, aluno.getEmail());
            stmt.setString(7, aluno.getEndereco());
            stmt.setString(8, aluno.getMunicipio());
            stmt.setString(9, aluno.getUf());
            stmt.setString(10, aluno.getCelular());

            stmt.execute();
            System.out.println("Aluno " + aluno.getNome() + " cadastrado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conexao, stmt, null);
        }
    }

    // metodo para fazer consultar (read)
    public Aluno consultar(String rgm) {
        // JOIN para trazer os dados do curso junto com o aluno
        String sql = """
            SELECT a.*, c.cod_curso, c.nome_curso, c.campus, c.periodo
            FROM tb_aluno a
            INNER JOIN tb_curso c ON c.cod_curso = a.fk_cod_curso
            WHERE a.rgm = ?
            """;

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Aluno alunoEncontrado = null;

        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, rgm);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Monta o curso com os dados do JOIN
                Curso curso = new Curso();
                curso.setCodCurso(rs.getInt("cod_curso"));
                curso.setNome(rs.getString("nome_curso"));
                curso.setCampus(rs.getString("campus"));
                curso.setPeriodo(Curso.Periodo.valueOf(rs.getString("periodo")));

                // Monta o aluno e coloca o curso dentro dele
                alunoEncontrado = new Aluno();
                alunoEncontrado.setRgm(rs.getString("rgm"));
                alunoEncontrado.setNome(rs.getString("nome"));
                alunoEncontrado.setCpf(rs.getString("cpf"));
                alunoEncontrado.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                alunoEncontrado.setEmail(rs.getString("email"));
                alunoEncontrado.setEndereco(rs.getString("endereco"));
                alunoEncontrado.setMunicipio(rs.getString("municipio"));
                alunoEncontrado.setUf(rs.getString("uf"));
                alunoEncontrado.setCelular(rs.getString("celular"));
                alunoEncontrado.setCurso(curso); // ← estava faltando isso!
            }

        } catch (Exception e) {
            System.out.println("Erro ao consultar aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conexao, stmt, rs);
        }

        return alunoEncontrado;
    }

    // metodo para fazer alteracao (update)
    public void alterar(Aluno aluno) {
        // No UPDATE, o RGM vai no final (no WHERE) para sabermos quem alterar
        String sql = "UPDATE tb_aluno SET fk_cod_curso = ?, nome = ?, cpf = ?, data_nascimento = ?, email = ?, endereco = ?, municipio = ?, uf = ?, celular = ? WHERE rgm = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, aluno.getCurso().getCodCurso());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getCpf());
            stmt.setDate(4, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(5, aluno.getEmail());
            stmt.setString(6, aluno.getEndereco());
            stmt.setString(7, aluno.getMunicipio());
            stmt.setString(8, aluno.getUf());
            stmt.setString(9, aluno.getCelular());

            // O RGM é o 10º parâmetro
            stmt.setString(10, aluno.getRgm());

            stmt.execute();
            System.out.println("Aluno " + aluno.getNome() + " alterado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao alterar aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conexao, stmt, null);
        }
    }

    // metodo para excluir
    public void excluir(String rgm) {
        String sql = "DELETE FROM tb_aluno WHERE rgm = ?";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {
            conexao = ConnectionFactory.getConnection();
            stmt = conexao.prepareStatement(sql);
            stmt.setString(1, rgm);

            stmt.execute();
            System.out.println("Aluno de RGM " + rgm + " excluído com sucesso!");

            // Graças ao seu "ON DELETE CASCADE" no banco, as notas sumirão sozinhas!

        } catch (Exception e) {
            System.out.println("Erro ao excluir aluno: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conexao, stmt, null);
        }
    }

    // metodo para fechar as conexões sem repetir código

    private void fecharConexao(Connection conexao, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conexao != null) ConnectionFactory.closeConnection(conexao);
        } catch (Exception e) {
            System.out.println("Erro ao fechar os recursos: " + e.getMessage());
        }
    }
}
