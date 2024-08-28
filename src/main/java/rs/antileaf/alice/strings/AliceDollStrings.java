package rs.antileaf.alice.strings;

import java.util.Map;

public class AliceDollStrings {
	private static Map<String, AliceDollStrings> strings = null;
	
	public String NAME;
	public String TYPE;
	public String TAG;
	public String PASSIVE_NAME, ACT_NAME = null;
	public String PASSIVE_DESCRIPTION, ACT_DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;
	
	public static void init(Map<String, AliceDollStrings> strings) {
		AliceDollStrings.strings = strings;
	}
	
	public static AliceDollStrings get(String id) {
		if (!strings.containsKey(id))
			return new AliceDollStrings();
		return strings.get(id);
	}
}
