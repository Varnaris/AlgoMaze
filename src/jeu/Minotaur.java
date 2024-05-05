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
	private final Sprite mino;
	private Image imageMino;
	private int time;
	
	public Minotaur(Coordonnee position, Chemin route, int tempsDeplacementMax) {
		this.position = position;
		this.route = route;
		mino = new Sprite("Minotaur", 4, tempsDeplacementMax);
		imageMino = mino.getSprite(Direction.GAUCHE, 0);
	}
	
	public void update(int delta) {
		time += delta;
		if (time >= 200) {
			time -= 200;
			nextPosition();
		}
	}
	
	private void nextPosition() {
		position = route.next();
	}
	
	public void draw(GameContainer gc) {
		imageMino.draw(gc.getWidth() / 2 - 20,gc.getHeight() / 2 - 40, Main.TAILLECASE + 40,Main.TAILLECASE + 40);
		// TODO modifier les coordonnees pour que le minotaur soit sur ses coordonnees
	}
	
}
