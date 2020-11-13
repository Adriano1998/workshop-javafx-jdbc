package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	//criou a dependencia
	private SellerService service;
	
	//criar referencias para os componentes da tela departmentlist.
	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEdit;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnRemove;
	
	//carregar os departamentos nela.
	private ObservableList<Seller> obsList;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		//acessando o stage - pega referencia pro stage atual e passa para criar a janela de formulario.
		Stage parentStage = Utils.currentStage(event);
		
		Seller obj = new Seller();
		
	//	createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	//uma forma de injetar dependencia sem usar o new.
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}


	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		//pegar a referencia para o stage atual.
		//acessa a classe principal, acessa a cena, chama o get window e pega a referencia para janela, esse getwindow é superclasse do stage, entao
		//faz o downcasting para stage.
		Stage stage = (Stage)Main.getMainScene().getWindow();
		//comando para o tableviewdepartment acompanhar a janela
		//macete para o tableview acompanhar a altura da janela.
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}
	
	//acessar o serviço, carregar os departamento e jogar na observablelist.
	//depois disso associa o observablelist com o tableview e ai sim vao aparecer os departamentos.
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		List<Seller> list = service.findAll();
		//instancia o observablelist pegando os dados originais da lista.
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		
		initEditButtons();
		initRemoveButtons();
	}
	//vai receber por parametro uma referencia para o stage da janela que criou a janela de dialogo
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); 
			
			//pegou o controlador da tela que acabou de carregar
			SellerFormController controller = loader.getController();
			//injetar nesse controlador o departamento.
			controller.setSeller(obj);
			//chamar o metodo update para carregar os dados do obj no formulario.
			
			controller.setSellerService(new SellerService());
			
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

	
	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
					event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");
			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		
		});
	}
	//implementação da remoção do departamento
	//operação para remover uma entidade.
	private void removeEntity(Seller obj) {
		// o resultado do alert - que será o botão clicado - vai atribuir a uma variavel
		Optional <ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		//se ele apertou no ok, confirma a deleção.
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}

}
