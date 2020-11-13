package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

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
	public void onBtNewAction(ActionEvent event) {
		//acessando o stage - pega referencia pro stage atual e passa para criar a janela de formulario.
		Stage parentStage = Utils.currentStage(event);
		
		Department obj = new Department();
		
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
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
	//vai receber por parametro uma referencia para o stage da janela que criou a janela de dialogo
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); 
			
			//pegou o controlador da tela que acabou de carregar
			DepartmentFormController controller = loader.getController();
			//injetar nesse controlador o departamento.
			controller.setDepartment(obj);
			//chamar o metodo update para carregar os dados do obj no formulario.
			
			controller.setDepartmentService(new DepartmentService());
			
			//esta se inscrevendo para receber o evento. Quando o evento for disparado vai ser executado o metodo ondatachanged.
			controller.subscribeDataChangeListener(this);
			
			controller.updateFormData();
			
			
			
			
			//criar uma variavel do tipo stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter department data");
			//cria uma nova cena - elemento raiz da cena é o pane.
			dialogStage.setScene(new Scene(pane));
			//uma janela que não pode ser redimensionada.
			dialogStage.setResizable(false);
			
			dialogStage.initOwner(parentStage);
			//aqui que fala se a janela será modal ou com outro comportamento.
			//enquanto não fechar não acessa a janela anterior.
			dialogStage.initModality(Modality.WINDOW_MODAL);
			//funcao para carregar a janela do formulario para preencher um novo departamento.
			dialogStage.showAndWait();
			
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	//o que vai ter que executar no departmentlistcontroller quando receber uma notificação que os dados foram atualizados
	//função que faz essa atualização : updatetableview.
	//na hora que disparar esse evento que alteraram os dados, chama a função updatetableview.
	@Override
	public void OnDataChanged() {
	updateTableView();
		
	}

}
