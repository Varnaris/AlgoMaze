package test;
import java.util.ArrayList;
import java.util.List;
import algorithme.*;
import utils.Coordonnee;

public class Test {
	public static void main(String[] args) {
		Labyrinthe l = new Labyrinthe(21);
		Coordonnee debut = l.getDebut();
		Coordonnee fin = l.getFin();
		Chemin chemin = l.trouverChemin(debut, fin);
		List<Coordonnee> cheminSet = new ArrayList<>();
		cheminSet.add(chemin.getCoordonnee());
		while (chemin.hasNext()) {
			cheminSet.add(chemin.next());
		}
		System.out.println(cheminSet);
		System.out.println(l.getLargeur());
		for (Iterable<Coordonnee> i : l) {
			for (Coordonnee c : i) {
				if (cheminSet.contains(c)) {
                    System.out.print("🔴");
                } else if (l.estBlanc(c)) {
					System.out.print("⬛");
				} else {
					System.out.print("⬜");
				}
			}
			System.out.println();
		}
	}
}
