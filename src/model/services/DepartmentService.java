package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
 
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	
	//metodo que vai retornar uma lista de departamentos
	public List<Department> findAll(){
		return dao.findAll();
	}
	
	//inserir um departamento no banco ou atualizar o ja existente.
	public void saveOrUpdate(Department obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	
}
