package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	// o primeiro string indica o nome do campo e o segundo string indica a mensagem de erro.
	private Map<String, String > errors = new HashMap<>();

	//for�ar a instancia��o da exce��o com string
	//passando para superclasse esse msg
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){
		return errors;
	}
	
	//metodo para permitir que seja adicionado um elemento nessa cole��o de erros.
	public void addError(String fieldName, String errorMessage) {
		errors.put(fieldName, errorMessage);
	}
}
