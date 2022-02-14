package Gestion.repository;

import org.springframework.data.repository.CrudRepository;

import Gestion.model.Produit;

public interface ProduitInterface extends CrudRepository<Produit, Integer> {

}

