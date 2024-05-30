package utils;

import java.util.Iterator;
import java.util.Set;
import jeu.Main;

public class Utils {
	
	public static <T> T getRandomFromSet(Set<T> set) {
		int index = Main.RANDOM.nextInt(set.size());
		Iterator<T> it = set.iterator();
		for (int i = 0; i < index; i++) {
			it.next();
		}
		return it.next();
	}
	
	public static <T> Set<T> getRandomSubSet(Set<T> set, float coeff) {
		Set<T> subset = set;
		int nbElements = (int) (set.size() * ( 1 - coeff));
		for (int i = 0; i < nbElements; i++) {
			T element = getRandomFromSet(set);
			subset.add(element);
			set.remove(element);
		}
		set.addAll(subset);
		return subset;
	}
}
