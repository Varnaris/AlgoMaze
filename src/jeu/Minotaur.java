package jeu;

import utils.Coordonnee;
import utils.Direction;
import algorithme.Chemin;

import org.newdawn.slick.Image;

import affichage.Sprite;

public class Minotaur {
	private Coordonnee position;
	private Chemin route;
	private int tempsDeplacementMax;
	private int tempsDeplacement = 0;
	
	private final Sprite mino;
	private Image imageMino;
	
	public Coordonnee getPosition() {
		return position;
	}
	
	public Minotaur(Coordonnee position, Chemin route, int tempsDeplacementMax) {
		this.position = position;
		this.route = route;
		this.tempsDeplacementMax = tempsDeplacementMax;
		mino = new Sprite("Minotaur", 4, tempsDeplacementMax);
		imageMino = mino.getSprite(Direction.GAUCHE, 0);
	}
	
	public void update(int delta) {
		tempsDeplacement += delta;
		if (tempsDeplacement >= tempsDeplacementMax) {
			tempsDeplacement -= tempsDeplacementMax;
			nextPosition();
		}
	}
	
	private void nextPosition() {
		if (route.hasNext()) {
			imageMino = mino.getSprite(route.getMod(), tempsDeplacement);
			position = route.next();
		}
	}
	
	public void draw(float x, float y) {
		Coordonnee deplacementImage = route.getMod().mul((float) tempsDeplacement * Main.TAILLECASE / tempsDeplacementMax);
		imageMino.draw(x - 35 + deplacementImage.getX(), y - 20 + deplacementImage.getY(), Main.TAILLECASE + 50, Main.TAILLECASE + 50);
	}
		
	public void updateChemin(Coordonnee coordChat) {
		route.corrigerFinChemin(coordChat);
	}
}
