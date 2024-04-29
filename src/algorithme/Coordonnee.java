package algorithme;

public class Coordonnee {
	private int x;
	private int y;
	public Coordonnee(int x, int y) {
		this.x = x;
        this.y = y;
	}
	
	public Coordonnee(Coordonnee predecesseur, Coordonnee successeur) {
		if (!predecesseur.estConnexe(successeur)) {
			throw new IllegalArgumentException("Les coordonnees ne sont pas connexes");
		}
		this.x = (predecesseur.getX() + successeur.getX()) / 2;
		this.y = (predecesseur.getY() + successeur.getY()) / 2;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Coordonnee addMod(Direction c) {
        return new Coordonnee(this.x + c.getX(), this.y + c.getY());
    }
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Coordonnee) {
			Coordonnee c = (Coordonnee) o;
			return this.x == c.x && this.y == c.y;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.x * Labyrinthe.getLargeur() + this.y;
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
	
	public Coordonnee mul(int n) {
		return new Coordonnee(this.x * n, this.y * n);
	}
	
	public boolean estConnexe(Coordonnee c) {
		return (this.x == c.x && Math.abs(this.y - c.y) == 2) || (this.y == c.y && Math.abs(this.x - c.x) == 2);
	}
	
	public boolean estVoisin(Coordonnee c) {
		return (this.x == c.x && Math.abs(this.y - c.y) == 1) || (this.y == c.y && Math.abs(this.x - c.x) == 1);
	}
}
