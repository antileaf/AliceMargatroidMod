package rs.antileaf.alice.utils;

import basemod.BaseMod;
import com.megacrit.cardcrawl.localization.Keyword;
import rs.antileaf.alice.AliceMargatroidMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AliceKeywordsHelper {
	public static HashMap<String, String> descriptions;

	public static void addDollKeywords(ArrayList<Keyword> dollKeywords) {
		descriptions = new HashMap<>();

		for (Keyword key : dollKeywords) {
			String[] names = Arrays.stream(key.NAMES).skip(1).toArray(String[]::new);
			AliceSpireKit.log("Loading keyword : " + names[0]);
			
			BaseMod.addKeyword(AliceMargatroidMod.SIMPLE_NAME.toLowerCase(), names[0], names,
					key.DESCRIPTION);

			descriptions.put(names[0], key.DESCRIPTION);

//					AbstractDoll.getFlavor(names[0]) + AliceMiscKit.getPeriodSymbol() +
//							AbstractDoll.getDescription(names[0]));
		}
	}
}
