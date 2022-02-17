package Gestion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Panier {
	
	// Attributs
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	int idClient, idProduit, qteProduit, commande;
	
	// Accesseurs
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdClient() {
		return idClient;
	}
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}
	public int getIdProduit() {
		return idProduit;
	}
	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
	}
	public int getQteProduit() {
		return qteProduit;
	}
	public void setQteProduit(int qteProduit) {
		this.qteProduit = qteProduit;
	}
	public int getCommande() {
		return commande;
	}
	public void setCommande(int commande) {
		this.commande = commande;
	}
	
	
	
	
	
}
