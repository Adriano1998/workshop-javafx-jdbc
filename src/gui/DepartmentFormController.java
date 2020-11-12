package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {

	private Department entity;
	
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
	
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onbtsaveaction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onbtcancelaction");
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
