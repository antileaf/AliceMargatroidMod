package rs.antileaf.alice.utils;

import basemod.ModLabel;
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
	public static String ENABLE_DEBUGGING = "enableDebugging";
	public static String SKIN_SELECTION_UNLOCKED = "skinSelectionUnlocked";
	public static String SKIN_CHOSEN = "skinChosen";
	
	public static SpireConfig conf = null;
	public static Map<String, String> strings;
	public static ModLabel skinLabel;
	
	public static void loadConfig() {
		try {
			Properties defaults = new Properties();
			defaults.setProperty(SHOULD_OPEN_TUTORIAL, "true");
			defaults.setProperty(ENABLE_SPELL_CARD_SIGN_DISPLAY, "true");
			defaults.setProperty(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE, "true");
			defaults.setProperty(ENABLE_WITCHS_TEA_PARTY_FEATURE, "true");
			defaults.setProperty(ENABLE_DEBUGGING, "false");
			defaults.setProperty(SKIN_SELECTION_UNLOCKED, "false");
			defaults.setProperty(SKIN_CHOSEN, "ORIGINAL");
			
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
	
	public static boolean enableDebugging() {
		return conf.getBool(ENABLE_DEBUGGING);
	}
	
	public static void setEnableDebugging(boolean enableDebugging) {
		conf.setBool(ENABLE_DEBUGGING, enableDebugging);
	}
	
	public static boolean isAliceSkinSelectionUnlocked() {
		return conf.getBool(SKIN_SELECTION_UNLOCKED);
	}
	
	public static void setAliceSkinSelectionUnlocked(boolean unlocked) {
		conf.setBool(SKIN_SELECTION_UNLOCKED, unlocked);
	}
	
	public static String getAliceSkinChosen() {
		return conf.getString(SKIN_CHOSEN);
	}
	
	public static void setAliceSkinChosen(String skin) {
		conf.setString(SKIN_CHOSEN, skin);
		if (skinLabel != null)
			skinLabel.text = strings.get(SKIN_CHOSEN) + skin;
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
		strings = gson.fromJson(json, (new TypeToken<Map<String, String>>() {}).getType());
		
		float y = 700.0F;
		
		ModLabeledToggleButton tutorialButton = new ModLabeledToggleButton(
				strings.get(SHOULD_OPEN_TUTORIAL),
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
				strings.get(ENABLE_SPELL_CARD_SIGN_DISPLAY),
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
				strings.get(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE),
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
				strings.get(ENABLE_WITCHS_TEA_PARTY_FEATURE),
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
		
		y -= 50.0F;
		
		ModLabeledToggleButton enableDebuggingButton = new ModLabeledToggleButton(
				strings.get(ENABLE_DEBUGGING),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableDebugging(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableDebugging(button.enabled);
					save();
				}
		);
		
		panel.addUIElement(tutorialButton);
		panel.addUIElement(spellCardButton);
		panel.addUIElement(useAlternativeMarisaCardImageButton);
		panel.addUIElement(enableWitchsTeaPartyFeatureButton);
		panel.addUIElement(enableDebuggingButton);
		
		if (enableDebugging()) {
			y -= 50.0F;
			
			ModLabeledToggleButton enableSkinSelectionButton = new ModLabeledToggleButton(
					strings.get(SKIN_SELECTION_UNLOCKED),
					350.0F,
					y,
					Settings.CREAM_COLOR,
					FontHelper.charDescFont,
					isAliceSkinSelectionUnlocked(),
					panel,
					(modLabel) -> {
					},
					(button) -> {
						setAliceSkinSelectionUnlocked(button.enabled);
						save();
					}
			);
			
			y -= 50.0F;
			
			skinLabel = new ModLabel(
					strings.get(SKIN_CHOSEN) + getAliceSkinChosen(),
					350.0F,
					y,
					Settings.CREAM_COLOR,
					FontHelper.charDescFont,
					panel,
					(modLabel) -> {
					}
			);
			
			panel.addUIElement(enableSkinSelectionButton);
			panel.addUIElement(skinLabel);
		}
		
		return panel;
	}
}
