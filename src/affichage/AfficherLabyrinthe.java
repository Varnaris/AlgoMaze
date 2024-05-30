package affichage;
import algorithme.*;
import utils.Coordonnee;
import utils.Direction;
import jeu.Item;
import jeu.Main;
import jeu.Minotaur;

import org.newdawn.slick.*;
import java.util.Set;

public class AfficherLabyrinthe {
	private Labyrinthe labyrinthe;
	private Coordonnee coordLabyrinthe;
	private Coordonnee coordCentreImage;
	private Direction deplacement;
	private int tempsDeplacement = 0;
	private final int tempsDeplacementMax;
	
	private Coordonnee debut;
	private boolean estApparuMino = false;
	private boolean estEntreDansLabyrinthe = false;
	
	private final Sprite chat;
	private Image imageChat;
	
	private Minotaur minotaur;
	private boolean perdu = false;
	
	private Set<Coordonnee> cheminSet;
	private Set<Item> items;
	
	public AfficherLabyrinthe(Labyrinthe labyrinthe, Minotaur minotaur, Coordonnee debut, int tempsDeplacementMax, Coordonnee coordLabyrinthe, Set<Item> items) {
        this.labyrinthe = labyrinthe;
        this.coordLabyrinthe = coordLabyrinthe;
        this.items = items;
        coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
        deplacement = Direction.NULLE;
        this.tempsDeplacementMax = tempsDeplacementMax;
        chat = new Sprite("CatSprits", 4, tempsDeplacementMax);
        imageChat = chat.getSprite(Direction.DROITE, 0);
        this.minotaur = minotaur;
        this.debut = debut.addMod(Direction.DROITE);
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
		updateItems(delta);
		if (estApparuMino ) {
			minotaur.update(delta);
		}
		minotaur.update(delta);
		if (!deplacement.equals(Direction.NULLE)) {
			updateTempsDeplacement(delta);
			imageChat = chat.getSprite(deplacement, tempsDeplacement);
			if (tempsDeplacement >= tempsDeplacementMax) {
				tempsDeplacement -= tempsDeplacementMax;
				coordLabyrinthe = coordLabyrinthe.addMod(deplacement);
				coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
				deplacement = Direction.NULLE;
			}
		}
		perdu = coordLabyrinthe.equals(minotaur.getPosition()) && !minotaur.estStun();
	}
	
	public boolean isPerdu() {
		return perdu;
	}
	
	public void faireDeplacement(Direction d) {
		imageChat = chat.getSprite(d, 0);
		if (! estApparuMino) {
			if (!estEntreDansLabyrinthe) {
				if (debut.equals(coordLabyrinthe)) {
					estEntreDansLabyrinthe = true;
				}
			} else {
				if (Coordonnee.distance(debut, coordLabyrinthe) >= 10) {
					minotaur = new Minotaur(labyrinthe, debut, labyrinthe.trouverChemin(debut, coordLabyrinthe), minotaur.getTempsDeplacementMax());
					estApparuMino = true;
				}
			}
		}
		if (estDeplacementValide(d)) {
			coordLabyrinthe = coordLabyrinthe.addMod(deplacement);
			coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
			deplacement = d;
			if (labyrinthe.estValide(coordLabyrinthe)) {
				minotaur.updateChemin(coordLabyrinthe.addMod(d));
			}
		}
	}
	
	public void setCoordChat(Coordonnee coord) {
		coordLabyrinthe = coord;
		coordCentreImage = coord.mul(Main.TAILLECASE);
	}
	
	public void updateTempsDeplacement(int delta) {
		tempsDeplacement += delta;
		float f = (float) tempsDeplacement / tempsDeplacementMax;
		Direction d = deplacement.mul(Main.TAILLECASE * f);
		coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE).addMod(d);
	}
	
	private void updateItems(int delta) {
		Item itemToRemove = null;
		for (Item item : items) {
			item.update(delta);
			if (item.getCoordonnee().equals(coordLabyrinthe)) {
				itemToRemove = item;
				item.effectuerEffet(this);
			}
		}
		if (itemToRemove != null) {
			enleverItem(itemToRemove);
		}
	}
	
	public void afficherLabyrinthe(GameContainer gc, Graphics g) {
		float width = gc.getWidth();
	    float height = gc.getHeight();
		int x = (int) (coordLabyrinthe.getX() - (width / Main.TAILLECASE) / 2 - 2);
		int y = (int) (coordLabyrinthe.getY() - (height / Main.TAILLECASE) / 2 - 2);
		int largeur = gc.getWidth() / Main.TAILLECASE + 4;
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
		afficherItems(width, height);
		imageChat.draw(width / 2, height / 2, Main.TAILLECASE, Main.TAILLECASE);
		afficherMinotaur(width, height);
	}

	private void afficherMinotaur(float width, float height) {
		Coordonnee coord = minotaur.getPosition();
		if (coord.getX() >= coordLabyrinthe.getX() - (width / Main.TAILLECASE) / 2 - 2
				&& coord.getX() <= coordLabyrinthe.getX() + (width / Main.TAILLECASE) / 2 + 2
				&& coord.getY() >= coordLabyrinthe.getY() - (height / Main.TAILLECASE) / 2 - 2
				&& coord.getY() <= coordLabyrinthe.getY() + (height / Main.TAILLECASE) / 2 + 2) {
			
			minotaur.draw(coord.getX() * Main.TAILLECASE - coordCentreImage.getX() + width / 2,
				coord.getY() * Main.TAILLECASE - coordCentreImage.getY() + height / 2);
		}
	}
	
	private void afficherItems(float width, float height) {
		for (Item item : items) {
			Coordonnee coord = item.getCoordonnee();
			if (coord.getX() >= coordLabyrinthe.getX() - (width / Main.TAILLECASE) / 2 - 2
					&& coord.getX() <= coordLabyrinthe.getX() + (width / Main.TAILLECASE) / 2 + 2
					&& coord.getY() >= coordLabyrinthe.getY() - (height / Main.TAILLECASE) / 2 - 2
					&& coord.getY() <= coordLabyrinthe.getY() + (height / Main.TAILLECASE) / 2 + 2) {
				Image image = item.getSprite();
				image.draw(coord.getX() * Main.TAILLECASE - coordCentreImage.getX() + width / 2,
						coord.getY() * Main.TAILLECASE - coordCentreImage.getY() + height / 2);
			}
		}
	}
	
	public void afficherChemin(Set<Coordonnee> cheminSet, GameContainer gc, Graphics g) {
		g.setColor(Color.green);
		for (Coordonnee coord : cheminSet) {
			g.fillRect(coord.getX() * Main.TAILLECASE - coordCentreImage.getX() + gc.getWidth() / 2,
					coord.getY() * Main.TAILLECASE - coordCentreImage.getY() + gc.getHeight() / 2, Main.TAILLECASE,
					Main.TAILLECASE);
		}
    }
	
	public void enleverItem(Item item) {
		items.remove(item);
	}
	
	public Labyrinthe getLabyrinthe() {
		return labyrinthe;
	}
}