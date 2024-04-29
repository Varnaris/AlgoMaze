package test;
import java.util.HashSet;
import java.util.Set;

import algorithme.*;
public class Test {
	public static void main(String[] args) {
		Labyrinthe l = new Labyrinthe();
		Coordonnee debut = new Coordonnee(1, 17);
		Coordonnee fin = new Coordonnee(33, 17);
		Chemin chemin = l.trouverChemin(debut, fin);
		Set<Coordonnee> cheminSet = new HashSet<>();
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
