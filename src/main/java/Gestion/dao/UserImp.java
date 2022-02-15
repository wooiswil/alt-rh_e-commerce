package Gestion.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Gestion.model.User;
import Gestion.repository.UserInterface;

// session_save()
@Service
// pour faire le commit() de la requete (équivalent .hibernatecfg.xml) 
@Transactional
public class UserImp  {
	
	// verifie la compatibilité entre le type des columns et des attributs
	@Autowired
	
	// Creation d'objet de type UserInterface pour faire appelle aux methode de l'interface
	UserInterface interfaceUser;
	
	// fonction pour l'ajout
	public void saveUser(User user) {

		// equivalent de la requete insert into / preparation de requete / execution de la requete
		interfaceUser.save(user);
	}
	
	// func pour retourner une liste
	public List<User> getUser() {
		
		// equivalent de select * from 
		List<User> listUser = (List<User>)  interfaceUser.findAll();
		
		return listUser;
	}
	
	
	// func de suppression
	public void rmUser(int idUser) {
	
		interfaceUser.deleteById(idUser);
	}
	
	
	// func de recherche
	public User searchUser(int idUser) {
		
		// get() retourne l'enregistrement
		User resSearch =   interfaceUser.findById(idUser).get();
		
		return resSearch;
		
	}
	
	
	// func d'auth pour email
	public User authUser(String email) {
		
		// recherche par email
		User resAuth =  interfaceUser.findByEmail(email);
		
		return resAuth;
	}
	

}
