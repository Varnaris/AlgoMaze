package algorithme;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class SommetGraphe implements Comparable<SommetGraphe>, Iterable<SommetGraphe>{
	private Coordonnee coordonnee;
	private SommetGraphe predecesseur;

	public SommetGraphe(Coordonnee coordonnee, SommetGraphe predecesseur) {
		this.coordonnee = coordonnee;
		this.predecesseur = predecesseur;
	}
	
	public Coordonnee getCoordonnee() {
		return this.coordonnee;
	}
	
	public SommetGraphe getPredecesseur() {
		return this.predecesseur;
	}
	
	public int profondeur() {
		if (this.predecesseur == null) {
			return 0;
		}
		return 1 + this.predecesseur.profondeur();
	}
	
	@Override
	public int compareTo(SommetGraphe o) {
		return this.profondeur() - o.profondeur();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SommetGraphe) {
			SommetGraphe s = (SommetGraphe) o;
			return this.coordonnee.equals(s.coordonnee);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.coordonnee.hashCode();
	}
	
	@Override
	public Iterator<SommetGraphe> iterator() {
		return new Iterator<SommetGraphe>() {
			private SommetGraphe sommet = SommetGraphe.this;

			@Override
			public boolean hasNext() {
				return sommet != null;
			}

			@Override
			public SommetGraphe next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}
				SommetGraphe s = sommet;
				sommet = sommet.getPredecesseur();
				return s;
			}
		};
	}
	
	public Set<SommetGraphe> getPredecesseurs() {
		Set<SommetGraphe> predecesseurs = new LinkedHashSet<>();
		for (SommetGraphe s : this) {
			predecesseurs.add(s);
		}
	    return predecesseurs;
	}
	
	private SommetGraphe choisirSuccesseur(Set<Coordonnee> sommetsIsoles, Direction... d) {
		List<Coordonnee> out = new ArrayList<>();
		for (Direction dir : d) {
			Coordonnee c = this.coordonnee.addMod(dir);
			if (sommetsIsoles.contains(c)) {
				out.add(c);
			}
		}
		if (out.isEmpty()) {
			return null;
		}
		return new SommetGraphe(out.get(Labyrinthe.RANDOM.nextInt(out.size())), this);
	}
	
	public SommetGraphe choisirSuccesseur(Set<Coordonnee> sommetsIsoles) {
		if (predecesseur == null) {
			return choisirSuccesseur(sommetsIsoles, Direction.DROITE.mul(2), Direction.BAS.mul(2), 
					Direction.GAUCHE.mul(2), Direction.HAUT.mul(2));
		}
		Direction droit = new Direction(predecesseur.getCoordonnee(), coordonnee);
		Direction[] lateraux = droit.dirTengeantes();
		SommetGraphe out1 = choisirSuccesseur(sommetsIsoles, droit);
		SommetGraphe out2 = choisirSuccesseur(sommetsIsoles, lateraux);
		Float coeff2 = Labyrinthe.getCoeff2();
		if (coeff2 == 1.0 || (coeff2 != 0.0 && Labyrinthe.RANDOM.nextFloat() < coeff2)) {
            return out1 != null ? out1 : out2;
        }
        return out2 != null ? out2 : out1;
	}
}
