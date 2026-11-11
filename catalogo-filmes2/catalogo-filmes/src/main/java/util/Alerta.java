package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerta {

    public static void erro(String mensagem) {
        mostrar(Alert.AlertType.ERROR, mensagem);
    }

    public static void aviso(String mensagem) {
        mostrar(Alert.AlertType.WARNING, mensagem);
    }

    public static boolean confirmar(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensagem);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private static void mostrar(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
