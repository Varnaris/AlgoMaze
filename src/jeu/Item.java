package jeu;

import org.newdawn.slick.Image;

import affichage.AfficherLabyrinthe;
import utils.Coordonnee;

public abstract class Item {
	private String nom;
	private Coordonnee coordonnee;
	
	protected Item(String nom, Coordonnee coordonnee) {
		this.nom = nom;
		this.coordonnee = coordonnee;
	}
	
	public String getNom() {
		return nom;
	}

	public Coordonnee getCoordonnee() {
		return coordonnee;
	}
	
	public abstract Image getSprite();
	
	@Override
	public int hashCode() {
		return getCoordonnee().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Item) {
			Item i = (Item) o;
			return getCoordonnee().equals(i.getCoordonnee());
		}
		return false;
	}
	
	public abstract void effectuerEffet(AfficherLabyrinthe affichage);
	
	public abstract void update(int delta);
}
