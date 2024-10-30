package com.papeis.cores.EstoquePapeis.Cores.Controller;

import com.papeis.cores.EstoquePapeis.Cores.MainApplication;
import com.papeis.cores.EstoquePapeis.Cores.Model.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Controller
public class ViewController implements Initializable {

    @FXML
    private MenuItem menuItemAdd;
    @FXML
    private MenuItem menuItemLista;
    @FXML
    private MenuItem menuItemEditar;
    @FXML
    private MenuItem menuItemVoltar;

    @Autowired
    private ConfigurableApplicationContext springContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializações necessárias
    }

    // Método para carregar a tela de adicionar produto
    public void onButtonAdd() {
        loadScrollPaneView("/EditProduct.fxml", EditProductController::updateTableView);
    }

    // Método para carregar a lista de produtos
    @FXML
    public void onButtonList() {
        loadView("/ProductionList.fxml", ListController::updateTableView);
    }

    private <T> void loadView(String nameCaminho, Consumer<T> initializangAction) {
        try {
            URL fxmlLocation = getClass().getResource(nameCaminho);
            if (fxmlLocation == null) {
                throw new IOException("O arquivo FXML '" + nameCaminho + "' não foi encontrado.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setControllerFactory(springContext::getBean);
            VBox newVBox = loader.load();

            Scene currentScene = MainApplication.getMainScene();
            MainApplication.pushScene(currentScene);

            VBox mainVBox = (VBox) ((ScrollPane) currentScene.getRoot()).getContent();
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            // Chama o método clear() se o controlador for uma instância de ListController
            T controller = loader.getController();
            if (controller instanceof ListController) {
                ((ListController) controller).clear();  // Limpa o controlador anterior
            }

            initializangAction.accept(controller);
        } catch (IOException e) {
            Alerts.showAlert("Erro", "Erro ao carregar a página", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Método genérico para carregar uma nova cena com ScrollPane
    private <T> void loadScrollPaneView(String fxmlPath, Consumer<T> initializationAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            ScrollPane newScrollPane = loader.load();  // Carrega o novo ScrollPane

            Scene mainScene = MainApplication.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);  // Mantém o menu
            mainVBox.getChildren().clear();  // Limpa o VBox principal
            mainVBox.getChildren().add(mainMenu);  // Adiciona o menu de volta
            mainVBox.getChildren().add(newScrollPane.getContent());  // Adiciona o conteúdo da nova tela

            T controller = loader.getController();  // Obtém o controller da nova tela
            initializationAction.accept(controller);  // Inicializa o controller
        } catch (IOException e) {
            Alerts.showAlert("Erro", "Erro ao carregar a página", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void onVoltarPaginaAnterior() {
        Stage stage = (Stage) menuItemVoltar.getParentPopup().getOwnerWindow(); // Obtém a janela do stage

        try {
            // Verifique o caminho do FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            loader.setControllerFactory(springContext::getBean); // Use a factory do Spring para injeção de dependência
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
