package jeu;

import utils.Coordonnee;
import utils.Direction;
import algorithme.Chemin;
import org.newdawn.slick.*;

import org.newdawn.slick.Image;

import affichage.Sprite;

public class Minotaur {
	private Coordonnee position;
	private Chemin route;
	public Coordonnee getPosition() {
		return position;
	}

	private final Sprite mino;
	private Image imageMino;
	private int time;
	
	public Minotaur(Coordonnee position, Chemin route, int tempsDeplacementMax) {
		this.position = position;
		this.route = route;
		mino = new Sprite("Minotaur", 4, tempsDeplacementMax);
		imageMino = mino.getSprite(Direction.GAUCHE, 0);
		System.out.println(position.getX() + " " + position.getY());
	}
	
	public void update(int delta) {
		time += delta;
		if (time >= 1000) {
			time -= 1000;
			nextPosition();
		}
	}
	
	private void nextPosition() {
		if (route.hasNext()) {
			position = route.next();
		}
	}
	
	public void draw(float x, float y) {
		imageMino.draw(x, y, Main.TAILLECASE, Main.TAILLECASE);
		// TODO modifier les coordonnees pour que le minotaur soit sur ses coordonnees
	}
	public void updateChemin(Coordonnee coordChat) {
		route.corrigerFinChemin(coordChat);
	}
}
