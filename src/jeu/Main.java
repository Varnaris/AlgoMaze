package jeu;
import algorithme.*;
import utils.*;
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
	
	private Labyrinthe labyrinthe;
	private AfficherLabyrinthe affichage;
	private Coordonnee debut;
	private Coordonnee fin;
	
	public Main(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		labyrinthe = new Labyrinthe(31);
		debut = labyrinthe.getDebut();
		fin = labyrinthe.getFin();
		affichage = new AfficherLabyrinthe(labyrinthe,175);
		Set<Coordonnee> cheminSet = labyrinthe.trouverChemin(debut, fin).getCoordonnees();
		cheminSet = Utils.getRandomSubset(cheminSet, 0.5f);
		affichage.setCheminSet(cheminSet);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		affichage.afficherLabyrinthe(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		lireDeplacement(gc);
		affichage.updateLabyrinthe(delta);
	}

	private void lireDeplacement(GameContainer gc) {
		Direction d = Direction.NULLE;
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN)) {
			d = d.add(Direction.BAS);
		}
		if (input.isKeyDown(Input.KEY_Z) || input.isKeyDown(Input.KEY_UP)) {
			d = d.add(Direction.HAUT);
		}
		if (input.isKeyDown(Input.KEY_Q) || input.isKeyDown(Input.KEY_LEFT)) {
			d = d.add(Direction.GAUCHE);
		}
		if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			d = d.add(Direction.DROITE);
		}
		if (affichage.estDeplacementNul() && !d.equals(Direction.NULLE)) {
			affichage.faireDeplacement(d);
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