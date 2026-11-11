package service;

import dao.FilmeDAO;
import model.Filme;

import java.sql.SQLException;
import java.util.List;

// Service Layer: regras de negócio de Filme, entre o Controller e o DAO
public class FilmeService {

    private final FilmeDAO dao;

    public FilmeService(FilmeDAO dao) {
        this.dao = dao;
    }

    public List<Filme> listar() throws SQLException {
        return dao.listar();
    }

    public int inserir(Filme filme) throws SQLException, RegraNegocioException {
        validar(filme);
        return dao.inserir(filme);
    }

    public void restaurar(Filme filme) throws SQLException, RegraNegocioException {
        validar(filme);
        dao.restaurar(filme);
    }

    public void atualizar(Filme filme) throws SQLException, RegraNegocioException {
        validar(filme);
        dao.atualizar(filme);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    private void validar(Filme filme) throws SQLException, RegraNegocioException {
        String titulo = filme.getTitulo() == null ? "" : filme.getTitulo().trim();
        if (titulo.isEmpty() || filme.getGenero() == null) {
            throw new RegraNegocioException("Título e gênero são obrigatórios.");
        }
        if (titulo.length() > 150) {
            throw new RegraNegocioException("O título não pode passar de 150 caracteres.");
        }
        if (filme.getDiretor() != null && filme.getDiretor().length() > 100) {
            throw new RegraNegocioException("O nome do diretor não pode passar de 100 caracteres.");
        }
        if (filme.getAno() < 1888) {
            throw new RegraNegocioException("Ano inválido.");
        }
        if (filme.getNota() != null && (filme.getNota() < 0 || filme.getNota() > 10)) {
            throw new RegraNegocioException("A nota deve estar entre 0 e 10.");
        }
        if (dao.existeTituloAno(titulo, filme.getAno(), filme.getId())) {
            throw new RegraNegocioException("Já existe um filme com esse título e ano.");
        }
    }
}
