package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	
	private SellerService service;
	
	private DepartmentService departmentService;
	
	//vai poder permitir outros objetos se inscreverem nessa lista e receberem o evento.
	private List <DataChangeListener> datachangelistener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;
	
	
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorBirthDate;
	
	@FXML
	private Label labelErrorBaseSalary;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	
	private ObservableList<Department> obsList;
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	
	
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
		
	}
	
	//esse metodo vai escrever esse listener na lista.
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		datachangelistener.add(listener);
	}
	
	//esse getformdata vai pegar os dados das caixinhas do formulario e instanciar um departamento.
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListener();
			//pegar a referencia da janela e fechar a janela
			Utils.currentStage(event).close();
		}
		
		catch(ValidationException e) {
			//vai ser uma coleção de erros.
			setErrorMessages(e.getErrors());
		}
		
	catch(DbException e) {
		Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
	}
	}
	
	//vai executar o metodo ondatachanged da interface datachangelistener
	//vai emitir o evento ondatachanged para cada listener.
	private void notifyDataChangeListener() {
		//para cada datachangelistener pertencente a lista datachangerlisteners
		for(DataChangeListener listener : datachangelistener) {
			listener.OnDataChanged();
		}
		
		
	}


	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException exception = new ValidationException("Validation error");
		
		
		obj.setId(Utils.tryParseToInt( txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can´t be empty");
		}
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size()>0) {
			throw exception;
		}
		
		return obj;
	}


	@FXML
	public void onBtCancelAction(ActionEvent event) {
		//botao de cancelar fecha a janela.
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd//MM//yyyy");
		initializeComboBoxDepartment();
	}
	
	//jogar nas caixas de texto os dados que estão no objeto entity la de cima.
	public void updateFormData() {
		//verificação para testar se o entity é null
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		//a caixa de texto trabalha com string - entao converte int para string.
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f",entity.getBaseSalary()));
		if(entity.getBirthDate()!=null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(),ZoneId.systemDefault()));
		}
		
		//vai definir para que o combobox esteja selecionado no primeiro elemento.
		if(entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else {
			//o departamento que estiver associado com o vendedor vai para o combobox.
			comboBoxDepartment.setValue(entity.getDepartment());
		}
		
		
	}
	
	public void loadAssociatedObjects() {
		
		if(departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		//jogar os departamento para a observablelist:
		obsList = FXCollections.observableArrayList(list);
		//setar a lista como a lista que vai ta associada ao combobox:
		comboBoxDepartment.setItems(obsList);
	}
	
	
	//essa coleção vai carregar os erros, percorrer essa coleção, preenchendo os erros nas caixas de texto.
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

	
	
	
}
