package affichage;
import org.newdawn.slick.Image;

import utils.Direction;


public class SpriteMinotaur {
	private Image imagePrincipal;
	private int cycle;
	
	public SpriteMinotaur(int cycle) {
		try {
			imagePrincipal = new Image("sprite/Minotaur/minotaur.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.cycle = cycle;
	}
	
	public Image getSprite(Direction d, int tempsDeplacement, int mode) {
		int n = (tempsDeplacement % cycle) * 4 / cycle;
		int x = n * 128 + mode * 4 * 128;
		int y = 0;
		if (d.equals(Direction.HAUT)) {
			y = 384;
        } else if (d.equals(Direction.BAS)) {
        	y = 0;
        } else if (d.equals(Direction.GAUCHE)) {
        	y = 128;
        } else {
			y = 256;
		}
		return imagePrincipal.getSubImage(x, y, 128, 128);
	}
}
