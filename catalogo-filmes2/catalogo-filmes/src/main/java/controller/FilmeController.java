package controller;

import command.AtualizarFilmeCommand;
import command.ExcluirFilmeCommand;
import command.HistoricoComandos;
import command.InserirFilmeCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Filme;
import model.FilmeBuilder;
import model.Genero;
import observer.GeneroObserver;
import service.FilmeService;
import service.GeneroService;
import service.RegraNegocioException;
import service.Servicos;
import util.Alerta;

import java.sql.SQLException;

// Padrão Observer: este controller é o Observer, recarrega os gêneros quando eles mudam
// Padrão Command: este controller é o Client, cria os comandos e os entrega ao histórico
public class FilmeController implements GeneroObserver {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDiretor;
    @FXML private TextField txtAno;
    @FXML private TextField txtNota;
    @FXML private ComboBox<Genero> cmbGenero;
    @FXML private Button btnDesfazer;

    @FXML private TableView<Filme> tabela;
    @FXML private TableColumn<Filme, Integer> colId;
    @FXML private TableColumn<Filme, String> colTitulo;
    @FXML private TableColumn<Filme, String> colDiretor;
    @FXML private TableColumn<Filme, Integer> colAno;
    @FXML private TableColumn<Filme, Double> colNota;
    @FXML private TableColumn<Filme, String> colGenero;

    private final FilmeService filmeService = Servicos.FILME;
    private final GeneroService generoService = Servicos.GENERO;
    private final HistoricoComandos historico = new HistoricoComandos();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDiretor.setCellValueFactory(new PropertyValueFactory<>("diretor"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colNota.setCellValueFactory(new PropertyValueFactory<>("nota"));
        colGenero.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGenero().getNome()));

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                preencherCampos(novo);
            }
        });

        carregarGeneros();
        carregarFilmes();
        atualizarBotaoDesfazer();

        generoService.adicionarObserver(this);
        tabela.sceneProperty().addListener((o, antiga, scene) -> {
            if (scene != null) {
                scene.windowProperty().addListener((o2, antiga2, janela) -> {
                    if (janela != null) {
                        janela.setOnHidden(e -> generoService.removerObserver(this));
                    }
                });
            }
        });
    }

    @Override
    public void generosAlterados() {
        Genero selecionado = cmbGenero.getValue();
        carregarGeneros();
        if (selecionado != null) {
            for (Genero g : cmbGenero.getItems()) {
                if (g.getId() == selecionado.getId()) {
                    cmbGenero.setValue(g);
                }
            }
        }
        carregarFilmes();
    }

    private void carregarGeneros() {
        try {
            cmbGenero.setItems(FXCollections.observableArrayList(generoService.listar()));
        } catch (SQLException e) {
            Alerta.erro("Erro ao carregar gêneros: " + e.getMessage());
        }
    }

    private void carregarFilmes() {
        try {
            tabela.setItems(FXCollections.observableArrayList(filmeService.listar()));
        } catch (SQLException e) {
            Alerta.erro("Erro ao carregar filmes: " + e.getMessage());
        }
    }

    private void atualizarBotaoDesfazer() {
        btnDesfazer.setDisable(!historico.podeDesfazer());
        btnDesfazer.setText(historico.podeDesfazer()
                ? "Desfazer: " + historico.descricaoDoUltimo() : "Desfazer");
    }

    @FXML
    private void desfazer() {
        try {
            historico.desfazer();
            limpar();
            carregarFilmes();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao desfazer: " + e.getMessage());
        }
        atualizarBotaoDesfazer();
    }

    private void preencherCampos(Filme f) {
        txtTitulo.setText(f.getTitulo());
        txtDiretor.setText(f.getDiretor() == null ? "" : f.getDiretor());
        txtAno.setText(String.valueOf(f.getAno()));
        txtNota.setText(f.getNota() == null ? "" : String.valueOf(f.getNota()));
        for (Genero g : cmbGenero.getItems()) {
            if (g.getId() == f.getGenero().getId()) {
                cmbGenero.setValue(g);
            }
        }
    }

    private Filme lerFormulario(int id) {
        int ano;
        try {
            ano = Integer.parseInt(txtAno.getText().trim());
        } catch (NumberFormatException e) {
            Alerta.aviso("Ano inválido.");
            return null;
        }

        Double nota = null;
        String textoNota = txtNota.getText().trim().replace(",", ".");
        if (!textoNota.isEmpty()) {
            try {
                nota = Double.parseDouble(textoNota);
            } catch (NumberFormatException e) {
                Alerta.aviso("Nota inválida.");
                return null;
            }
        }

        return new FilmeBuilder()
                .id(id)
                .titulo(txtTitulo.getText().trim())
                .diretor(txtDiretor.getText().trim())
                .ano(ano)
                .nota(nota)
                .genero(cmbGenero.getValue())
                .build();
    }

    @FXML
    private void inserir() {
        Filme filme = lerFormulario(0);
        if (filme == null) {
            return;
        }
        try {
            historico.executar(new InserirFilmeCommand(filmeService, filme));
            limpar();
            carregarFilmes();
            atualizarBotaoDesfazer();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao inserir filme: " + e.getMessage());
        }
    }

    @FXML
    private void atualizar() {
        Filme selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alerta.aviso("Selecione um filme na tabela.");
            return;
        }
        Filme filme = lerFormulario(selecionado.getId());
        if (filme == null) {
            return;
        }
        try {
            historico.executar(new AtualizarFilmeCommand(filmeService, selecionado, filme));
            limpar();
            carregarFilmes();
            atualizarBotaoDesfazer();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao atualizar filme: " + e.getMessage());
        }
    }

    @FXML
    private void excluir() {
        Filme selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alerta.aviso("Selecione um filme na tabela.");
            return;
        }
        if (!Alerta.confirmar("Excluir o filme \"" + selecionado.getTitulo() + "\"?")) {
            return;
        }
        try {
            historico.executar(new ExcluirFilmeCommand(filmeService, selecionado));
            limpar();
            carregarFilmes();
            atualizarBotaoDesfazer();
        } catch (RegraNegocioException e) {
            Alerta.aviso(e.getMessage());
        } catch (SQLException e) {
            Alerta.erro("Erro ao excluir filme: " + e.getMessage());
        }
    }

    @FXML
    private void limpar() {
        txtTitulo.clear();
        txtDiretor.clear();
        txtAno.clear();
        txtNota.clear();
        cmbGenero.setValue(null);
        tabela.getSelectionModel().clearSelection();
    }
}
