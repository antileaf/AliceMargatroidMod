package rs.antileaf.alice.strings;

import rs.antileaf.alice.utils.AliceSpireKit;

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
			AliceSpireKit.log("AliceCardModifierStrings.get", "No such string: " + id);
		
		return strings.get(id);
	}
}
