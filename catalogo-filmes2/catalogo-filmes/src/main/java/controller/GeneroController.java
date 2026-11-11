package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Genero;
import service.GeneroService;
import service.RegraNegocioException;
import service.Servicos;
import util.Alerta;

import java.sql.SQLException;

public class GeneroController {

    @FXML private TextField txtNome;
    @FXML private TableView<Genero> tabela;
    @FXML private TableColumn<Genero, Integer> colId;
    @FXML private TableColumn<Genero, String> colNome;

    private final GeneroService service = Servicos.GENERO;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                txtNome.setText(novo.getNome());
            }
        });

        carregar();
    }

    private void carregar() {
        try {
            tabela.setItems(FXCollections.observableArrayList(service.listar()));
        } catch (SQLException e) {
            Alerta.erro("Erro ao carregar gêneros: " + e.getMessage());
        }
    }

    @FXML
    private void inserir() {
        try {
            service.inserir(new Genero(0, txtNome.getText().trim()));
            limpar();
            carregar();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao inserir gênero: " + e.getMessage());
        }
    }

    @FXML
    private void atualizar() {
        Genero selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alerta.aviso("Selecione um gênero na tabela.");
            return;
        }
        try {
            service.atualizar(new Genero(selecionado.getId(), txtNome.getText().trim()));
            limpar();
            carregar();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao atualizar gênero: " + e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        Genero selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alerta.aviso("Selecione um gênero na tabela.");
            return;
        }
        if (!Alerta.confirmar("Excluir o gênero \"" + selecionado.getNome() + "\"?")) {
            return;
        }
        try {
            service.excluir(selecionado);
            limpar();
            carregar();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao excluir gênero: " + e.getMessage());
        }
    }

    @FXML
    private void limpar() {
        txtNome.clear();
        tabela.getSelectionModel().clearSelection();
    }
}
