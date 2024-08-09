package rs.antileaf.alice.strings;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AliceSkinStrings {
	public String NAME;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;
	
	static Map<String, AliceSkinStrings> strings = null;
	
	public static void init(Map<String, AliceSkinStrings> strings) {
		AliceSkinStrings.strings = strings;
	}
	
	@Nullable
	public static AliceSkinStrings get(String id) {
		if (!strings.containsKey(id))
			return new AliceSkinStrings() {{
				NAME = "???";
				DESCRIPTION = "???";
			}};
		return strings.get(id);
	}
}
