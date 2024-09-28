package rs.antileaf.alice.strings;

import java.util.Map;

public class AliceLanguageStrings {
	public static String PERIOD; // 句号
	public static String PERIOD_WITH_SPACE;
	public static String COMMA; // 逗号
	public static String COMMA_WITH_SPACE;
	public static String CAESURA; // 顿号
	public static String CAESURA_WITH_SPACE;
	public static String COLON; // 冒号
	public static String COLON_WITH_SPACE;
	public static String SEMICOLON; // 分号
	public static String SEMICOLON_WITH_SPACE;
	public static String EXCLAMATION_MARK; // 感叹号
	public static String EXCLAMATION_MARK_WITH_SPACE;
	public static String QUESTION_MARK; // 问号
	public static String QUESTION_MARK_WITH_SPACE;
	public static String OR; // 或
	public static String AND; // 和
	
	public static void init(Map<String, String> strings) {
		PERIOD = strings.get("PERIOD");
		PERIOD_WITH_SPACE = strings.get("PERIOD_WITH_SPACE");
		COMMA = strings.get("COMMA");
		COMMA_WITH_SPACE = strings.get("COMMA_WITH_SPACE");
		CAESURA = strings.get("CAESURA");
		CAESURA_WITH_SPACE = strings.get("CAESURA_WITH_SPACE");
		COLON = strings.get("COLON");
		COLON_WITH_SPACE = strings.get("COLON_WITH_SPACE");
		SEMICOLON = strings.get("SEMICOLON");
		SEMICOLON_WITH_SPACE = strings.get("SEMICOLON_WITH_SPACE");
		EXCLAMATION_MARK = strings.get("EXCLAMATION_MARK");
		EXCLAMATION_MARK_WITH_SPACE = strings.get("EXCLAMATION_MARK_WITH_SPACE");
		QUESTION_MARK = strings.get("QUESTION_MARK");
		QUESTION_MARK_WITH_SPACE = strings.get("QUESTION_MARK_WITH_SPACE");
		OR = strings.get("OR");
		AND = strings.get("AND");
	}
}
