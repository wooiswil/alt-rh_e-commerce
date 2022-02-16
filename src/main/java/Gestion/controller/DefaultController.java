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
				
				// récuperation de l'id
				String userCoId = session.getAttribute("usrId").toString();
				mo.addAttribute("userCoId", userCoId);
				
				// recupération et enregistrement des identifiants de connexion attribués à userCo pour la session
				String userCo = session.getAttribute("usr").toString();
				System.out.println(userCo);
				mo.addAttribute("userCo", userCo);
				
			}
			
		}
		
		if(session.getAttribute("msgE") != null) {

			mo.addAttribute("msgE", session.getAttribute("msgE"));
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
			
		return "redirect:/"; 
	}

	@RequestMapping("/form")
	public String toFormulaire(@RequestParam(name="name", required=false) String name, Model mo) {  // fonction
		mo.addAttribute("name", name);
		return "formulaire"; // return 
	}

//	@RequestMapping("/getForm")
//	public String affForm(@RequestParam("nom") String nom, @RequestParam("prenom") String prenom,
//			@RequestParam("email") String email, @RequestParam("mdp") String mdp, @RequestParam("tel") String tel,
//			@RequestParam("age") int age, Model mod) { // pour recuperer les para
//		System.out.println("Votre nom est : " + nom + "\n " + "Votre prenom est : " + prenom + "\n "
//				+ "Votre email est : " + email + " \n " + "et votre age est : " + age);
//		// Model mod ==> permet de setter des attributs pour l'envoie vers la page =>
//		// getPersonne;html avec
//		mod.addAttribute("nom", nom);
//		mod.addAttribute("prenom", prenom);
//		mod.addAttribute("email", email);
//		mod.addAttribute("age", age);
//		return "getPersonne";
//	}
}
