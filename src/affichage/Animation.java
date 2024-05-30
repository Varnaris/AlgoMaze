package affichage;

import org.newdawn.slick.Image;

import jeu.Main;
import utils.Coordonnee;

public class Animation {
	private Image[] images;
	private int nbSprite;
	private int cycle;
	
	public Animation(Coordonnee taille, Coordonnee debut,String path, int nbSprite, int cycle) {
        this.nbSprite = nbSprite;
        this.cycle = cycle;
        images = new Image[nbSprite];
        try {
            Image image = new Image(path);
            initSprites(image, taille, debut);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void initSprites(Image image, Coordonnee taille, Coordonnee debut) {
		int tailleCase = image.getWidth()/taille.getX();
		for (int i = 0; i < nbSprite; i++) {
			int x = ((debut.getX() + i)%taille.getX()) * tailleCase;
			int y = (debut.getY() + (debut.getX() + i)/taille.getX()) * tailleCase;
			
			images[i] = image.getSubImage(x, y, tailleCase, tailleCase);
			images[i] = images[i].getScaledCopy(Main.TAILLECASE, Main.TAILLECASE);
		}
	}
	
	public Image getSprite(int temps) {
		int n = (temps % cycle) * nbSprite / cycle;
		return images[n];
	}
}
