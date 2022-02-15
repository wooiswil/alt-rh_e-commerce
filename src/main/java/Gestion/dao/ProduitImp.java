package Gestion.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Gestion.model.Produit;
import Gestion.repository.ProduitInterface;

@Service //session_save()
@Transactional // pour faire le commit() de la requete (équivalent .hibernatecfg.xml)
public class ProduitImp {
	
	// Creation d'objet de type UserInterface pour faire appelle aux methode de l'interface
	@Autowired
	ProduitInterface prdInterface;
	
	// Ajout d'un produit
	public void addPrd(Produit p) {
		
		prdInterface.save(p);
		
	}
	
	// Affichage (liste) des produits
	public List<Produit> getPrd(){
		
		List<Produit> listPrd = (List<Produit>) prdInterface.findAll();
		
		return listPrd;
	}
	
	// update
	public void modPrd(Produit p) {
		
		prdInterface.save(p);
		
	}
	
	// delete
	public void rmUser(int idUser) {
		
		prdInterface.deleteById(idUser);
		
	}
	
	// find by id
	public Produit searchPrd(int idPrd) {
		
		Produit resById = prdInterface.findById(idPrd).get();
		
		return resById;
	}
	
	// find by name
	
	public Produit resPrd(String designation) {
		
		Produit prdRes = prdInterface.findByDesignation(designation);
		
		return prdRes;
	}
}
