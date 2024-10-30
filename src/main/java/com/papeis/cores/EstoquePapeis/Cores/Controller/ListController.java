package com.papeis.cores.EstoquePapeis.Cores.Controller;

import com.papeis.cores.EstoquePapeis.Cores.Model.Alerts;
import com.papeis.cores.EstoquePapeis.Cores.Model.EntityProdutos;
import com.papeis.cores.EstoquePapeis.Cores.Service.ServiceProdutos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ListController implements Initializable {

    @FXML
    private TableView<EntityProdutos> tableViewProdutos;
    @FXML
    private TableColumn<EntityProdutos, Integer> tableColumnId;
    @FXML
    private TableColumn<EntityProdutos, String> tableColumnNome;
    @FXML
    private TableColumn<EntityProdutos, Integer> tableColumnQnt;
    @FXML
    private TableColumn<EntityProdutos, Double> tableColumnPreco;
    @FXML
    private TableColumn<EntityProdutos, Double> tableColumnTotalPreco;

    private ObservableList<EntityProdutos> obsList;
    @FXML
    private TextField textFieldDeletar;

    @FXML
    private Button buttonVoltar;

    @Autowired
    private ServiceProdutos serviceProdutos;

    private Stage stage; // Nova variável para armazenar a referência do Stage

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
        updateTableView();
    }

    // Método para configurar o Stage
    public void setStage(Stage stage) {
        this.stage = stage;
        initializeNodes(); // Chama a inicialização dos nós aqui também
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnQnt.setCellValueFactory(new PropertyValueFactory<>("qnt"));
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tableColumnTotalPreco.setCellValueFactory(new PropertyValueFactory<>("totalPreco"));

        if (stage != null) {
            tableViewProdutos.prefHeightProperty().bind(stage.heightProperty());
        }
    }

    @FXML
    public void updateTableView() {
        if (serviceProdutos == null) {
            throw new IllegalStateException("Service está null");
        }
        List<EntityProdutos> list = serviceProdutos.findAllProdutos();
        obsList = FXCollections.observableArrayList(list);
        tableViewProdutos.setItems(obsList);
    }

    @FXML
    public void onDelete() {
        String del = textFieldDeletar.getText();
        if (del.isEmpty()) {
            Alerts.showAlert("Erro", "O campo de deletar não pode estar vazio.", "Por favor, insira um ID.", Alert.AlertType.ERROR);
            return;
        }
        try {
            Integer num = Integer.parseInt(del);
            serviceProdutos.deleteProduct(num);
            updateTableView();
            textFieldDeletar.clear();
        } catch (NumberFormatException e) {
            Alerts.showAlert("Erro", "ID inválido", "O ID deve ser um número inteiro.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onVoltarPaginaAnterior() {
        clear();  // Limpa os campos antes de voltar para a página anterior

        Stage stage = (Stage) buttonVoltar.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));

        try {
            ScrollPane view = loader.load();  // Carrega o layout FXML do ViewController
            Scene scene = new Scene(view);  // Cria uma nova cena com o layout carregado
            stage.setScene(scene);  // Define a nova cena no stage atual
            stage.show();  // Exibe a nova cena
        } catch (IOException e) {
            e.printStackTrace();
            Alerts.showAlert("Erro", "Não foi possível carregar a tela anterior", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método para limpar os dados do controlador
    public void clear() {
        if (obsList != null) {
            obsList.clear();  // Limpa os itens da lista observável
            tableViewProdutos.setItems(obsList);  // Atualiza a tabela para refletir as mudanças
        }
        textFieldDeletar.clear();  // Limpa o campo de entrada
    }
}
