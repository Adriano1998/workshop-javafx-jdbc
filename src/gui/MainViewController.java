package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
import model.services.SellerService;

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
	public void onMenuItemSellerAction() {
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemDepartamentAction() {
		//função para inicializar o controlador. Ação de inicialização do controller departmentlistcontroller.
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x->{});
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	//com tudo isso, consegue manipular a cena principal incluindo alem do main menu, os filhos da janela que tiver abrindo.
	
	//metodo para carregar uma tela.
	private <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
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
			
			//getcontroller vai retornar o controlador do tipo que for chamado. Ou seja, o departmentlistcontroller como foi especificado la em cima.
			T controller = loader.getController();
		
			
			//para executar o initializingaction: tem que chamar a função accept do consumer.
			initializingAction.accept(controller);
			
			//essas 2 linhas de cima vão fazer com que seja exectuado a função que for passada como argumento
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Erro carregando a página", e.getMessage(), AlertType.ERROR);
		}
	}
	
	
}
