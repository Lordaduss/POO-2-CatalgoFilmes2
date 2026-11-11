package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Alerta;

public class MenuController {

    @FXML
    private void abrirGeneros() {
        abrir("/view/GeneroView.fxml", "Gerenciar Gêneros");
    }

    @FXML
    private void abrirFilmes() {
        abrir("/view/FilmeView.fxml", "Gerenciar Filmes");
    }

    private void abrir(String caminho, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminho));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            Alerta.erro("Erro ao abrir a tela: " + e.getMessage());
        }
    }
}
