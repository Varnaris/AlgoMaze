package affichage;
import algorithme.Direction;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Sprite {
	private String nom;
	private int nbSprite;
	private int cycle;
	
	private Image[] spritesHaut;
	private Image[] spritesBas;
	private Image[] spritesGauche;
	private Image[] spritesDroite;
	
	public Sprite(String nom, int nbSprite, int cycle) {
		this.nom = nom;
		this.nbSprite = nbSprite;
		this.cycle = cycle;
		spritesHaut = new Image[nbSprite];
		spritesBas = new Image[nbSprite];
		spritesGauche = new Image[nbSprite];
		spritesDroite = new Image[nbSprite];
		String chemin = "sprite/" + nom + "/";
		try {
			initSprites(chemin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initSprites(String chemin) throws SlickException {
		for (int i = 0; i < nbSprite; i++) {
			String nombre = (i == 0) ? "" : Integer.toString(i);
			spritesHaut[i] = new Image(chemin + nom + "Up" + nombre + ".png");
			spritesBas[i] = new Image(chemin + nom + "Down" + nombre + ".png");
			spritesGauche[i] = new Image(chemin + nom + "Left" + nombre + ".png");
			spritesDroite[i] = new Image(chemin + nom + "Right" + nombre + ".png");
		}
	}
	
	public Image getSprite(Direction d, int i) {
		int n = (i % cycle) * nbSprite / cycle;
		if (d.equals(Direction.HAUT)) {
			return spritesHaut[n];
        } else if (d.equals(Direction.BAS)) {
			return spritesBas[n];
		} else if (d.equals(Direction.GAUCHE)) {
			return spritesGauche[n];
		} else if (d.equals(Direction.DROITE)) {
			return spritesDroite[n];
		} else {
			return spritesBas[0];
		}
	}

}
