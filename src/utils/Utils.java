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
}
