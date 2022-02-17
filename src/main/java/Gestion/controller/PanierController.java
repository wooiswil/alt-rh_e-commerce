package Gestion.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import Gestion.dao.PanierImp;
import Gestion.model.Panier;

@Controller
@RequestMapping("/Panier")
public class PanierController {
	
	@Autowired
	PanierImp cartImp;

	@RequestMapping(value = "/AddCart", method = RequestMethod.POST)
	public String toAddToCart(
			
					// recuperation des champs de formulaire 
						@RequestParam(name="idClient", required = false)String idClient,
						@RequestParam(name="idProduit", required = false) String idProduit,
						@RequestParam(name= "qteProduit", required = false) String qteProduit
			)  {

		
		// Instance de la classe Panier
		Panier cart = new Panier();
		
		cart.setIdClient(Integer.parseInt(idClient));
		cart.setIdProduit(Integer.parseInt(idProduit));
		cart.setQteProduit(Integer.parseInt(qteProduit));
		cart.setCommande(0);
		
		// func d'ajout
		cartImp.addToCart(cart);
		System.out.println("panier ajouté par :" + cart.getIdClient());
	return "redirect:/";
}
}
