package jeu;
import algorithme.*;
import utils.Coordonnee;
import utils.Direction;
import affichage.*;

import java.util.Random;
import java.util.Set;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends BasicGameState {
	public static final int TAILLECASE = 60;
	public static final Random RANDOM = new Random();
	public static final int LARGEURMAX = 201;
	
	private Image cat = null; 
	private Labyrinthe labyrinthe;
	private AfficherLabyrinthe affichage;
	private Coordonnee debut;
	private Coordonnee fin;
	private Set<Coordonnee> cheminSet;
	private Direction deplacement;
	private boolean deplacementEnCours = false;
	private int tempsDeplacement = 0;
	public Main(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		labyrinthe = new Labyrinthe(31);
		debut = labyrinthe.getDebut();
		fin = labyrinthe.getFin();
		cheminSet = labyrinthe.trouverChemin(debut, fin).getCoordonnees();
		affichage = new AfficherLabyrinthe(labyrinthe,200);
		deplacement = Direction.NULLE;
		cat = new Image("sprite/CatSprits/CatSpritsDown.png");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		affichage.afficherLabyrinthe(gc, g);
		affichage.afficherChemin(cheminSet, gc, g);
		cat.draw(gc.getWidth()/2,gc.getHeight()/2,TAILLECASE,TAILLECASE);
		 
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (deplacementEnCours) {
			tempsDeplacement += delta;
			affichage.updateTempsDeplacement(tempsDeplacement);
			if (tempsDeplacement >= 200) {
				deplacementEnCours = false;
				tempsDeplacement = 0;
			}
		} else {
			if (!deplacement.equals(Direction.NULLE)) {
				deplacementEnCours = true;
				affichage.faireDeplacement(deplacement);
			}
		}
		deplacement = Direction.NULLE;
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
			deplacement = deplacement.add(Direction.BAS);
		}
		if (input.isKeyDown(Input.KEY_Z) || input.isKeyDown(Input.KEY_UP)) {
			deplacement = deplacement.add(Direction.HAUT);
		}
		if (input.isKeyDown(Input.KEY_Q) || input.isKeyDown(Input.KEY_LEFT)) {
			deplacement = deplacement.add(Direction.GAUCHE);
		}
		if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			deplacement = deplacement.add(Direction.DROITE);
		}
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