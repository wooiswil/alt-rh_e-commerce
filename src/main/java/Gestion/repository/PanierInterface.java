package Gestion.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import Gestion.model.Panier;

public interface PanierInterface extends CrudRepository<Panier, Integer> {
	
	List<Panier> findByIdClient(int idClient);

}
