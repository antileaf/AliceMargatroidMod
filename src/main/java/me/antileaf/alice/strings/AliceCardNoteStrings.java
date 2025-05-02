package me.antileaf.alice.strings;

import me.antileaf.alice.AliceMargatroidMod;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AliceCardNoteStrings {
	public static String DEFAULT_TITLE;
	
	public String TITLE = null;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;
	
	static Map<String, AliceCardNoteStrings> strings = null;
	
	public static void init(Map<String, AliceCardNoteStrings> strings) {
		AliceCardNoteStrings.strings = strings;
		DEFAULT_TITLE = strings.get("DEFAULT").TITLE + " [" + AliceMargatroidMod.SIMPLE_NAME.toLowerCase() + ":InfoIcon]";
		
		for (AliceCardNoteStrings note : strings.values()) {
			if (note.TITLE == null)
				note.TITLE = DEFAULT_TITLE;
		}
	}
	
	@Nullable
	public static AliceCardNoteStrings get(String id) {
		if (!strings.containsKey(id))
			return null;
		return strings.get(id);
	}
}
