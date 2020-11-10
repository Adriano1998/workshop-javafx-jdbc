package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	//criar referencias para os componentes da tela departmentlist.
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("Ação do botão");
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}


	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//pegar a referencia para o stage atual.
		//acessa a classe principal, acessa a cena, chama o get window e pega a referencia para janela, esse getwindow é superclasse do stage, entao
		//faz o downcasting para stage.
		Stage stage = (Stage)Main.getMainScene().getWindow();
		//comando para o tableviewdepartment acompanhar a janela
		//macete para o tableview acompanhar a altura da janela.
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
