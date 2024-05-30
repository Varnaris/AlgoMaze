package jeu;

import affichage.AfficherLabyrinthe;
import utils.Coordonnee;

public class Portail extends Item {
	
	public Portail(Coordonnee coordonnee) {
		super("Portail", coordonnee, "sprite/Items/portail.png");
	}

	@Override
	protected void effectuerEffet(AfficherLabyrinthe affichage) {
	    Coordonnee arrivee = affichage.getLabyrinthe().prendreFeuilleAleatoire();
		if (arrivee.equals(this.getCoordonnee())) {
			arrivee = affichage.getLabyrinthe().prendreFeuilleAleatoire();
	    }
		affichage.teleporterChat(arrivee);
	}
}
