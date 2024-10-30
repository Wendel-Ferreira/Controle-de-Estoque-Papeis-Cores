package com.papeis.cores.EstoquePapeis.Cores.Controller;

import com.papeis.cores.EstoquePapeis.Cores.MainApplication;
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
public class EditProductController implements Initializable {

    @FXML
    private TextField textFieldCod;
    @FXML
    private TextField textFieldNome;
    @FXML
    private TextField textFieldQnt;
    @FXML
    private TextField textFieldPreco;
    @FXML
    private TextField textFieldTotal;

    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonVoltar;

    @FXML
    private TableView<EntityProdutos> tableViewMain;
    @FXML
    private TableColumn<EntityProdutos, Integer> tableColumnCod;
    @FXML
    private TableColumn<EntityProdutos, String> tableColumnNome;
    @FXML
    private TableColumn<EntityProdutos, Integer> tableColumnQnt;
    @FXML
    private TableColumn<EntityProdutos, Double> tableColumnPreco;
    @FXML
    private TableColumn<EntityProdutos, Double> tableColumnTotal;

    @Autowired
    private ServiceProdutos serviceProdutos;

    private ObservableList<EntityProdutos> obsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
        updateTableView();
    }

    private void initializeNodes() {
        tableColumnCod.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnQnt.setCellValueFactory(new PropertyValueFactory<>("qnt"));
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("totalPreco"));

        Stage stage = (Stage) MainApplication.getMainScene().getWindow();
        tableViewMain.prefHeightProperty().bind(stage.heightProperty());
    }


    @FXML
    public void updateTableView() {
        if (serviceProdutos == null) {
            throw new IllegalStateException("Service está null");
        }
        List<EntityProdutos> list = serviceProdutos.findAllProdutos();
        obsList = FXCollections.observableArrayList(list);
        tableViewMain.setItems(obsList);
    }

    @FXML
    public void onAddProduto() {
        String nomeProduto = textFieldNome.getText();

        String qntProduto = textFieldQnt.getText();
        Integer ConvertorQnt = Integer.parseInt(qntProduto);

        String precoProduto = textFieldPreco.getText();
        Double ConvertorPreco = Double.parseDouble(precoProduto);

        EntityProdutos novoProduto = new EntityProdutos(nomeProduto, ConvertorQnt, ConvertorPreco);
        serviceProdutos.SaveProduct(novoProduto);
        updateTableView();
        textFieldCod.clear();
        textFieldNome.clear();
        textFieldQnt.clear();
        textFieldPreco.clear();
    }

    @FXML
    public void onEditarProduto() {
        try {
            // Recuperar o ID do produto do campo de texto
            String codProduto = textFieldCod.getText();
            if (codProduto.isEmpty()) {
                throw new IllegalArgumentException("ID do produto não pode estar vazio");
            }
            Integer ConvertorCod = Integer.parseInt(codProduto);

            // Verificar se o produto com esse ID existe no banco
            EntityProdutos produtoExistente = serviceProdutos.findById(ConvertorCod);
            if (produtoExistente == null) {
                throw new IllegalArgumentException("Produto com ID " + ConvertorCod + " não foi encontrado.");
            }

            // Atualizar os campos do produto com os valores fornecidos
            String nomeProduto = textFieldNome.getText();
            if (!nomeProduto.isEmpty()) {
                produtoExistente.setNome(nomeProduto);  // Atualizar o nome
            }

            String qntProduto = textFieldQnt.getText();
            if (!qntProduto.isEmpty()) {
                Integer ConvertorQnt = Integer.parseInt(qntProduto);
                produtoExistente.setQnt(ConvertorQnt);  // Atualizar a quantidade
            }

            String precoProduto = textFieldPreco.getText();
            if (!precoProduto.isEmpty()) {
                Double ConvertorPreco = Double.parseDouble(precoProduto);
                produtoExistente.setPreco(ConvertorPreco);  // Atualizar o preço
            }

            // Recalcular o total (caso necessário)
            produtoExistente.setTotalPreco(produtoExistente.getQnt() * produtoExistente.getPreco());

            // Salvar as alterações no banco
            serviceProdutos.SaveProduct(produtoExistente);

            // Atualizar a tabela de visualização
            updateTableView();

            // Limpar os campos após a edição
            textFieldCod.clear();
            textFieldNome.clear();
            textFieldQnt.clear();
            textFieldPreco.clear();

        } catch (NumberFormatException e) {
            // Tratamento de erro para conversão de números inválidos
            throw new IllegalArgumentException("Entrada inválida para quantidade ou preço");
        } catch (IllegalArgumentException e) {
            // Tratamento de erro quando o ID não é encontrado ou outra validação falha
            Alerts.showAlert("Erro ao Editar Produto", "Edição Falhou", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void onVoltarPaginaAnterior() {
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


}
