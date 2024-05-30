package jeu;

import utils.Coordonnee;
import utils.Direction;
import algorithme.Chemin;
import algorithme.Labyrinthe;

import org.newdawn.slick.Image;

import affichage.Sprite;

public class Minotaur {
	private Coordonnee position;
	private Chemin route;
	private int tempsDeplacementMax;
	private int tempsDeplacement = 0;
	
	private Labyrinthe labyrinthe;
	private boolean charge = false;
	private boolean stun = false;
	private int tmpStun = 0;
	private int tmpStunMax;
	private Coordonnee posChat;
	private Image imageMino;
	private final Sprite mino;


	public int getTempsDeplacementMax() {
		return tempsDeplacementMax;
	}

	public Sprite getMino() {
		return mino;
	}

	
	public Coordonnee getPosition() {
		return position;
	}
	
	public Minotaur(Labyrinthe labyrinthe, Coordonnee position, Chemin route, int tempsDeplacementMax) {
		this.labyrinthe = labyrinthe;
		this.position = position;
		this.route = route;
		this.tempsDeplacementMax = tempsDeplacementMax;
		tmpStunMax = tempsDeplacementMax*3;
		mino = new Sprite("Minotaur", 4, tempsDeplacementMax);
		imageMino = mino.getSprite(Direction.GAUCHE, 0);
		posChat = position;
	}
	
	public void update(int delta) {
		if (!stun) {
			tempsDeplacement += delta;
		} else {
			tmpStun += delta;
		}
		imageMino = mino.getSprite(route.getMod(), tempsDeplacement);
		if (charge) {
			if (tempsDeplacement >= tempsDeplacementMax) {
				tempsDeplacement -= tempsDeplacementMax;
				charger(route.getMod());
			}
		} else if (stun) {
			if (tmpStun >= tmpStunMax) {
				tmpStun = 0;
				stun = false;
			}
		} else {
			if (tempsDeplacement >= tempsDeplacementMax) {
				tempsDeplacement -= tempsDeplacementMax;
				nextPosition();
			}
		}
	}
	
	private void charger(Direction sens) {
		Coordonnee nestPos = position.addMod(sens);
		if (labyrinthe.estBlanc(nestPos)) {
			position = nestPos;
		} else {
			route = labyrinthe.trouverChemin(position, posChat);
			charge = false;
			tempsDeplacementMax = tempsDeplacementMax*3;
			stun = true;
		}
		
	}
	
	private void nextPosition() {
		if (route.hasNext()) {
			imageMino = mino.getSprite(route.getMod(), tempsDeplacement);
			position = route.next();
			if (route.estLigneDroite()) {
				charge = true;
				tempsDeplacementMax = tempsDeplacementMax/3;
			}
		}
	}
	
	public void draw(float x, float y) {
		Coordonnee deplacementImage = route.getMod().mul((float) tempsDeplacement * Main.TAILLECASE / tempsDeplacementMax);
		imageMino.draw(x - 17 + deplacementImage.getX(), y - 30 + deplacementImage.getY(), Main.TAILLECASE + 50, Main.TAILLECASE + 50);
	}
		
	public void updateChemin(Coordonnee coordChat) {
		try {
			route.corrigerFinChemin(coordChat);
		} catch (IllegalArgumentException e) {
			if (!route.hasNext()) {
				route = labyrinthe.trouverChemin(position, coordChat);
			}
		}
		posChat = coordChat;
	}
}
