package command;

import service.RegraNegocioException;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;

// Padrão Command: papel de Invoker, executa os comandos e guarda o histórico para desfazer
public class HistoricoComandos {

    private final Deque<Comando> pilha = new ArrayDeque<>();

    public void executar(Comando comando) throws SQLException, RegraNegocioException {
        comando.executar();
        pilha.push(comando);
    }

    public void desfazer() throws SQLException, RegraNegocioException {
        if (pilha.isEmpty()) {
            return;
        }
        pilha.pop().desfazer();
    }

    public boolean podeDesfazer() {
        return !pilha.isEmpty();
    }

    public String descricaoDoUltimo() {
        return pilha.isEmpty() ? "" : pilha.peek().descricao();
    }
}
