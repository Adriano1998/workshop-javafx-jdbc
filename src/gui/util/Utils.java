package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	//palco atual
	//para acessar o stage onde o controller que recebeu o evento est�.
	//se clicar no bot�o pega o stage daquele bot�o. 
	public static Stage currentStage(ActionEvent event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
}