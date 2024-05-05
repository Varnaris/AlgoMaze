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
	//private Minotaur minotaur;
	
	public Main(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		labyrinthe = new Labyrinthe(15);
		debut = labyrinthe.getDebut();
		fin = labyrinthe.getFin();
		Coordonnee d = new Coordonnee(debut.getX() - gc.getWidth() / (2*TAILLECASE), debut.getY());
		affichage = new AfficherLabyrinthe(labyrinthe,150, d);
		Set<Coordonnee> cheminSet = labyrinthe.trouverChemin(debut, fin).getCoordonnees();
		cheminSet = Utils.getRandomSubset(cheminSet, 0.5f);
		affichage.setCheminSet(cheminSet);
		//minotaur = new Minotaur(debut, labyrinthe.trouverChemin(debut, fin), 150);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		affichage.afficherLabyrinthe(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		lireDeplacement(gc);
		affichage.updateLabyrinthe(delta);
		updateBounds(gc, sbg);
		//minotaur.update(delta);
	}

	private void updateBounds(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if (affichage.getCoordChat().getX() >= labyrinthe.getLargeur() + gc.getWidth() / (2*TAILLECASE) + 1) {
			init(gc, sbg);
		} else if (affichage.getCoordChat().getX() < -gc.getWidth() / (2*TAILLECASE)) {
            affichage.setCoordChat(affichage.getCoordChat().addMod(Direction.DROITE));
		}
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
			appgc.setShowFPS(true);
			appgc.setTargetFrameRate(60);
			appgc.setTitle("AlgoMaze");
			Display.setResizable(true);
            appgc.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }
}
