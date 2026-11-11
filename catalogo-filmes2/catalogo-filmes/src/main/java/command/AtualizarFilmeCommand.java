package command;

import model.Filme;
import service.FilmeService;
import service.RegraNegocioException;

import java.sql.SQLException;

public class AtualizarFilmeCommand implements Comando {

    private final FilmeService service;
    private final Filme antes;
    private final Filme depois;

    public AtualizarFilmeCommand(FilmeService service, Filme antes, Filme depois) {
        this.service = service;
        this.antes = antes;
        this.depois = depois;
    }

    @Override
    public void executar() throws SQLException, RegraNegocioException {
        service.atualizar(depois);
    }

    @Override
    public void desfazer() throws SQLException, RegraNegocioException {
        service.atualizar(antes);
    }

    @Override
    public String descricao() {
        return "atualizar \"" + antes.getTitulo() + "\"";
    }
}
