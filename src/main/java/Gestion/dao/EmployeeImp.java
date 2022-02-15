package Gestion.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Gestion.model.Employee;
import Gestion.model.Panier;
import Gestion.repository.EmployeeInterface;

@Service
@Transactional
public class EmployeeImp {

	@Autowired
	EmployeeInterface empInterface;
	
	// insertion d'un nouveau produit dans le panier
		public void addEmp(Employee emp) {
			
			empInterface.save(emp);
		}
		
		// affichage de la liste des panier
		public List<Employee> getEmpList() {
			
			List<Employee> listEmp = (List<Employee>) empInterface.findAll();
			
			return listEmp;
		}
		
		// function de modification (update)
		public void modEmp(Employee emp) {
			
			empInterface.save(emp);
			
		}
		
		// function de suppression (delete)
		public void rmEmp(int idEmp) {
			
			empInterface.deleteById(idEmp);
		}
		
		// function de recherche (select)
		public Employee searchEmp(int idEmp) {
			
			Employee  returnSearch = empInterface.findById(idEmp).get();
			
			return returnSearch;
		}
		
//		// func d'auth
		public Employee authEmailEmp(String email) {
			
			Employee returnVerif = empInterface.findByEmail(email);
			
			return returnVerif;
		}
}
