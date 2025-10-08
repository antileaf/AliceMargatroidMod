package me.antileaf.alice.strings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class AliceCardModifierStrings {
	private static final Logger logger = LogManager.getLogger(AliceCardModifierStrings.class);
	
	public String NAME;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;
	
	static Map<String, AliceCardModifierStrings> strings = null;
	
	public static void init(Map<String, AliceCardModifierStrings> strings) {
		AliceCardModifierStrings.strings = strings;
	}
	
	public static AliceCardModifierStrings get(String id) {
		if (!strings.containsKey(id))
			logger.warn("No such string: {}", id);
		
		return strings.get(id);
	}
}
