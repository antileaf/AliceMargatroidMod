package me.antileaf.alice.strings;

import me.antileaf.alice.utils.AliceHelper;

import java.util.Map;

public class AliceCardModifierStrings {
	public String NAME;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;
	
	static Map<String, AliceCardModifierStrings> strings = null;
	
	public static void init(Map<String, AliceCardModifierStrings> strings) {
		AliceCardModifierStrings.strings = strings;
	}
	
	public static AliceCardModifierStrings get(String id) {
		if (!strings.containsKey(id))
			AliceHelper.log("AliceCardModifierStrings.get", "No such string: " + id);
		
		return strings.get(id);
	}
}
