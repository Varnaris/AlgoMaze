package algorithme;

public class Direction extends Coordonnee {
	public static final Direction HAUT = new Direction(0, -1);
	public static final Direction BAS = new Direction(0, 1);
	public static final Direction GAUCHE = new Direction(-1, 0);
	public static final Direction DROITE = new Direction(1, 0);
	
	public static final Direction[] DIRECTIONS = {HAUT, BAS, GAUCHE, DROITE};
	
	private Direction(int x, int y) {
		super(x, y);
	}

	public Direction(Coordonnee debut, Coordonnee arrivee) {
		this(arrivee.getX() - debut.getX(), arrivee.getY() - debut.getY());
	}
	
	public Direction oppose() {
		return new Direction(-this.getX(), -this.getY());
	}
	
	public Direction mul(int n) {
		return new Direction(this.getX() * n, this.getY() * n);
	}
	
	public Direction[] dirTengeantes() {
		return new Direction[] { new Direction(this.getY(), -this.getX()), new Direction(-this.getY(), this.getX()) };
	}
}