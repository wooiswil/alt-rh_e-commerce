package Gestion.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Gestion.model.Panier;
import Gestion.repository.PanierInterface;

@Service
@Transactional
public class PanierImp {

	@Autowired
	
	PanierInterface cartInterface;
	
	// insertion d'un nouveau produit dans le panier
	public void addToCart(Panier cart) {
		
		cartInterface.save(cart);
	}
	
	// affichage de la liste des panier
	public List<Panier> getCartList() {
		
		List<Panier> listCart = (List<Panier>) cartInterface.findAll();
		
		return listCart;
	}
	
	public void modCart(Panier cart) {
		
		cartInterface.save(cart);
		
	}
	
	public void rmCart(int idCart) {
		
		cartInterface.deleteById(idCart);
	}
	
	public Panier searchById(int idClient) {
		
		Panier res = cartInterface.findById(idClient).get();
		
		return res;
	}
	
}
