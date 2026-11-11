package dao;

import model.Genero;
import util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneroDAO {

    public void inserir(Genero genero) throws SQLException {
        String sql = "INSERT INTO genero (nome) VALUES (?)";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, genero.getNome());
            ps.executeUpdate();
        }
    }

    public List<Genero> listar() throws SQLException {
        List<Genero> lista = new ArrayList<>();
        String sql = "SELECT id, nome FROM genero ORDER BY nome";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Genero(rs.getInt("id"), rs.getString("nome")));
            }
        }
        return lista;
    }

    public void atualizar(Genero genero) throws SQLException {
        String sql = "UPDATE genero SET nome = ? WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, genero.getNome());
            ps.setInt(2, genero.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM genero WHERE id = ?";
        try (Connection con = ConexaoDB.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
