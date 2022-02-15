package Gestion.repository;

import org.springframework.data.repository.CrudRepository;

import Gestion.model.Employee;


public interface EmployeeInterface extends CrudRepository<Employee, Integer> {

	Employee findByEmail(String email);
}
