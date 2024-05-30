package jeu;

import org.newdawn.slick.Image;

import affichage.AfficherLabyrinthe;
import affichage.Animation;
import utils.Coordonnee;

public class Portail extends Item {
	Animation portail;
	int temps;
	
	public Portail(Coordonnee coordonnee) {
		super("Portail", coordonnee);
		portail = new Animation(new Coordonnee(5, 7), new Coordonnee(0, 0), "sprite/Items/portail.png", 32, 1000);
	}

	@Override
	public void effectuerEffet(AfficherLabyrinthe affichage) {
	    Coordonnee arrivee = affichage.getLabyrinthe().prendreFeuilleAleatoire();
	    
		if (getCoordonnee().equals(arrivee)) {
			arrivee = affichage.getLabyrinthe().prendreFeuilleAleatoire();
		}
		affichage.setCoordChat(arrivee);
	}

	@Override
	public Image getSprite() {
		return portail.getSprite(temps);
	}

	@Override
	public void update(int delta) {
		temps = (temps + delta) % 1000;
	}
}
