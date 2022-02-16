package Gestion.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Gestion.dao.EmployeeImp;
import Gestion.dao.PanierImp;
import Gestion.dao.ProduitImp;
import Gestion.dao.UserImp;
import Gestion.model.Employee;
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
		
		@Autowired // ==> pour les class d'implémentation
		EmployeeImp empImp;
		
		// variable static pour donner le dossier de l'url pour le stockage
		private static String chUpload = "src/main/resources/static/uploads/";
	
		// variable static pour donner le dossier de l'url pour le stockage
		private static String prdUpload = "src/main/resources/static/prdUploads/";
		
		////////////////
		// login section
		////////////////
		
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
				session.setAttribute("role", "Super-admin");
				session.setAttribute("email", email);

				
				// redirection vers le homeAdmin "admin/dashboard" si les conditions sont remplies
				return "redirect:/homeAdmin";
			} else if (empImp.authEmailEmp(email) != null){
				if (empImp.authEmailEmp(email).getPassword().equals(mdp)) {
					
					// creation des attributs puis en enregistrement avec la session
					session.setAttribute("role", empImp.authEmailEmp(email).getRole());
					session.setAttribute("nom", empImp.authEmailEmp(email).getNom());
					System.out.println("test role " + empImp.authEmailEmp(email).getRole() );
					return "redirect:/homeAdmin";
				}
				// verif 
//				System.out.println("Les identifiants rentrés sont : "+ email + mdp);
//				
//				// creation des attributs puis en enregistrement avec la session
//				mod.addAttribute("email", email);
//				mod.addAttribute("mdp", mdp);
//				session.setAttribute("email", email);
				// redirection vers le homeAdmin "admin/" si les conditions sont remplies
			}
				else {
				
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
		if(s.getAttribute("role") != null) {
			
			// permet d'avoir le nom de l'user connecté affiché dans le dashboard
			m.addAttribute("email", s.getAttribute("email"));
			m.addAttribute("nom", s.getAttribute("nom"));
			m.addAttribute("role", s.getAttribute("role"));
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
	
	////////////////
	// user section
	////////////////
	
	// ====> Liste user
		@RequestMapping("/getUser")
		public String toGetUser(Model m, HttpSession s) {
			
			// pour l'affichage des utilisateurs
			List<User> listUser = (List<User>) userImp.getUser();
			
			// creation d'attributs pour l'affichage
			m.addAttribute("user", listUser);
			m.addAttribute("role", s.getAttribute("role"));
			System.out.println("Il y a " + listUser.size() + " users dans la base de données");
			return "admin/affichageUser";
		}
		
	// ===> mod user
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
	
	// ====> del user
	// passage de paramètre "/{id} pour la suppression
	@RequestMapping("/deleteUser/{id}")
	// pour acceder au parametre spécifié dans l'url et son type 
	public String rmUser(@PathVariable("id") int idClient) {
		
		// appelle de la fonction de suppression 
		userImp.rmUser(idClient);
		
		return "redirect:/getUser";
	}
	
	//////////////////
	// produit section
	//////////////////
	
	// ====> Liste prd
		@RequestMapping("/getPrd")
		public String getPrd(Model m, HttpSession s) {
			
			// pour l'affichage des produits
			List<Produit> listPrd = (List<Produit>) prdImp.getPrd();
			
			// creation d'attributs pour l'affichage
			m.addAttribute("produit", listPrd);
			m.addAttribute("role", s.getAttribute("role"));
			System.out.println("Il y a  "+listPrd.size() + " produits dans la base de données");
			return "admin/affichageProduit";
		}
	
	// ====> mod Prd
		@RequestMapping("/modPrd")
		public String toModPrd(
				
				// recuperation des champs de formulaire 
				@RequestParam(name="ref", required = false)String ref,
				@RequestParam(name="designation", required = false) String designation,
				@RequestParam(name="prix", required = false) String prix, 
				@RequestParam(name="poids", required = false) String poids, 
				@RequestParam(name="qteProduit", required = false) String qteProduit,
				@RequestParam(name="photo", required = false) MultipartFile photo,
				@RequestParam(name= "id", required = false) String id,
				RedirectAttributes rA)  throws IOException  {
			
			// Etape 1 ==> recherche de l'enregistrement par l'id
			Produit p = prdImp.searchPrd(Integer.parseInt(id));
			
			// pour verifier si il y a une piee jointe chargée, on utilise isEmpty()
			if(photo.isEmpty()) {
				
				// afficher un msg selon les conditions dans une page web en Spring boot ==> ObjectRedirectAttribute.addFlashAttribute("nomAttr", "valeur de msg")
				rA.addFlashAttribute("msg", "Vous avez oublié d'ajouter une photo");
				System.out.println("test");
				return "redirect:/getPrd";
			} else {
				
				// affichage du nom de pièce jointe (avec getOriginalFileName()
				System.out.println("Photo ajouté : " + photo.getOriginalFilename());
				
				// stocker la photo dans static/uploads avec l'objet getByte()
				byte[] bytesPrd = photo.getBytes();
							
				// creation d'objet path pour stocker
				Path phPrd = Paths.get(prdUpload+photo.getOriginalFilename());
							
				// pour upload dans ph
				Files.write(phPrd, bytesPrd);
							
				// affichage du message pour le nom de l'upload
				rA.addFlashAttribute("Mess", "La nouvelle photo uploadé est : "+ photo.getOriginalFilename());
				
				// etape 2 ==> setter les valeurs pour la modification
				// setter les valeurs des champs du form dans les attributs de l'entité (Employee)
				
				p.setRef(ref);
				p.setDesignation(designation);
				p.setPrix(Double.parseDouble(prix));
				p.setPoids(Double.parseDouble(poids));
				p.setQteProduit(Integer.parseInt(qteProduit));
				p.setPhoto(photo.getOriginalFilename());
				
				// appelle func de modification
				prdImp.modPrd(p);
				
				// verification 
				System.out.println(p.getId());
				
				
				return "redirect:/getPrd";
			}
		}
		
	// ====> del prd
	@RequestMapping("/deletePrd/{id}")
	public String rmPrd(@PathVariable("id") int idPrd) {
		
		//appelle func delete
		prdImp.rmUser(idPrd);
		
		return "redirect:/getPrd";
	}
	
	///////////////////////
	// employee section
	///////////////////////
	
	// ====> liste emp
	@RequestMapping("/getEmp")
	public String getEmp(Model m, HttpSession s) {
		
		// creation de la collection pour l'affichage des Emp
		List<Employee> listEmp = (List<Employee>) empImp.getEmpList();
		
		// creation d'attribut pour l'affichage
		m.addAttribute("emp", listEmp);
		m.addAttribute("role", s.getAttribute("role"));
		System.out.println("Il y a " + listEmp.size() + " enregistrements d'employees dans la base de données");
		return "admin/affichageEmployee";
	}

	// ====> mod emp
	@RequestMapping("/modEmp")
	public String toModEmp(
			
			// recuperation des champs de formulaire 
			@RequestParam(name="nom", required = false)String nom,
			@RequestParam(name="prenom", required = false) String prenom,
			@RequestParam(name= "email", required = false) String email,
			@RequestParam(name= "mdp", required = false) String mdp,
			@RequestParam(name= "role", required = false) String role,
			@RequestParam(name="photo", required = false) MultipartFile photo, 
			@RequestParam(name= "id", required = false) String id,
			RedirectAttributes rA
			) throws IOException {
		
		// Etape 1 ==> recherche de l'enregistrement par l'id
		Employee emp = empImp.searchEmp(Integer.parseInt(id));
		
		// pour verifier si il y a une piee jointe chargée, on utilise isEmpty()
		if (photo.isEmpty()) {
			
					// afficher un msg selon les conditions dans une page web en Spring boot ==> ObjectRedirectAttribute.addFlashAttribute("nomAttr", "valeur de msg")
					rA.addFlashAttribute("msg", "Vous avez oublié d'ajouter une photo");
					System.out.println("test");
					return "redirect:/getEmp";
			} else {
					
					// affichage du nom de pièce jointe (avec getOriginalFileName()
					System.out.println("Photo " + photo.getOriginalFilename());
					
//					// stocker la photo dans static/uploads avec l'objet getByte() || throws IOException 
					byte [] bytes = photo.getBytes();
			
//					// creation d'objet path pour stocker || chUpload ==> variable static (scope global)
					Path ph =Paths.get(chUpload+photo.getOriginalFilename());
					
//					// pour upload dans ph
					Files.write(ph, bytes);
					
					// affichage du message pour le nom de l'upload
					rA.addFlashAttribute("Mess", "Nouvelle photo : "+ photo.getOriginalFilename() +" uploadé pour l'employee " + nom);
					

					// etape 2 ==> setter les valeurs pour la modification
					// setter les valeurs des champs du form dans les attributs de l'entité (Employee)
					emp.setNom(nom);
					emp.setPrenom(prenom);
					emp.setEmail(email);
					emp.setPassword(mdp);
					emp.setRole(role);
					emp.setPhoto(photo.getOriginalFilename());
					
					empImp.modEmp(emp);
					System.out.println(emp.getId());
		return "redirect:/getEmp";
	}
	}
	
	// ====> del emp
		@RequestMapping("/deleteEmp/{id}")
		public String rmEmp(@PathVariable("id") int idEmp) {
			
			// appelle func delete
			empImp.rmEmp(idEmp);
			return "redirect:/getEmp";
		}
	
	////////////////
	// panier section
	////////////////
	
	// ====> liste cart
	@RequestMapping("/getCart")
	public String toGetCart(Model m, HttpSession s) {
		
		// pour l'affichage de la liste des paniers
		List<Panier> listCart = (List<Panier>) cartImp.getCartList();
		
		// creation d'attributs pour l'affichage
		m.addAttribute("carts", listCart);
		m.addAttribute("role", s.getAttribute("role"));
		return "admin/affichagePanier";
	}
	
	// ====> mod cart
	// ====> del cart
}
