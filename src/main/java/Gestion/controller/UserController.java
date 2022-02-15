package Gestion.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Gestion.dao.UserImp;
import Gestion.model.User;

@Controller
public class UserController {

	// verifie la compatibilité entre le type des columns et des attributs
	@Autowired
	
	// Appelle de la classe d'implémentation pour les methodes
			UserImp userImp;
	
	@RequestMapping("/getFormUser")
	public String toGetForm(
			
			// recuperation des champs de formulaire 
			@RequestParam(name="nom", required = false)String nom,
			@RequestParam(name="prenom", required = false) String prenom,
			@RequestParam(name="email", required = false) String email, 
			@RequestParam(name="mdp", required = false) String mdp, 
			@RequestParam(name="adresse", required = false) String adresse,
			@RequestParam(name="tel", required = false) String tel, 
			@RequestParam(name="btnForm", required = false) String btn,
			Model mod, HttpSession session) { 
		
		// Instance de la classe User
		User users = new User();
		
		// verification de la valeur du btn
		if(btn != null) {
			
			// traitement et instructions pour l'affichage
			// inscription
			if(btn.equals("btnIns")) {
				
				// setter les valeurs des champs du form dans les attributs de l'entité (User)
				users.setNom(nom);
				users.setPrenom(prenom);
				users.setEmail(email);
				users.setMdp(mdp);
				users.setAdresse(adresse);
				users.setTel(tel);
				
				// Appelle de la methode d'implémentation ==> ici l'ajout 
				userImp.saveUser(users);
				
				// recuperation des informations pour les stockés dans la session
				session.setAttribute("users", users.getNom() + " " +users.getPrenom());
				
				// verification
				System.out.println("Bienvenu " + users.getNom());
				
				// redirection qui correspond à l'url de l'index.html
				return "redirect:/"; 
				
			} else if (btn.equals("btnCo")) {
				
				// creation d'attributs pour l'user connecté
				mod.addAttribute("email", email);
				mod.addAttribute("mdp", mdp);
				
				// recherche par email ==> du form
				User resSearch = userImp.authUser(email);
				
				// verif si email dans la bdd
				if (resSearch == null) {
					
					// instruction si email non dans la bdd
					System.out.println("Email incorrect");
					// affichage d'un message d'erreur en cas d'identifiants invalides
					session.setAttribute("msgE", "Email " + email + " incorrect");
					return "redirect:/"; 
					
					// instruction si email dans la bdd ==> verif du mdp
				} else {
					session.setAttribute("MailExist", email);
					// verif email de form == email bdd
					if(resSearch.getMdp().equals(mdp)) {
						
						// recuperation des informations pour les stockés dans la session avec l'objet crée ligne 74
						session.setAttribute("usr", resSearch.getNom() + " " +resSearch.getPrenom());
						session.setAttribute("usrID", resSearch.getId());
						
						System.out.println(email);
						session.setAttribute("msgE", null);
						return "redirect:/"; 
					} else {
						// instruction si mdp non dans la bdd
						session.setAttribute("msgE", "Password incorrect");
						System.out.println("Mdp incorrect");
						return "redirect:/"; 
					}
				}
			}
		}
		return "index";
		}
}
