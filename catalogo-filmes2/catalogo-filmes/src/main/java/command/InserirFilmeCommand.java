package command;

import model.Filme;
import service.FilmeService;
import service.RegraNegocioException;

import java.sql.SQLException;

public class InserirFilmeCommand implements Comando {

    private final FilmeService service;
    private final Filme filme;
    private int idCriado;

    public InserirFilmeCommand(FilmeService service, Filme filme) {
        this.service = service;
        this.filme = filme;
    }

    @Override
    public void executar() throws SQLException, RegraNegocioException {
        idCriado = service.inserir(filme);
    }

    @Override
    public void desfazer() throws SQLException {
        service.excluir(idCriado);
    }

    @Override
    public String descricao() {
        return "inserir \"" + filme.getTitulo() + "\"";
    }
}
