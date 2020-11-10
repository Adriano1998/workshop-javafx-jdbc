package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("Ação do menuitem departamento");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		System.out.println("Ação do menuitem About");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
		
	}
	
	
	

}
