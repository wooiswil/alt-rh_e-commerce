package Gestion.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Gestion.dao.PanierImp;
import Gestion.dao.ProduitImp;
import Gestion.model.Panier;
import Gestion.model.Produit;

@Controller
public class DefaultController {
	
	@Autowired
	ProduitImp prdImp;
	
	@Autowired
	PanierImp cartImp;

	@RequestMapping("/")
	public String toIndex(HttpSession session, Model mo) {
		
		// verification des identifiants de l'usr spécifié dans le UserController
		if(session.getAttribute("usr") != null ) {
			if (session.getAttribute("usrId") != null) {
//				System.out.println(session.getAttribute("usr"));
				// récuperation de l'id
				String userCoId = session.getAttribute("usrId").toString();
				mo.addAttribute("userCoId", userCoId);
				
				// pour l'affichage de la liste des paniers
				List<Panier> listCart = (List<Panier>) cartImp.retEnrUser(Integer.parseInt(userCoId));
				mo.addAttribute("cartById", listCart);
				
//				if(listCart != null) {
//					Produit prdId = prdImp.searchPrd(Integer.parseInt(userCoId)).getId();
//				}
				
				// compte le nombre d'enregistrement pour l'aff
				mo.addAttribute("nbPanier", listCart.size());
				
				// recupération et enregistrement des identifiants de connexion attribués à userCo pour la session
				String userCo = session.getAttribute("usr").toString();
				mo.addAttribute("userCo", userCo);
				
			}
			
		}
		
		if(session.getAttribute("msgE") != null) {

			mo.addAttribute("msgE", session.getAttribute("msgE"));
		} else {
			mo.addAttribute("msgE", null);
		}
		// pour l'affichage des produits
				List<Produit> listPrd = (List<Produit>) prdImp.getPrd();
				System.out.println(listPrd.size());
				// creation d'attributs pour l'affichage
				mo.addAttribute("produit", listPrd);
				
		return "index"; 
	}
	
	@RequestMapping("/logout")
	public String toLeave(HttpSession session, Model mo) {
			
		// affectation de la valeur null à la session de l'user qui permet la deconnexion
			session.setAttribute("usr", null);
			session.setAttribute("msgE", null);
			
			
		return "redirect:/"; 
	}

	@RequestMapping("/form")
	public String toFormulaire(@RequestParam(name="name", required=false) String name, Model mo) {  // fonction
		mo.addAttribute("name", name);
		return "formulaire"; // return 
	}

}
