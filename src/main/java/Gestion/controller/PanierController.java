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

import Gestion.dao.PanierImp;
import Gestion.model.Panier;

@Controller
@RequestMapping("/Panier")
public class PanierController {
	
	@Autowired
	PanierImp cartImp;

	// variable static pour donner le dossier de l'url pour le stockage
	private static String cartUpload = "src/main/resources/static/cartUploads/";

	@RequestMapping(value = "/AddCart", method = RequestMethod.POST)
	public String toAddToCart(
			
					// recuperation des champs de formulaire 
						@RequestParam(name="idClient", required = false)String idClient,
						@RequestParam(name="idProduit", required = false) String idProduit,
						@RequestParam(name= "photoProduit", required = false) MultipartFile photoProduit,
						@RequestParam(name= "qteProduit", required = false) String qteProduit,
						RedirectAttributes rA
			) throws IOException {

		// affichage de nom de pièce jointe (avec getOriginalFileName()
		System.out.println("photoProduit " + photoProduit.getOriginalFilename());
		
		// stocker la photoProduit dans static/uploads avec l'objet getByte()
		byte [] bytes = photoProduit.getBytes();
		
		// creation d'objet path pour stocker
		Path ph =Paths.get(cartUpload+photoProduit.getOriginalFilename());
		
		// pour upload dans ph
		Files.write(ph, bytes);
		
		// affichage du message pour le nom de l'upload
//		rA.addFlashAttribute("Mess", "La photoProduit uploadé est : "+ photoProduit.getOriginalFilename());
		
		// Instance de la classe Panier
		Panier cart = new Panier();
		
		cart.setIdClient(Integer.parseInt(idClient));
		cart.setIdProduit(Integer.parseInt(idProduit));
		cart.setPhotoProduit(photoProduit.getOriginalFilename());
		cart.setQteProduit(Integer.parseInt(qteProduit));
		
		// func d'ajout
		cartImp.addToCart(cart);
		
	return "";
}
}
