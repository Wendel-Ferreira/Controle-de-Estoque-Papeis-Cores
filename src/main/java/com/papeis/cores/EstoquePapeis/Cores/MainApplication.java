package com.papeis.cores.EstoquePapeis.Cores;

import com.papeis.cores.EstoquePapeis.Cores.Controller.ViewController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Stack;

@SpringBootApplication
@EntityScan(basePackages = "com.papeis.cores.EstoquePapeis.Cores.Model") //Não sabia que precisava utilizar Scan para o SpringBoot identificar a aplicação quando não está no mesmo package
public class MainApplication extends Application { //O Application é de uma class abstract para starter o JavaFX

	private ConfigurableApplicationContext springContext; //Essa configuração é que faz o Spring Boot funcionar com JavaFX


	private static Scene mainScene; //Criando a Cena principal
	@Autowired
	private ViewController viewController;
	@FXML
	private Button viewList; //Preciso mapear esse botão no Builder e contruir a função

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void init(){springContext = SpringApplication.run(MainApplication.class);

	}

	@Override
	public void start(Stage primarystage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		ScrollPane scrollPane = fxmlLoader.load();

		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);

		mainScene = new Scene(scrollPane);
		primarystage.setScene(mainScene);
		primarystage.setTitle("Estoque Papeis & Cores");
		primarystage.show();
	}
	private static Stack<Scene> sceneHistory = new Stack<>();  // Armazena as cenas

	public static void pushScene(Scene scene) {
		sceneHistory.push(scene);
	}

	public static Scene popScene() {
		return sceneHistory.isEmpty() ? null : sceneHistory.pop();
	}
	@Override
	public void stop() {
		springContext.close();
	}
	public static Scene getMainScene(){
		return mainScene;
	}

	@FXML
	public void onListProdutos(){
	viewController.onButtonList();
	}

}
