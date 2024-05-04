package algorithme;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import utils.Coordonnee;
import utils.Direction;


public class Chemin implements Iterator<Coordonnee>{
	private Chemin suivant;
	private Coordonnee actuel;
	
	public Chemin(SommetGraphe debut, SommetGraphe fin) {
		this(debut, fin.getPredecesseurs());
	}
	
	private Chemin(SommetGraphe debut, Set<SommetGraphe> fin) {
		actuel = debut.getCoordonnee();
		if (fin.contains(debut)) {
			suivant = getCheminInverse(debut, fin.iterator().next(), null);
		} else {
			debut = debut.getPredecesseur();
			suivant = new Chemin(new Chemin(debut, fin), new Coordonnee(actuel, debut.getCoordonnee()));
		}
	}
	
	private Chemin getCheminInverse(SommetGraphe debut, SommetGraphe fin, Chemin cSuivant) {
		if (debut.equals(fin)) {
			return cSuivant;
		}
		Chemin c = new Chemin(cSuivant, fin.getCoordonnee());
		fin = fin.getPredecesseur();
		c = new Chemin(c, new Coordonnee(fin.getCoordonnee(), c.getCoordonnee()));
		return getCheminInverse(debut,fin, c);
	}

	private Chemin(Chemin suivant, Coordonnee actuel) {
		this.suivant = suivant;
		this.actuel = actuel;
	}
	
	public Coordonnee getCoordonnee() {
		return this.actuel;
	}
	
	public Chemin getSuivant() {
		return suivant;
	}
	
	public int taille() {
		if (suivant == null) {
			return 1;
		}
		return 1 + suivant.taille();
	}
	
	public boolean estLigneDroite() {	
		return (suivant == null || suivant.getSuivant() == null) || (getMod().equals(suivant.getMod()) && suivant.estLigneDroite());
    }
	
	public Set<Coordonnee> getCoordonnees() {
		Set<Coordonnee> set = (suivant == null) ? new java.util.HashSet<>() : suivant.getCoordonnees();
		set.add(actuel);
		return set;
	}
	
	public boolean estSurChemin(Coordonnee c) {
		if (actuel.equals(c)) {
			return true;
		}
		if (suivant == null) {
			return false;
		}
		return suivant.estSurChemin(c);
	}
	
	@Override
	public boolean hasNext() {
		return suivant != null;
	}
	
	@Override
	public Coordonnee next() {
		if (suivant == null) {
			throw new NoSuchElementException("Il n'y a pas de coordonnee suivante");
		}
		actuel = suivant.getCoordonnee();
		suivant = suivant.getSuivant();
		return actuel;
	}
	
	public Direction getMod() {
		if (suivant == null) {
			return Direction.NULLE;
		}
		return new Direction(actuel, suivant.getCoordonnee());
	}
	
	public void corrigerFinChemin(Coordonnee c) {
		if (actuel.equals(c)) {
			suivant = null;
		}
		else if (suivant == null) {
			if (!actuel.estVoisin(c) && !actuel.estConnexe(c)) {
				throw new IllegalArgumentException("Les coordonnees ne sont ni connexes ni voisines");
			}
			if (actuel.estVoisin(c)) {
				suivant = new Chemin(null, c);
			} else {
				suivant = new Chemin(new Chemin(null, c), new Coordonnee(actuel, c));
			}
		} else {
			suivant.corrigerFinChemin(c);
		}
	}
	
	public void corrigerDebutChemin(Coordonnee c) {
		if (actuel.equals(c)) {
			return;
		}
		if (suivant.getCoordonnee().equals(c)) {
			actuel = c;
			suivant = suivant.getSuivant();
		} else {
			Chemin ch = new Chemin(suivant, actuel);
			actuel = c;
			suivant = ch;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Chemin) {
			Chemin c = (Chemin) o;
			return actuel.equals(c.getCoordonnee());
		}
		return false;
	}
}
