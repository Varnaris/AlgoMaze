package affichage;
import algorithme.*;
import utils.Coordonnee;
import utils.Direction;
import jeu.Main;
import org.newdawn.slick.*;
import java.util.Set;

public class AfficherLabyrinthe {
	private Labyrinthe labyrinthe;
	private Coordonnee coordLabyrinthe;
	private Coordonnee coordCentreImage;
	private Direction deplacement;
	private int tempsDeplacementMax;
	
	public AfficherLabyrinthe(Labyrinthe labyrinthe, int tempsDeplacementMax) {
        this.labyrinthe = labyrinthe;
        coordLabyrinthe = labyrinthe.getDebut();
        coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
        deplacement = Direction.NULLE;
        this.tempsDeplacementMax = tempsDeplacementMax;
    }
	
	public boolean estDeplacementValide(Direction d) {
		Coordonnee coord = coordLabyrinthe.addMod(d);
		return labyrinthe.estValide(coord) && !labyrinthe.estNoir(coord);
	}
	
	public void faireDeplacement(Direction d) {
		coordLabyrinthe = coordLabyrinthe.addMod(deplacement);
		coordCentreImage = coordLabyrinthe.mul(Main.TAILLECASE);
		deplacement = d;
	}
	
	public void updateTempsDeplacement(int delta) {
		float f = (float) delta / tempsDeplacementMax;
		Direction d = deplacement.mul(Main.TAILLECASE * f);
		coordCentreImage = coordCentreImage.addMod(d);
	}
	
	public void afficherLabyrinthe(GameContainer gc, Graphics g) {
		int x = coordLabyrinthe.getX() - (gc.getWidth() / Main.TAILLECASE) / 2 - 1;
		int y = coordLabyrinthe.getY() - (gc.getHeight() / Main.TAILLECASE) / 2 - 1;
		int largeur = gc.getWidth() / Main.TAILLECASE + 2;
		
		g.setColor(Color.white);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		for (int i = 0; i < largeur; i++) {
			for (int j = 0; j < largeur; j++) {
				Coordonnee coord = new Coordonnee(x + i, y + j);
				if (labyrinthe.estValide(coord)) {
					if (!labyrinthe.estNoir(coord)) {
						g.setColor(Color.white);
					} else {
						g.setColor(Color.black);
					}
					g.fillRect(coord.getX() * Main.TAILLECASE - coordCentreImage.getX() + gc.getWidth() / 2,
							coord.getY() * Main.TAILLECASE - coordCentreImage.getY() + gc.getHeight() / 2, Main.TAILLECASE,
							Main.TAILLECASE);
				}
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
	/*
	//main method
	public static void main(String[] args) {
		try {
			AppGameContainer appgc;
			Labyrinthe l = new Labyrinthe();
			Coordonnee debut = l.getDebut();
			Coordonnee fin = l.getFin();
			Chemin chemin = l.trouverChemin(debut, fin);
			Set<Coordonnee> cheminSet = chemin.getCoordonnees();
			appgc = new AppGameContainer(new StateBasedGame("Afficher Labyrinthe") {
				@Override
				public void initStatesList(GameContainer gc) throws SlickException {
					addState(new BasicGameState() {
						private AfficherLabyrinthe afficherLabyrinthe = new AfficherLabyrinthe(l);
						private Coordonnee centre = new Coordonnee(Labyrinthe.getLargeur() / 2,
								Labyrinthe.getLargeur() / 2);
						private Direction deplacement = Direction.NULLE;
						@Override
						public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
						}

						@Override
						public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
							g.setColor(Color.white);
							System.out.println("render");
							afficherLabyrinthe.afficherLabyrinthe(gc, g);
							afficherLabyrinthe.afficherChemin(cheminSet, gc, g);
							deplacement = deplacement.add(Direction.DROITE);
							System.out.println(deplacement);
							afficherLabyrinthe.faireDeplacement(deplacement);
							
						}

						@Override
						public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
							System.out.println("update");
						}

						@Override
						public int getID() {
							return 1;
						}
					});
				}
			});
			appgc.setDisplayMode(appgc.getScreenWidth(), appgc.getScreenHeight(), false);
			appgc.setShowFPS(false);
			appgc.setTargetFrameRate(60);
			appgc.setTitle("AlgoMaze");
			Display.setResizable(true);
			//appgc.setAlwaysRender(false);
			appgc.start();
		} catch (SlickException ex) {
			ex.printStackTrace();
		}
	}*/
}
