package rs.antileaf.alice.strings;

import java.util.Map;

public class AliceTargetIconStrings {
	public static String TITLE = null;
	public static String DESCRIPTION;
	public static Map<String, String> DETAILS;

	public static void init(Map<String, String> strings) {
		TITLE = strings.getOrDefault("TITLE", null);
		DESCRIPTION = strings.get("DESCRIPTION");
		DETAILS = strings;
	}
}
