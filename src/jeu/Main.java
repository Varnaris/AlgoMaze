package jeu;
import algorithme.*;
import utils.*;
import affichage.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends BasicGameState {
	public static final int TAILLECASE = 80;
	public static final Random RANDOM = new Random();
	public static final int LARGEURMAX = 21;
	
	private Labyrinthe labyrinthe;
	private AfficherLabyrinthe affichage;
	private Coordonnee debut;
	private Coordonnee fin;
	private Image lumiere;
	private Minotaur minotaur;

	public Main(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		labyrinthe = new Labyrinthe(LARGEURMAX, 0.1f, 0.3f, 1.0f);
		debut = labyrinthe.getDebut();
		fin = labyrinthe.getFin();
		Coordonnee d = new Coordonnee(debut.getX() - gc.getWidth() / (2*TAILLECASE), debut.getY());
		minotaur = new Minotaur(labyrinthe, fin, labyrinthe.trouverChemin(fin, debut), 800);
		Set<Item> setItems = new HashSet<>();
		for (int i = 0; i < 60; i++) {
			setItems.add(new Portail(labyrinthe.prendreFeuilleAleatoire()));
		}
		
		affichage = new AfficherLabyrinthe(labyrinthe, minotaur, debut,150, d, setItems);
		Set<Coordonnee> cheminSet = filDArianne();
		affichage.setCheminSet(cheminSet);
		lumiere = new Image("sprite/grotte.png");
	}

	private Set<Coordonnee> filDArianne() {
		Set<Coordonnee> cheminSet = labyrinthe.trouverChemin(debut, fin).getCoordonnees();
		cheminSet = Utils.getRandomSubSet(cheminSet, 0.1f);
		return cheminSet;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		affichage.afficherLabyrinthe(gc, g);
		int scaleLight = 1;
		lumiere.draw(TAILLECASE / 2f - lumiere.getWidth() / 2f + gc.getWidth() / 2f - lumiere.getWidth() / (2*scaleLight),
				TAILLECASE / 2f - lumiere.getHeight() / 2f + gc.getHeight() / 2f - lumiere.getHeight() / (2*scaleLight),
				lumiere.getWidth() + lumiere.getWidth() / scaleLight , lumiere.getHeight() + lumiere.getHeight() / scaleLight ); 
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		lireDeplacement(gc);
		affichage.updateLabyrinthe(delta);
		updateFin(gc, sbg);
		updateBounds(gc, sbg);
	}
	
	private void updateFin(GameContainer gc, StateBasedGame sbg) throws SlickException {
		if (affichage.isPerdu()) {
			init(gc, sbg);
		}
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
			d = Direction.BAS;
		}
		if (input.isKeyDown(Input.KEY_Z) || input.isKeyDown(Input.KEY_UP)) {
			d = Direction.HAUT;
		}
		if (input.isKeyDown(Input.KEY_Q) || input.isKeyDown(Input.KEY_LEFT)) {
			d = Direction.GAUCHE;
		}
		if (input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT)) {
			d = Direction.DROITE;
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
