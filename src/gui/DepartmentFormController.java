package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	
	private DepartmentService service;
	
	//vai poder permitir outros objetos se inscreverem nessa lista e receberem o evento.
	private List <DataChangeListener> datachangelistener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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


	private Department getFormData() {
		Department obj = new Department();
		obj.setId(Utils.tryParseToInt( txtId.getText()));
		obj.setName(txtName.getText());
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
		Constraints.setTextFieldMaxLength(txtName, 30);
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
	}
}
