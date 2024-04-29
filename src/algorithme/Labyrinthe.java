package algorithme;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;

public class Labyrinthe implements Iterable<Iterable<Coordonnee>>{
    public static final Random RANDOM = new Random();
    private static float COEFF1 = 0.9f;
    private static float COEFF2 = 0.5f;
    private static float COEFF3 = 0.1f;
    private static int TAILLE = 17;
    private static int LARGEUR = TAILLE * 2 + 1;
    
    private Map<Coordonnee, SommetGraphe> graphe;
    private Set<Coordonnee> labyrinthe;
	private Set<Coordonnee> feuilles;
    
	public Labyrinthe() {
		graphe = new HashMap<>();
		labyrinthe = initSommetIsolees();
		Set<Coordonnee> sommetsIsoles = initSommetIsolees();
		Coordonnee debut = initDebutFin();
		SommetGraphe racine = new SommetGraphe(debut, null);
		ComposanteGraphe composante = new ComposanteGraphe(racine);
		sommetsIsoles.remove(debut);
		ajouterSommetGraphe(racine);
		while (!sommetsIsoles.isEmpty()) {
			SommetGraphe sommet = composante.etendreGraphe(sommetsIsoles);
			ajouterSommetGraphe(sommet);
			sommetsIsoles.remove(sommet.getCoordonnee());
			labyrinthe.add(new Coordonnee(sommet.getCoordonnee(), sommet.getPredecesseur().getCoordonnee()));
		}
		feuilles = composante.getFeuilles();
	}
	
	private Coordonnee initDebutFin() {
		labyrinthe.add(new Coordonnee(0, TAILLE));
		labyrinthe.add(new Coordonnee(LARGEUR - 1, TAILLE));
		return new Coordonnee(1, TAILLE);
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
	public static float getCoeff1() {
		return COEFF1;
	}
	
	public static float getCoeff2() {
		return COEFF2;
	}
	
	public static float getCoeff3() {
		return COEFF3;
	}
	
	public static int getLargeur() {
		return LARGEUR;
	}
	
	public boolean estValide(Coordonnee c) {
		return c.getX() >= 0 && c.getX() < LARGEUR && c.getY() >= 0 && c.getY() < LARGEUR;
	}
	
	public boolean estNoir(Coordonnee c) {
		return !labyrinthe.contains(c);
	}
	
	public Coordonnee prendreFeuilleAleatoire() {
		Coordonnee out = new ArrayList<>(feuilles).get(RANDOM.nextInt(feuilles.size()));
		feuilles.remove(out);
		return out;
	}
	
	public Chemin trouverChemin(Coordonnee debut, Coordonnee fin) {
		return new Chemin(graphe.get(debut), graphe.get(fin));
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
		
		private SommetGraphe choisirSommetAleatoire() {
			if (COEFF1 == 1.0 || (COEFF1 != 0.0 && RANDOM.nextFloat() < COEFF1)) {
				return sommetsEligibles.get(sommetsEligibles.size() - 1);
			}
			return sommetsEligibles.get(RANDOM.nextInt(sommetsEligibles.size()));
		}
		
		protected SommetGraphe etendreGraphe(Set<Coordonnee> sommetsIsoles) {
			if (sommetsIsoles.isEmpty()) {
				throw new IllegalArgumentException("Aucun sommet isolé disponible");
			}
			SommetGraphe candidatDepart = choisirSommetAleatoire();
			SommetGraphe candidatArrivee = candidatDepart.choisirSuccesseur(sommetsIsoles);
			while (candidatArrivee == null && !sommetsEligibles.isEmpty()) {
				sommetsEligibles.remove(candidatDepart);
				candidatDepart = choisirSommetAleatoire();
				candidatArrivee = candidatDepart.choisirSuccesseur(sommetsIsoles);
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
