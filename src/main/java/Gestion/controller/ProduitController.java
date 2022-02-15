package Gestion.controller;

import java.io.File;
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

import Gestion.dao.ProduitImp;
import Gestion.model.Produit;

@Controller
@RequestMapping("/Produit")
public class ProduitController {
	
	@Autowired
	ProduitImp prdImp;
	
	// variable static pour donner le dossier de l'url pour le stockage
	private static String prdUpload = "src/main/resources/static/prdUploads/";
	
	@RequestMapping("/ajoutPrd")
	public String toAddNewPrd() {
		return "forms/ajoutPrd";
	}
	
	@RequestMapping(value = "/AddPrd", method = RequestMethod.POST)
		public String toGetPrd(
				
				// recuperation des champs de formulaire 
				@RequestParam(name="ref", required = false)String ref,
				@RequestParam(name="designation", required = false) String designation,
				@RequestParam(name="prix", required = false) String prix, 
				@RequestParam(name="poids", required = false) String poids, 
				@RequestParam(name="qteProduit", required = false) String qteProduit,
				@RequestParam(name="photo", required = false) MultipartFile photo,
				RedirectAttributes rA)  throws IOException {
		
		// pour verifier si il y a une piece jointe chargée, on utilise isEmpty()
		if(photo.isEmpty()) {
			
			// afficher un msg selon les conditions dans une page web en Spring boot ==> ObjectRedirectAttribute.addFlashAttribute("nomAttr", "valeur de msg")
			rA.addFlashAttribute("msg", "Il faut ajouter une photo");
						
			return "redirect:/Produit/ajoutPrd";
			
			// instruction si pj chargée 
		} else {
			
			// verification si nom déja dans la dbb
			if(prdImp.resPrd(designation) != null) {
				rA.addFlashAttribute("msg", "Ce produit : "+ designation + " existe deja");
				return "redirect:/Produit/ajoutPrd";
			}
			
			// affichage de nom de pièce jointe (avec getOriginalFileName()
			System.out.println("L'image " + photo.getOriginalFilename() + " a été chargé");
			
			// stocker la photo dans static/uploads avec l'objet getByte()
			byte[] bytesPrd = photo.getBytes();
			
			// creation d'objet path pour stocker
			Path phPrd = Paths.get(prdUpload+photo.getOriginalFilename());
			
			// pour upload dans ph
			Files.write(phPrd, bytesPrd);
			
			// affichage du message pour le nom de l'upload
			rA.addFlashAttribute("Mess", "La photo uploadé est : "+ photo.getOriginalFilename());
			
			// Instance de la classe Produit
			Produit p = new Produit();
			
			// setter les valeurs des champs du form dans les attributs de l'entité (Produit)
			
			p.setRef(ref);
			p.setDesignation(designation);
			p.setPrix(Double.parseDouble(prix));
			p.setPoids(Double.parseDouble(poids));
			p.setQteProduit(Integer.parseInt(qteProduit));
			p.setPhoto(photo.getOriginalFilename());
			
			// Appelle de la methode d'implémentation ==> ici l'ajout 
			prdImp.addPrd(p);
			
			// verification
			System.out.println("Insertion du produit " + p.getDesignation());
		}
		
			
		return "redirect:/getPrd";
	}

}
