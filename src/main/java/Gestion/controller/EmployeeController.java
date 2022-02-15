package Gestion.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Gestion.dao.EmployeeImp;
import Gestion.model.Employee;

@Controller
@RequestMapping("/Employee")
public class EmployeeController {

	@Autowired
	EmployeeImp empImp;
	
	// variable static pour donner le dossier de l'url pour le stockage
	private static String chUpload = "src/main/resources/static/uploads/";
	// fonction affichage page d'ajout
	
	@RequestMapping("/ajout")
	public String toAddEmp() {
		return "employee/ajout";
	}
	
	// recuperation info emp
	// pour proteger l'acces des url avec spring boot, il faut ajouter entre les parenthèes de @reqMap
	// pour specifier le type de method de form utilisé pour exécuter une fonction il faut ajouter la clé method= Req.meth.post si l'exécution de l'url d'une fonction 
	// qui a la valeur method = req.method.post dans le reqMap sera seulement avec un form qui a l'attr method="post"
	@RequestMapping(value = "/AddEmp", method = RequestMethod.POST)
	public String toGetEmpInfo(
			
			// recuperation des champs de formulaire 
						@RequestParam(name="nom", required = false)String nom,
						@RequestParam(name="prenom", required = false) String prenom,
						@RequestParam(name= "email", required = false) String email,
						@RequestParam(name= "mdp", required = false) String mdp,
						@RequestParam(name= "role", required = false) String role,
						@RequestParam(name="photo", required = false) MultipartFile photo, 
						RedirectAttributes rA
			) throws IOException {
		// pour verifier si il y a une piece jointe chargée, on utilise isEmpty()
		if (photo.isEmpty()) {
			// afficher un msg selon les conditions dans une page web en Spring boot ==> ObjectRedirectAttribute.addFlashAttribute("nomAttr", "valeur de msg")
			rA.addFlashAttribute("msg", "Il faut ajouter une photo");
			
			return "redirect:/Employee/ajout";
			// photo selectionné
		} else {
			if(empImp.authEmailEmp(email) != null) {
				rA.addFlashAttribute("msg", "L'email : "+ email + " exist deja");
				rA.addFlashAttribute("nom", nom);
				rA.addFlashAttribute("prenom", prenom);
				return "redirect:/Employee/ajout";
			}
				// affichage de nom de pièce jointe (avec getOriginalFileName()
				System.out.println("Photo " + photo.getOriginalFilename());
				
				// stocker la photo dans static/uploads avec l'objet getByte()
				byte [] bytes = photo.getBytes();
				
				// creation d'objet path pour stocker
				Path ph =Paths.get(chUpload+photo.getOriginalFilename());
				
				// pour upload dans ph
				Files.write(ph, bytes);
				
				// affichage du message pour le nom de l'upload
				rA.addFlashAttribute("Mess", "La photo uploadé est : "+ photo.getOriginalFilename());
				
				// Instance de la classe Employee
				Employee empl = new Employee();
				
////			// setter les valeurs des champs du form dans les attributs de l'entité (Employee)
				empl.setNom(nom);
				empl.setPrenom(prenom);
				empl.setEmail(email);
				empl.setPassword(mdp);
				empl.setRole(role);
				empl.setPhoto(photo.getOriginalFilename());
				
////			// Appelle de la methode d'implémentation ==> ici l'ajout 
				empImp.addEmp(empl);
				
				// verif email
				
			
			// verification 
		System.out.println("Ajout de l'employee : " +empl.getNom() + " dans la base de données."); 
		}
		return "redirect:/getEmp";
		}
}
