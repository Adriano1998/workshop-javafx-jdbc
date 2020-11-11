package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {
	
	//3 atributos dos itens de menu
	//tipo menuitem
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemAbout;
	
	//MÉTODOS PARA TRATAR CADA UMA DAS AÇÕES DESSES ITENS DE MENU
	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("Ação do menuitem vendedor");
	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	//metodo para carregar uma tela.
	private void loadView(String absoluteName) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			//pegou referencia para cena.
			Scene mainScene = Main.getMainScene();
			//metodo getroot pega o primeiro elemento da view.
			//esse getcontent ja é uma referencia para o que tiver dentro desse scrollpane.
			//acessa o scrollpane e depois o content para poder acessar o vbox do mainview.
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			//primeiro filho do vbox da janela principal - main menu
			Node mainMenu = mainVBox.getChildren().get(0);
			//limpar todos os filhos do mainvbox.
			mainVBox.getChildren().clear();
			
			//vai adicionar ao mainvbox: adicionar o mainmenu e os filhos do newvbox.
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			
			//com tudo isso, consegue manipular a cena principal incluindo alem do main menu, os filhos da janela que tiver abrindo.
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
	//processo manual de injetar dependencia no controller e depois chamar para atualizar os dados na tela do tableview. 
	private void loadView2(String absoluteName) {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			//a partir do loader pode pegar a referencia para o controller da view.
			DepartmentListController controller  = loader.getController();
			//injetou a dependencia
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a página", e.getMessage(), AlertType.ERROR);
		}
	}

}
