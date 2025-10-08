package me.antileaf.alice.utils;

import basemod.BaseMod;
import com.megacrit.cardcrawl.localization.Keyword;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AliceKeywordsHelper {
	private static final Logger logger = LogManager.getLogger(AliceKeywordsHelper.class);
	
	public static HashMap<String, String> descriptions;

	public static void addDollKeywords(ArrayList<Keyword> dollKeywords) {
		descriptions = new HashMap<>();

		for (Keyword key : dollKeywords) {
			String[] names = Arrays.stream(key.NAMES).skip(1).toArray(String[]::new);
			logger.info("Loading keyword : {}", names[0]);
			
			BaseMod.addKeyword("alicemargatroid", names[0], names,
					key.DESCRIPTION);

			descriptions.put(names[0], key.DESCRIPTION);

//					AbstractDoll.getFlavor(names[0]) + AliceMiscKit.getPeriodSymbol() +
//							AbstractDoll.getDescription(names[0]));
		}
	}
}
