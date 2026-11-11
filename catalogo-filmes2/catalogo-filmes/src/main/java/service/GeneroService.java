package service;

import dao.FilmeDAO;
import dao.GeneroDAO;
import model.Genero;
import observer.GeneroObserver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Service Layer: regras de negócio de Gênero
// Padrão Observer: este service é o Subject, avisa os observers quando os gêneros mudam
public class GeneroService {

    private final GeneroDAO generoDAO;
    private final FilmeDAO filmeDAO;
    private final List<GeneroObserver> observers = new ArrayList<>();

    public GeneroService(GeneroDAO generoDAO, FilmeDAO filmeDAO) {
        this.generoDAO = generoDAO;
        this.filmeDAO = filmeDAO;
    }

    public void adicionarObserver(GeneroObserver observer) {
        observers.add(observer);
    }

    public void removerObserver(GeneroObserver observer) {
        observers.remove(observer);
    }

    private void notificarObservers() {
        for (GeneroObserver observer : new ArrayList<>(observers)) {
            observer.generosAlterados();
        }
    }

    public List<Genero> listar() throws SQLException {
        return generoDAO.listar();
    }

    public void inserir(Genero genero) throws SQLException, RegraNegocioException {
        validar(genero);
        generoDAO.inserir(genero);
        notificarObservers();
    }

    public void atualizar(Genero genero) throws SQLException, RegraNegocioException {
        validar(genero);
        generoDAO.atualizar(genero);
        notificarObservers();
    }

    public void excluir(Genero genero) throws SQLException, RegraNegocioException {
        int quantidade = filmeDAO.contarPorGenero(genero.getId());
        if (quantidade > 0) {
            throw new RegraNegocioException(
                    "Não é possível excluir: existem " + quantidade + " filme(s) com este gênero.");
        }
        generoDAO.excluir(genero.getId());
        notificarObservers();
    }

    private void validar(Genero genero) throws SQLException, RegraNegocioException {
        String nome = genero.getNome() == null ? "" : genero.getNome().trim();
        if (nome.isEmpty()) {
            throw new RegraNegocioException("Informe o nome do gênero.");
        }
        if (nome.length() > 50) {
            throw new RegraNegocioException("O nome do gênero não pode passar de 50 caracteres.");
        }
        for (Genero existente : generoDAO.listar()) {
            if (existente.getId() != genero.getId() && existente.getNome().equalsIgnoreCase(nome)) {
                throw new RegraNegocioException("Já existe um gênero com esse nome.");
            }
        }
    }
}
