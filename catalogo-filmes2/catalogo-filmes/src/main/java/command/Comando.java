package command;

import service.RegraNegocioException;

import java.sql.SQLException;

// Padrão Command: interface comum a todas as ações que podem ser desfeitas
public interface Comando {

    void executar() throws SQLException, RegraNegocioException;

    void desfazer() throws SQLException, RegraNegocioException;

    String descricao();
}
