package rs.antileaf.alice.utils;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import rs.antileaf.alice.cards.AliceMargatroid.WitchsTeaParty;
import rs.antileaf.alice.cards.Marisa.Alice6A;
import rs.antileaf.alice.cards.Marisa.AliceAsteroidBelt;
import rs.antileaf.alice.cards.Marisa.AliceDoubleSpark;
import rs.antileaf.alice.cards.Marisa.AliceSpark;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class AliceConfigHelper {
	public static String SHOULD_OPEN_TUTORIAL = "shouldOpenTutorial";
	public static String ENABLE_SPELL_CARD_SIGN_DISPLAY = "enableSpellCardSignDisplay";
	public static String ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE = "enableAlternativeMarisaCardImage";
	public static String ENABLE_WITCHS_TEA_PARTY_FEATURE = "enableWitchsTeaPartyFeature";
	
	public static SpireConfig conf = null;
	
	public static void loadConfig() {
		try {
			Properties defaults = new Properties();
			defaults.setProperty(SHOULD_OPEN_TUTORIAL, "true");
			defaults.setProperty(ENABLE_SPELL_CARD_SIGN_DISPLAY, "true");
			defaults.setProperty(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE, "true");
			defaults.setProperty(ENABLE_WITCHS_TEA_PARTY_FEATURE, "true");
			
			conf = new SpireConfig(AliceSpireKit.getModID(), "config", defaults);
		} catch (IOException e) {
			AliceSpireKit.log("AliceConfigHelper.initialize", "Failed to load config.");
		}
	}
	
	public static boolean shouldOpenTutorial() {
		return conf.getBool(SHOULD_OPEN_TUTORIAL);
	}
	
	public static void setShouldOpenTutorial(boolean shouldOpenTutorial) {
		conf.setBool(SHOULD_OPEN_TUTORIAL, shouldOpenTutorial);
	}
	
	public static boolean enableSpellCardSignDisplay() {
		return conf.getBool(ENABLE_SPELL_CARD_SIGN_DISPLAY);
	}
	
	public static void setEnableSpellCardSignDisplay(boolean enableSpellCardSignDisplay) {
		conf.setBool(ENABLE_SPELL_CARD_SIGN_DISPLAY, enableSpellCardSignDisplay);
	}
	
	public static boolean enableAlternativeMarisaCardImage() {
		return conf.getBool(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE);
	}
	
	public static void setEnableAlternativeMarisaCardImage(boolean enableAlternativeMarisaCardImage) {
		conf.setBool(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE, enableAlternativeMarisaCardImage);
		
		if (!AliceSpireKit.isMarisaModAvailable()) {
			for (String id : new String[]{Alice6A.ID, AliceAsteroidBelt.ID, AliceDoubleSpark.ID, AliceSpark.ID}) {
				AbstractCard card = CardLibrary.getCard(id);
				if (card instanceof CustomCard) {
					if (enableAlternativeMarisaCardImage)
						((CustomCard) card).textureImg = AliceSpireKit.getImgFilePath("Marisa/cards",
								card.getClass().getSimpleName() + "_original");
					else
						((CustomCard) card).textureImg = AliceSpireKit.getImgFilePath("Marisa/cards",
																card.getClass().getSimpleName());
					
					((CustomCard) card).loadCardImage(((CustomCard) card).textureImg);
				}
			}
		}
	}
	
	public static boolean enableWitchsTeaPartyFeature() {
		return conf.getBool(ENABLE_WITCHS_TEA_PARTY_FEATURE);
	}
	
	public static void setEnableWitchsTeaPartyFeature(boolean enableWitchsTeaPartyFeature) {
		conf.setBool(ENABLE_WITCHS_TEA_PARTY_FEATURE, enableWitchsTeaPartyFeature);
		
		WitchsTeaParty.updateAll();
	}
	
	public static void save() {
		try {
			conf.save();
		} catch (IOException e) {
			AliceSpireKit.log("AliceConfigHelper.save", "Failed to save config.");
		}
	}
	
	public static ModPanel createConfigPanel() {
		ModPanel panel = new ModPanel();
		
		Gson gson = new Gson();
		String json = Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("config"))
				.readString(String.valueOf(StandardCharsets.UTF_8));
		Map<String, String> config = gson.fromJson(json, (new TypeToken<Map<String, String>>() {}).getType());
		
		float y = 700.0F;
		
		ModLabeledToggleButton tutorialButton = new ModLabeledToggleButton(
				config.get(SHOULD_OPEN_TUTORIAL),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				shouldOpenTutorial(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setShouldOpenTutorial(button.enabled);
					save();
				}
		);
		
		y -= 50.0F;
		
		ModLabeledToggleButton spellCardButton = new ModLabeledToggleButton(
				config.get(ENABLE_SPELL_CARD_SIGN_DISPLAY),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableSpellCardSignDisplay(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableSpellCardSignDisplay(button.enabled);
					save();
				}
		);
		
		y -= 50.0F;

		ModLabeledToggleButton useAlternativeMarisaCardImageButton = new ModLabeledToggleButton(
				config.get(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableAlternativeMarisaCardImage(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableAlternativeMarisaCardImage(button.enabled);
					save();
				}
		);
		
		y -= 50.0F;
		
		ModLabeledToggleButton enableWitchsTeaPartyFeatureButton = new ModLabeledToggleButton(
				config.get(ENABLE_WITCHS_TEA_PARTY_FEATURE),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableWitchsTeaPartyFeature(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableWitchsTeaPartyFeature(button.enabled);
					save();
				}
		);
		
		panel.addUIElement(tutorialButton);
		panel.addUIElement(spellCardButton);
		panel.addUIElement(useAlternativeMarisaCardImageButton);
		panel.addUIElement(enableWitchsTeaPartyFeatureButton);
		
		return panel;
	}
}
