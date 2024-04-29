package algorithme;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class Chemin implements Iterator<Coordonnee>{
	private Chemin suivant;
	private Coordonnee actuel;
	
	/*
	public Chemin(SommetGraphe debut, SommetGraphe fin) {
		Chemin chemin2 = null;
	    Set<SommetGraphe> predesseceurs = debut.predesseceurs();
	    SommetGraphe jonction = null;
		for (SommetGraphe s : fin) {
			if (chemin2 != null) {
				chemin2 = new Chemin(chemin2, new Coordonnee(s.getCoordonnee(), chemin2.getCoordonnee()));
			}
			chemin2 = new Chemin(chemin2, s.getCoordonnee());
			if (predesseceurs.contains(s)) {
				jonction = s;
				break;
			}
		}
		
		Chemin chemin1 = this;
		actuel = debut.getCoordonnee();
		Iterator<SommetGraphe> it = debut.iterator();
		SommetGraphe s = it.next();
		while (it.hasNext()) {
			s = it.next();
			if (s.equals(jonction)) {
				break;
			}
		}
	}*/
	/*
	public Chemin(SommetGraphe debut, SommetGraphe fin) {
		actuel = debut.getCoordonnee();
		if (fin.predecesseurs().contains(debut)) {
			Chemin chemin = null;
			for (SommetGraphe s : fin) {
				if (chemin != null) {
					chemin = new Chemin(chemin, new Coordonnee(s.getCoordonnee(), chemin.getCoordonnee()));
				}
				if (s.equals(debut)) {
					break;
				}
				chemin = new Chemin(chemin, s.getCoordonnee());
				
			}
			suivant = chemin;
		} else {
			debut = debut.getPredecesseur();
			suivant = new Chemin(new Chemin(debut,fin), new Coordonnee(actuel, debut.getCoordonnee()));
		}
	}*/
	
	/*
	private Chemin(SommetGraphe debut, SommetGraphe fin, Chemin cSuivant) {
		if (debut.equals(fin)) {
            actuel = debut.getCoordonnee();
            suivant = cSuivant;
        } else if (debut.predecesseurs().contains(fin)) {
        	actuel = debut.getCoordonnee();
			debut = debut.getPredecesseur();
			suivant = new Chemin(new Chemin(debut,fin,cSuivant), new Coordonnee(actuel, debut.getCoordonnee()));
		} else {
			actuel = fin.getCoordonnee();
        	suivant = cSuivant;
        	fin = fin.getPredecesseur();
        	new Chemin(debut, fin, new Chemin(this, new Coordonnee(actuel, fin.getCoordonnee())));
		}
	}*/
	
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
	
	public Direction nextMod() {
		Coordonnee debut = actuel;
		Coordonnee fin = next();
		return new Direction(debut, fin);
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
		System.out.println(actuel + " " + c);
		System.out.println(suivant.getCoordonnee());
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
