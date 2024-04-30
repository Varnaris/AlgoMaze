package test;
import java.util.ArrayList;
import java.util.List;
import algorithme.*;

public class Test {
	public static void main(String[] args) {
		Labyrinthe l = new Labyrinthe();
		Coordonnee debut = l.getdebut();
		Coordonnee fin = l.getFin();
		Chemin chemin = l.trouverChemin(debut, fin);
		List<Coordonnee> cheminSet = new ArrayList<>();
		cheminSet.add(chemin.getCoordonnee());
		while (chemin.hasNext()) {
			cheminSet.add(chemin.next());
		}
		System.out.println(cheminSet);
		System.out.println(Labyrinthe.getLargeur());
		for (Iterable<Coordonnee> i : l) {
			for (Coordonnee c : i) {
				if (cheminSet.contains(c)) {
                    System.out.print("🔴");
                } else if (l.estNoir(c)) {
					System.out.print("⬜");
				} else {
					System.out.print("⬛");
				}
			}
			System.out.println();
		}
	}
}
