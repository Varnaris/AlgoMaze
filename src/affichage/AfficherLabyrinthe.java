package affichage;
import algorithme.*;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.util.Set;

public class AfficherLabyrinthe {
	private Labyrinthe labyrinthe;
	private final int TAILLE_CASE = 25;
	private Coordonnee coordLabyrinthe;
	private Coordonnee coordCentreImage;
	
	public AfficherLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        coordLabyrinthe = labyrinthe.getDebut();
        coordCentreImage = coordLabyrinthe.mul(TAILLE_CASE);
    }
	
    //write a prpogram that displays the maze with slick2d
	//there is only one public method in this class : "public void afficherLabyrinthe(Coordonnee centre, GameContainer gc, Graphics g)"
	
	//this method displays the maze with the center at the coordinates "centre"
	//the maze is displayed with the graphics object "g"
	//the game container "gc" is used to get the width and height of the window
	
	//the maze is displayed as follows:
	// - the center of the maze is at the coordinates "centre"
	// - the maze is displayed in a grid of squares of size "TAILLE_CASE"
	// - the squares that are part of the maze are displayed in white
	// - the squares that are not part of the maze are displayed in black
	
	public void afficherLabyrinthe(GameContainer gc, Graphics g) {
		int x = coordLabyrinthe.getX() - (gc.getWidth() / TAILLE_CASE) / 2;
		int y = coordLabyrinthe.getY() - (gc.getHeight() / TAILLE_CASE) / 2;
		for (int i = 0; i < gc.getWidth() / TAILLE_CASE; i++) {
			for (int j = 0; j < gc.getHeight() / TAILLE_CASE; j++) {
				Coordonnee c = new Coordonnee(x + i, y + j);
				if (labyrinthe.estValide(c) && labyrinthe.estNoir(c)) {
					g.setColor(Color.black);
				} else {
					g.setColor(Color.white);
				}
				g.fillRect(i * TAILLE_CASE, j * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE);
			}
		}
	}
	
	public void afficherChemin(Set<Coordonnee> cheminSet,Coordonnee centre, GameContainer gc, Graphics g) {
        for (Coordonnee c : cheminSet) {
            g.setColor(Color.green);
			g.fillRect((c.getX() - centre.getX() + gc.getWidth() / TAILLE_CASE / 2) * TAILLE_CASE,
					(c.getY() - centre.getY() + gc.getHeight() / TAILLE_CASE / 2) * TAILLE_CASE, TAILLE_CASE,
					TAILLE_CASE);
        }
        
    }
	
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
						private int modo = 1;
						@Override
						public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
						}

						@Override
						public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
							g.setColor(Color.white);
							System.out.println("render");
							afficherLabyrinthe.afficherLabyrinthe(gc, g);
							afficherLabyrinthe.afficherChemin(cheminSet,centre, gc, g);
							modo = (modo == 1 ? -1 : 1);
							centre = new Coordonnee(centre.getX() + modo, centre.getY());
							
						}

						@Override
						public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
							
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
			//appgc.setAlwaysRender(false);
			appgc.start();
		} catch (SlickException ex) {
			ex.printStackTrace();
		}
	}
}
