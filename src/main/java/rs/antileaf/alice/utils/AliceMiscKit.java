package rs.antileaf.alice.utils;

import java.util.Arrays;

public abstract class AliceMiscKit {
	public static <T> boolean arrayContains(T[] array, T value) {
		return Arrays.asList(array).contains(value);
	}
	
	public static <T> T[] reversedArray(T[] array) {
		T[] res = Arrays.copyOf(array, array.length);
		for (int i = 0; i < res.length / 2; i++) {
			T tmp = res[i];
			res[i] = res[res.length - i - 1];
			res[res.length - i - 1] = tmp;
		}
		return res;
	}
	
	public static String join(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString().trim();
	}
}
