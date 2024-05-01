package algorithme;

public class Direction extends Coordonnee {
	public static final Direction HAUT = new Direction(0, -1);
	public static final Direction BAS = new Direction(0, 1);
	public static final Direction GAUCHE = new Direction(-1, 0);
	public static final Direction DROITE = new Direction(1, 0);
	public static final Direction NULLE = new Direction(0, 0);
	
	public static final Direction[] DIRECTIONS = {HAUT, BAS, GAUCHE, DROITE};
	public static final Direction[] DIRECTIONS2 = {HAUT.mul(2), BAS.mul(2), GAUCHE.mul(2), DROITE.mul(2)};
	
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
	
	public Direction add(Direction c) {
		return new Direction(this.getX() + c.getX(), this.getY() + c.getY());
	}
	
	public Direction[] dirTengeantes() {
		return new Direction[] { new Direction(this.getY(), -this.getX()), new Direction(-this.getY(), this.getX()) };
	}
}
