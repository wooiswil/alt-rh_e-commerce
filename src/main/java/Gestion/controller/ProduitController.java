package Gestion.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Gestion.dao.ProduitImp;
import Gestion.model.Produit;

@Controller
public class ProduitController {
	
	@Autowired
	ProduitImp prdImp;
	
	@RequestMapping("/getFormPrd")
		public String toGetPrd(
				
				// recuperation des champs de formulaire 
				@RequestParam(name="ref", required = false)String ref,
				@RequestParam(name="designation", required = false) String designation,
				@RequestParam(name="prix", required = false) String prix, 
				@RequestParam(name="poids", required = false) String poids, 
				@RequestParam(name="qteProduit", required = false) String qteProduit,
				Model mod) {
		
		// Instance de la classe Produit
			Produit p = new Produit();
			
			// setter les valeurs des champs du form dans les attributs de l'entité (Produit)
			
			p.setRef(ref);
			p.setDesignation(designation);
			p.setPrix(Double.parseDouble(prix));
			p.setPoids(Double.parseDouble(poids));
			p.setQteProduit(Integer.parseInt(qteProduit));
			
			// Appelle de la methode d'implémentation ==> ici l'ajout 
			prdImp.addPrd(p);
			
			// verification
			System.out.println("Insertion du produit " + p.getDesignation());
			
		return "redirect:/getPrd";
	}

}
