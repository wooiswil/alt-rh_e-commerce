package Gestion.repository;

import org.springframework.data.repository.CrudRepository;

import Gestion.model.Produit;
import Gestion.model.User;

public interface ProduitInterface extends CrudRepository<Produit, Integer> {
	
	Produit findByDesignation(String designation);

}

