package command;

import model.Filme;
import service.FilmeService;
import service.RegraNegocioException;

import java.sql.SQLException;

public class ExcluirFilmeCommand implements Comando {

    private final FilmeService service;
    private final Filme filme;

    public ExcluirFilmeCommand(FilmeService service, Filme filme) {
        this.service = service;
        this.filme = filme;
    }

    @Override
    public void executar() throws SQLException {
        service.excluir(filme.getId());
    }

    @Override
    public void desfazer() throws SQLException, RegraNegocioException {
        service.restaurar(filme);
    }

    @Override
    public String descricao() {
        return "excluir \"" + filme.getTitulo() + "\"";
    }
}
