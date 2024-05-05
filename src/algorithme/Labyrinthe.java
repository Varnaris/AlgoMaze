package algorithme;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import jeu.Main;
import utils.Coordonnee;
import utils.Direction;
import utils.Utils;

public class Labyrinthe implements Iterable<Iterable<Coordonnee>>{
    private float coeff1;
    private float coeff2;
    private float coeff3;
    
    private int taille;
    private int largeur;
    
    private Map<Coordonnee, SommetGraphe> graphe;
    private Set<Coordonnee> casesBlanchesLabyrinthe;
	private Set<Coordonnee> feuilles;
    
	public Labyrinthe(int taille, float... coeffs) {
		this.taille = taille;
		this.coeff1 = (coeffs.length > 0 ? coeffs[0] : 0.5f);
		this.coeff2 = (coeffs.length > 1 ? coeffs[1] : 0.5f);
		this.coeff3 = (coeffs.length > 2 ? coeffs[2] : 2.0f);
		this.largeur = taille * 2 + 1;
		graphe = new HashMap<>();
		casesBlanchesLabyrinthe = initSommetIsolees();
		Set<Coordonnee> sommetsIsoles = initSommetIsolees();
		Coordonnee debut = initDebutFin();
		
		SommetGraphe racine = new SommetGraphe(debut, null);
		ComposanteGraphe composante = new ComposanteGraphe(racine);
		sommetsIsoles.remove(debut);
		ajouterSommetGraphe(racine);
		while (!sommetsIsoles.isEmpty()) {
			SommetGraphe sommet = composante.etendreGraphe(sommetsIsoles, coeff1, coeff2);
			ajouterSommetGraphe(sommet);
			sommetsIsoles.remove(sommet.getCoordonnee());
			casesBlanchesLabyrinthe.add(new Coordonnee(sommet.getCoordonnee(), sommet.getPredecesseur().getCoordonnee()));
		}
		feuilles = composante.getFeuilles();
	}
	
	private Coordonnee initDebutFin() {
		casesBlanchesLabyrinthe.add(getDebut());
		casesBlanchesLabyrinthe.add(getFin());
		return new Coordonnee(Main.RANDOM.nextInt(taille)*2+1, Main.RANDOM.nextInt(taille)*2+1);
	}
	
	public float getCoeff(int i) {
		switch (i) {
		case 1:
			return coeff1;
		case 2:
			return coeff2;
		case 3:
			return coeff3;
		default:
			throw new IllegalArgumentException("Coefficient inconnu");
		}
	}
	
	public int getLargeur() {
		return largeur;
	}
	
	public Coordonnee getDebut() {
		return new Coordonnee(0, taille);
	}
	
	public Coordonnee getFin() {
		return new Coordonnee(largeur - 1, taille);
	}
	
	private void ajouterSommetGraphe(SommetGraphe sommet) {
		graphe.put(sommet.getCoordonnee(), sommet);
	}
	
	
	private Set<Coordonnee> initSommetIsolees() {
		Set<Coordonnee> sommets = new HashSet<>();
		for (Iterable<Coordonnee> ligne : this) {
			for (Coordonnee c : ligne) {
				if (c.getX() % 2 == 1 && c.getY() % 2 == 1) {
					sommets.add(c);
				}
			}
		}
		return sommets;
	}
	
	public boolean estValide(Coordonnee c) {
		return c.getX() >= 0 && c.getX() < largeur && c.getY() >= 0 && c.getY() < largeur;
	}
	
	public boolean estBlanc(Coordonnee c) {
		return casesBlanchesLabyrinthe.contains(c);
	}
	
	public Coordonnee prendreFeuilleAleatoire() {
		return Utils.getRandomFromSet(feuilles);
	}
	
	public Chemin trouverChemin(Coordonnee debut, Coordonnee fin) {
		SommetGraphe debutSommet = getSommetGrapheAjacent(debut);
		SommetGraphe finSommet = getSommetGrapheAjacent(fin);
		Chemin chemin = new Chemin(debutSommet, finSommet);
		chemin.corrigerFinChemin(fin);
		chemin.corrigerDebutChemin(debut);
		return chemin;
	}
	
	private SommetGraphe getSommetGrapheAjacent(Coordonnee c) {
		if (graphe.containsKey(c)) {
			return graphe.get(c);
		}
		for (Direction dir : Direction.DIRECTIONS) {
			Coordonnee c2 = c.addMod(dir);
			if (graphe.containsKey(c2)) {
				return graphe.get(c2);
			}
		}
		return null;
	}

	@Override
	public Iterator<Iterable<Coordonnee>> iterator() {
		return new Iterator<Iterable<Coordonnee>>() {
			private int ligne = 0;
			
			@Override
			public boolean hasNext() {
                return ligne < getLargeur();
            }
			
			@Override
			public Iterable<Coordonnee> next() {
				if (!hasNext()) {
					throw new NoSuchElementException("Aucune ligne suivante");
				}
				int l = ligne;
				ligne++;
				return new Iterable<Coordonnee>() {
					@Override
					public Iterator<Coordonnee> iterator() {
						return new Iterator<Coordonnee>() {
							private int colonne = 0;

							@Override
							public boolean hasNext() {
								return colonne < getLargeur();
							}

							@Override
							public Coordonnee next() {
								if (!hasNext()) {
									throw new NoSuchElementException("Aucune colonne suivante");
								}
								int c = colonne;
								colonne++;
								return new Coordonnee(c, l);
							}
						};
					}
				};
			}
		};
	}
	
	static class ComposanteGraphe {
		private SommetGraphe racine;
		private Set<SommetGraphe> feuilles;
		private List<SommetGraphe> sommetsEligibles;
		
		protected ComposanteGraphe(SommetGraphe racine) {
			this.racine = racine;
			feuilles = new TreeSet<>();
			sommetsEligibles = new ArrayList<>();
			sommetsEligibles.add(racine);
			feuilles.add(racine);
		}
		
		public SommetGraphe getRacine() {
			return racine;
		}
		
		protected Set<Coordonnee> getFeuilles() {
			Set<Coordonnee> feuilles = new HashSet<>();
            for (SommetGraphe s : this.feuilles) {
                feuilles.add(s.getCoordonnee());
            }
            return feuilles;
		}
		
		private SommetGraphe choisirSommetAleatoire(float coeff) {
			if (coeff == 1.0 || (coeff != 0.0 && Main.RANDOM.nextFloat() < coeff)) {
				return sommetsEligibles.get(sommetsEligibles.size() - 1);
			}
			return sommetsEligibles.get(Main.RANDOM.nextInt(sommetsEligibles.size()));
		}
		
		protected SommetGraphe etendreGraphe(Set<Coordonnee> sommetsIsoles, float coeff1, float coeff2) {
			if (sommetsIsoles.isEmpty()) {
				throw new IllegalArgumentException("Aucun sommet isolé disponible");
			}
			SommetGraphe candidatDepart = choisirSommetAleatoire(coeff1);
			SommetGraphe candidatArrivee = candidatDepart.choisirSuccesseur(sommetsIsoles, coeff2);
			while (candidatArrivee == null && !sommetsEligibles.isEmpty()) {
				sommetsEligibles.remove(candidatDepart);
				candidatDepart = choisirSommetAleatoire(coeff1);
				candidatArrivee = candidatDepart.choisirSuccesseur(sommetsIsoles, coeff2);
			}
			feuilles.remove(candidatDepart);
			if (candidatArrivee == null) {
				throw new IllegalArgumentException("Aucun sommet isolé disponible");
			}
			sommetsIsoles.remove(candidatArrivee.getCoordonnee());
			sommetsEligibles.add(candidatArrivee);
			feuilles.add(candidatArrivee);
			return candidatArrivee;
		}
		
		
	}
}
