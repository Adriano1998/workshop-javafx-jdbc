package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

	//criou a dependencia
	private DepartmentService service;
	
	//criar referencias para os componentes da tela departmentlist.
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	//carregar os departamentos nela.
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("Ação do botão");
	}

	//uma forma de injetar dependencia sem usar o new.
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
	
	//acessar o serviço, carregar os departamento e jogar na observablelist.
	//depois disso associa o observablelist com o tableview e ai sim vao aparecer os departamentos.
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		List<Department> list = service.findAll();
		//instancia o observablelist pegando os dados originais da lista.
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}

}
