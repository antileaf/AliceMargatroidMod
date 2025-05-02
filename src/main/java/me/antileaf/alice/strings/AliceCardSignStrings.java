package me.antileaf.alice.strings;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AliceCardSignStrings {
	public static String DEFAULT_TITLE;
	private static Map<String, AliceCardSignStrings> strings = null;
	
	public String SIGN;
	public String[] EXTENDED_DESCRIPTION = null;
	
	public static void init(Map<String, AliceCardSignStrings> strings) {
		AliceCardSignStrings.strings = strings;
	}
	
	@Nullable
	public static AliceCardSignStrings get(String id) {
		if (!strings.containsKey(id))
			return null;
		return strings.get(id);
	}
}
