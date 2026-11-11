package dao;

import model.Filme;
import model.FilmeBuilder;
import model.Genero;
import util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FilmeDAO {

    public int inserir(Filme filme) throws SQLException {
        String sql = "INSERT INTO filme (titulo, diretor, ano, nota, genero_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, filme.getTitulo());
            ps.setString(2, filme.getDiretor());
            ps.setInt(3, filme.getAno());
            setNota(ps, 4, filme.getNota());
            ps.setInt(5, filme.getGenero().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    public void restaurar(Filme filme) throws SQLException {
        String sql = "INSERT INTO filme (id, titulo, diretor, ano, nota, genero_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, filme.getId());
            ps.setString(2, filme.getTitulo());
            ps.setString(3, filme.getDiretor());
            ps.setInt(4, filme.getAno());
            setNota(ps, 5, filme.getNota());
            ps.setInt(6, filme.getGenero().getId());
            ps.executeUpdate();
        }
    }

    public List<Filme> listar() throws SQLException {
        List<Filme> lista = new ArrayList<>();
        String sql = "SELECT f.id, f.titulo, f.diretor, f.ano, f.nota, "
                + "g.id AS genero_id, g.nome AS genero_nome "
                + "FROM filme f JOIN genero g ON f.genero_id = g.id "
                + "ORDER BY f.titulo";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                double n = rs.getDouble("nota");
                Double nota = rs.wasNull() ? null : n;
                Genero genero = new Genero(rs.getInt("genero_id"), rs.getString("genero_nome"));
                lista.add(new FilmeBuilder()
                        .id(rs.getInt("id"))
                        .titulo(rs.getString("titulo"))
                        .diretor(rs.getString("diretor"))
                        .ano(rs.getInt("ano"))
                        .nota(nota)
                        .genero(genero)
                        .build());
            }
        }
        return lista;
    }

    public void atualizar(Filme filme) throws SQLException {
        String sql = "UPDATE filme SET titulo = ?, diretor = ?, ano = ?, nota = ?, genero_id = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, filme.getTitulo());
            ps.setString(2, filme.getDiretor());
            ps.setInt(3, filme.getAno());
            setNota(ps, 4, filme.getNota());
            ps.setInt(5, filme.getGenero().getId());
            ps.setInt(6, filme.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM filme WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existeTituloAno(String titulo, int ano, int ignorarId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM filme WHERE LOWER(titulo) = LOWER(?) AND ano = ? AND id <> ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setInt(2, ano);
            ps.setInt(3, ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public int contarPorGenero(int generoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM filme WHERE genero_id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, generoId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private void setNota(PreparedStatement ps, int posicao, Double nota) throws SQLException {
        if (nota == null) {
            ps.setNull(posicao, Types.NUMERIC);
        } else {
            ps.setDouble(posicao, nota);
        }
    }
}
