package jeu;
import algorithme.*;
import affichage.*;

import java.util.Set;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends BasicGameState {
	private Labyrinthe labyrinthe;
	private AfficherLabyrinthe affichage;
	private Coordonnee debut;
	private Coordonnee fin;
	private Set<Coordonnee> cheminSet;
	private Direction deplacement;

	public Main(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		labyrinthe = new Labyrinthe();
		debut = labyrinthe.getDebut();
		fin = labyrinthe.getFin();
		cheminSet = labyrinthe.trouverChemin(debut, fin).getCoordonnees();
		affichage = new AfficherLabyrinthe(labyrinthe);
		deplacement = Direction.NULLE;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		affichage.setDeplacement(deplacement);
		affichage.afficherLabyrinthe(gc, g);
		affichage.afficherChemin(cheminSet, gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		affichage.setDeplacement(deplacement.mul(delta / 10));
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_UP:
			deplacement = Direction.HAUT;
			break;
		case Input.KEY_DOWN:
			deplacement = Direction.BAS;
			break;
		case Input.KEY_LEFT:
			deplacement = Direction.GAUCHE;
			break;
		case Input.KEY_RIGHT:
			deplacement = Direction.DROITE;
			break;
		case Input.KEY_Z:
			deplacement = Direction.HAUT;
			break;
		case Input.KEY_S:
			deplacement = Direction.BAS;
			break;
		case Input.KEY_Q:
			deplacement = Direction.GAUCHE;
			break;
		case Input.KEY_D:
			deplacement = Direction.DROITE;
			break;
		default:
			deplacement = Direction.NULLE;
			break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		deplacement = Direction.NULLE;
	}

	@Override
    public int getID() {
        return 0;
    }

	public static void main(String[] args) {
        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new StateBasedGame("Main") {
                @Override
                public void initStatesList(GameContainer gc) throws SlickException {
                    addState(new Main(0));
                }
            });
            appgc.setDisplayMode(appgc.getScreenWidth(), appgc.getScreenHeight(), false);
			appgc.setShowFPS(false);
			appgc.setTargetFrameRate(60);
			appgc.setTitle("AlgoMaze");
			Display.setResizable(true);
            appgc.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }
}