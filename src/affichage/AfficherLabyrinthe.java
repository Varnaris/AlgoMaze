package affichage;
import algorithme.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import java.util.Set;

public class AfficherLabyrinthe {
	private Labyrinthe labyrinthe;
	private final int TAILLE_CASE = 25;
	private Coordonnee coordLabyrinthe;
	private Coordonnee coordCentreImage;
	private Direction deplacement;
	
	public AfficherLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        coordLabyrinthe = labyrinthe.getDebut();
        coordCentreImage = coordLabyrinthe.mul(TAILLE_CASE);
        deplacement = Direction.NULLE;
    }
	
	public void setDeplacement(Direction d) {
		deplacement = d;
	}
	
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
				g.fillRect(i * TAILLE_CASE + deplacement.getX(), j * TAILLE_CASE + deplacement.getY(), TAILLE_CASE, TAILLE_CASE);
			}
		}
	}
	
	public void afficherChemin(Set<Coordonnee> cheminSet, GameContainer gc, Graphics g) {
        for (Coordonnee c : cheminSet) {
            g.setColor(Color.green);
			g.fillRect((c.getX() - coordLabyrinthe.getX() + gc.getWidth() / TAILLE_CASE / 2) * TAILLE_CASE,
					(c.getY() - coordLabyrinthe.getY() + gc.getHeight() / TAILLE_CASE / 2) * TAILLE_CASE, TAILLE_CASE,
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
							afficherLabyrinthe.setDeplacement(deplacement);
							
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
	}
}
