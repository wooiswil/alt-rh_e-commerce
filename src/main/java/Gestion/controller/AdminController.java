package Gestion.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Gestion.dao.PanierImp;
import Gestion.dao.ProduitImp;
import Gestion.dao.UserImp;
import Gestion.model.Panier;
import Gestion.model.Produit;
import Gestion.model.User;

@Controller
public class AdminController {
	
	// verifie la compatibilité entre le type des columns et des attributs
		@Autowired
		UserImp userImp;
		
		@Autowired
		ProduitImp prdImp;
		
		@Autowired // ==> pour les class d'implémentation
		PanierImp cartImp;
	
		// login section
		
	// return page login administrateur
	@RequestMapping("/admin")
	public String toGetAdminPage(
			@RequestParam(name="email", required=false) String email,
			@RequestParam(name="mdp", required=false) String mdp,
			Model mod,
			HttpSession session) {
		
		// affichage du msgE
		if(session.getAttribute("msgE") != null) {
			mod.addAttribute("msg", session.getAttribute("msgE"));
		}
		
		// Verifie les champs de formulaire
		if(email != null && mdp != null) {
			
			// si les champs sont conformes aux conditions
			if(email.equals("admin@gmail.com") && mdp.equals("admin2022")) {
				
				// creation des attributs puis en enregistrement avec la session
				mod.addAttribute("email", email);
				mod.addAttribute("mdp", mdp);
				session.setAttribute("email", email);

				
				// redirection vers le homeAdmin "admin/dashboard" si les conditions sont remplies
				return "redirect:/homeAdmin";
			} else {
				
				// affichage d'un message d'erreur en cas d'identifiants invalides
				mod.addAttribute("msg", "Email/Password incorrect");
			}
		} else {
			// permet de vider le msgE (attribut de session) lors du  refresh ou clic dans la barre d'adresse "/admin"
			session.setAttribute("msgE", null);
		}
		
		// Affichage de la page login.html pour le traitement
		return "admin/login";
	}
	
	@RequestMapping("/homeAdmin")
	public String toAccessHomeAdmin(
			HttpSession s,
			Model m) {
		
		// en cas de déconnexion, l'user est redirigé vers la page de login sinon
		if(s.getAttribute("email") != null) {
			
			// permet d'avoir le nom de l'user connecté affiché dans le dashboard
			m.addAttribute("email", s.getAttribute("email"));
			return "admin/dashboard";
		} else {
			s.setAttribute("msgE", "Veuillez vous connecter !");
			// redirect pour changer l'url et le contenu de la page
			return "redirect:/admin";
		}
	}

	@RequestMapping("/logoutAdmin")
	public String toLogout(
			HttpSession s) {
		
		// cree un attibut "null" pour le champ email qui permet la deconnexion apres la récuperation de la valeur
		s.setAttribute("email", null);
		s.setAttribute("msgE", null);
		return "redirect:/admin";
	}
	
	// user section
	
	// ===> Modif
	@RequestMapping("/modUser")
	public String toModUser(
			@RequestParam(name = "nom", required = false) String nom,
			@RequestParam(name = "prenom", required = false) String prenom,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "mdp", required = false) String mdp,
			@RequestParam(name = "adresse", required = false) String adresse,
			@RequestParam(name = "tel", required = false) String tel,
			@RequestParam(name = "id", required = false) String id,
			Model m
			) {
		
		// Etape 1 ==> recherche de l'enregistrement par l'id
		// Instance de la classe User
		User usr =userImp.searchUser(Integer.parseInt(id));
		
		// etape 2 ==> setter les valeurs pour la modification
		// setter les valeurs des champs du form dans les attributs de l'entité (User)
		usr.setNom(nom);
		usr.setPrenom(prenom);
		usr.setEmail(email);
		usr.setMdp(mdp);
		usr.setAdresse(adresse);
		usr.setTel(tel);
		
		// etape 3 ==> appelle de la function de modif
		// fonction de modification (similaire à l'ajout) possible grace au traitement de la ligne 130
		userImp.saveUser(usr);
		
		m.addAttribute("user", usr);
		
		// "redirect;" change l'url et le contenu de la page web
		// return "login" garde l'url de la func actuelle et change seulement le contenu de la page
		return "redirect:/getUser";
	}
	
	// ====> Liste
	@RequestMapping("/getUser")
	public String toGetUser(Model m) {
		
		// pour l'affichage des utilisateurs
		List<User> listUser = (List<User>) userImp.getUser();
		
		// creation d'attributs pour l'affichage
		m.addAttribute("user", listUser);
		
		return "admin/affichageUser";
	}
	
	
	// ====> Supp
	// passage de paramètre "/{id} pour la suppression
	@RequestMapping("/deleteUser/{id}")
	// pour acceder au parametre spécifié dans l'url et son type 
	public String rmUser(@PathVariable("id") int idClient) {
		
		// appelle de la fonction de suppression 
		userImp.rmUser(idClient);
		
		return "redirect:/getUser";
	}
	
	// produit section
	
	// ====> Modif
	
	// ====> Liste
	@RequestMapping("/getPrd")
	public String getPrd(Model m) {
		
		// pour l'affichage des produits
		List<Produit> listPrd = (List<Produit>) prdImp.getPrd();
		
		// creation d'attributs pour l'affichage
		m.addAttribute("prd", listPrd);
		
		return "admin/affichageProduit";
	}
	
	// ====> Supp
	
	// panier section
}
