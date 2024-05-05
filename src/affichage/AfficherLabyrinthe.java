package affichage;
import algorithme.*;
import utils.Coordonnee;
import utils.Direction;
import jeu.Main;

import org.newdawn.slick.*;
import java.util.Set;

public class AfficherLabyrinthe {
	private Labyrinthe labyrinthe;
	private Coordonnee coordLabyrinthe;
	private Coordonnee coordCentreImage;
	private Direction deplacement;
	private int tempsDeplacement = 0;
	private final int tempsDeplacementMax;
	
	private final Sprite chat;
	private Image imageChat;
	
	private Set<Coordonnee> cheminSet;
	
	public AfficherLabyrinthe(Labyrinthe labyrinthe, int tempsDeplacementMax, Coordonnee coordLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.coordLabyrinthe = coordLabyrinthe;
        coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
        deplacement = Direction.NULLE;
        this.tempsDeplacementMax = tempsDeplacementMax;
        chat = new Sprite("CatSprits", 4, tempsDeplacementMax);
        imageChat = chat.getSprite(Direction.DROITE, 0);
    }
	
	public void setCheminSet(Set<Coordonnee> cheminSet) {
		this.cheminSet = cheminSet;
	}
	
	public Coordonnee getCoordChat() {
		return coordLabyrinthe;
	}
	
	public boolean estDeplacementValide(Direction d) {
		Coordonnee coord = coordLabyrinthe.addMod(d);
		return estBlanc(coord);
	}
	
	public boolean estDeplacementNul() {
		return deplacement.equals(Direction.NULLE);
	}
	
	private boolean estBlanc(Coordonnee coord) {
		return labyrinthe.estBlanc(coord) || (!labyrinthe.estValide(coord) && coord.getY() == labyrinthe.getDebut().getY());
	}
	
	public void updateLabyrinthe(int delta) {
		if (!deplacement.equals(Direction.NULLE)) {
			updateTempsDeplacement(delta);
			imageChat = chat.getSprite(deplacement, tempsDeplacement);
			if (tempsDeplacement >= tempsDeplacementMax) {
				tempsDeplacement = 0;
				coordLabyrinthe = coordLabyrinthe.addMod(deplacement);
				coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
				deplacement = Direction.NULLE;
			}
		}
	}
	
	public void faireDeplacement(Direction d) {
		imageChat = chat.getSprite(d, 0);
		if (estDeplacementValide(d)) {
			coordLabyrinthe = coordLabyrinthe.addMod(deplacement);
			coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
			deplacement = d;
		}
	}
	
	public void setCoordChat(Coordonnee coord) {
		coordLabyrinthe = coord;
		coordCentreImage = coord.mul(Main.TAILLECASE);
	}
	
	public void updateTempsDeplacement(int delta) {
		tempsDeplacement += delta;
		float f = (float) delta / tempsDeplacementMax;
		Direction d = deplacement.mul(Main.TAILLECASE * f);
		coordCentreImage = coordCentreImage.addMod(d);
	}
	
	public void afficherLabyrinthe(GameContainer gc, Graphics g) {
		float width = gc.getWidth();
	    float height = gc.getHeight();
		int x = (int) (coordLabyrinthe.getX() - (width / Main.TAILLECASE) / 2 - 1);
		int y = (int) (coordLabyrinthe.getY() - (height / Main.TAILLECASE) / 2 - 1);
		int largeur = gc.getWidth() / Main.TAILLECASE + 2;
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < largeur; j++) {
				Coordonnee coord = new Coordonnee(x + i, y + j);
				if (cheminSet.contains(coord)) {
					g.setColor(Color.green);
				} else if (estBlanc(coord)) {
					g.setColor(Color.white);
				} else {
					g.setColor(Color.black);
				}
				g.fillRect(coord.getX() * Main.TAILLECASE - coordCentreImage.getX() + width / 2,
						coord.getY() * Main.TAILLECASE - coordCentreImage.getY() + height / 2, Main.TAILLECASE,
						Main.TAILLECASE);
			}
		}
		imageChat.draw(width / 2, height / 2, Main.TAILLECASE, Main.TAILLECASE);
	}
	
	public void afficherChemin(Set<Coordonnee> cheminSet, GameContainer gc, Graphics g) {
		g.setColor(Color.green);
		for (Coordonnee coord : cheminSet) {
			g.fillRect(coord.getX() * Main.TAILLECASE - coordCentreImage.getX() + gc.getWidth() / 2,
					coord.getY() * Main.TAILLECASE - coordCentreImage.getY() + gc.getHeight() / 2, Main.TAILLECASE,
					Main.TAILLECASE);
		}
    }
}