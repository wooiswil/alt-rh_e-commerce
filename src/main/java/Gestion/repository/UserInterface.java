package Gestion.repository;

import org.springframework.data.repository.CrudRepository;

import Gestion.model.User;

public interface UserInterface extends CrudRepository<User, Integer> {
	// "Integer" ==> fait référence au type de la clé primaire de l'entité qui est de type int 
	
	User findByEmail(String email);
	
	User findByMdp(String mdp);
	
}
