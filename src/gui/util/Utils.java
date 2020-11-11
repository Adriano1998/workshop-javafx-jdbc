package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	//palco atual
	//para acessar o stage onde o controller que recebeu o evento está.
	//se clicar no botão pega o stage daquele botão. 
	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
}
