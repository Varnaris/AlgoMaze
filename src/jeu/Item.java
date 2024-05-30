package jeu;

import org.newdawn.slick.Image;

import utils.Coordonnee;

public abstract class Item {
	private String nom;
	private Coordonnee coordonnee;
	private Image sprite;
	
	protected Item(String nom, Coordonnee coordonnee, String path) {
		this.nom = nom;
		this.coordonnee = coordonnee;
		try {
			sprite = new Image(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getNom() {
		return nom;
	}

	public Coordonnee getCoordonnee() {
		return coordonnee;
	}
	
	public Image getSprite() {
		return sprite;
	}
	
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
}
