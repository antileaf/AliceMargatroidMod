package rs.antileaf.alice.utils;

import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import rs.antileaf.alice.cards.deprecated.DEPRECATEDWitchsTeaParty;
import rs.antileaf.alice.cards.marisa.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class AliceConfigHelper {
	public static final String SHOULD_OPEN_TUTORIAL = "shouldOpenTutorial";
	public static final String ENABLE_SPELL_CARD_SIGN_DISPLAY = "enableSpellCardSignDisplay";
	public static final String ENABLE_CARD_TARGET_ICONS = "enableCardTargetIcons";
	public static final String ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE = "enableAlternativeMarisaCardImage";
	public static final String USE_PACK_MASTER_STYLE_MARISA_CARDS = "usePackMasterStyleMarisaCards";
	public static final String ENABLE_WITCHS_TEA_PARTY_FEATURE = "enableWitchsTeaPartyFeature"; // Deprecated
	public static final String ENABLE_SHANGHAI_DOLL_EVENT_FOR_OTHER_CHARACTERS = "enableShanghaiDollEventForOtherCharacters";
	public static final String ENABLE_DEBUGGING = "enableDebugging";
//	public static final String SKIN_SELECTION_UNLOCKED = "skinSelectionUnlocked";
	public static final String SKIN_CHOSEN = "skinChosen";
	public static final String SIGNATURE_UNLOCKED = "signatureUnlocked_";
	public static final String SIGNATURE_ENABLED = "signatureEnabled_";
	public static final String SIGNATURE_CHECKED = "signatureChecked";
	public static final String SUNGLASSES_UNLOCKED = "sunglassesUnlocked";
	public static final String SUNGLASSES_ENABLED = "sunglassesEnabled";
	
	public static SpireConfig conf = null;
	public static Map<String, String> strings;
	public static ModLabel skinLabel;
	
	public static void loadConfig() {
		try {
			Properties defaults = new Properties();
			defaults.setProperty(SHOULD_OPEN_TUTORIAL, "true");
			defaults.setProperty(ENABLE_SPELL_CARD_SIGN_DISPLAY, "true");
			defaults.setProperty(ENABLE_CARD_TARGET_ICONS, "true");
			defaults.setProperty(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE, "true");
			defaults.setProperty(USE_PACK_MASTER_STYLE_MARISA_CARDS, "true");
			defaults.setProperty(ENABLE_WITCHS_TEA_PARTY_FEATURE, "true");
			defaults.setProperty(ENABLE_SHANGHAI_DOLL_EVENT_FOR_OTHER_CHARACTERS, "true");
			defaults.setProperty(ENABLE_DEBUGGING, "false");
//			defaults.setProperty(SKIN_SELECTION_UNLOCKED, "false");
			defaults.setProperty(SKIN_CHOSEN, "ORIGINAL");
			
			conf = new SpireConfig(AliceHelper.getModID(), "config", defaults);
		} catch (IOException e) {
			AliceHelper.log("AliceConfigHelper.initialize", "Failed to load config.");
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

	public static boolean enableCardTargetIcons() {
		return conf.getBool(ENABLE_CARD_TARGET_ICONS);
	}

	public static void setEnableCardTargetIcons(boolean enableCardTargetIcons) {
		conf.setBool(ENABLE_CARD_TARGET_ICONS, enableCardTargetIcons);
	}
	
	private static void updateAllMarisaCards() {
		for (String id : new String[]{Alice6A.ID, AliceAsteroidBelt.ID, AliceDoubleSpark.ID, AliceSpark.ID}) {
			AbstractCard card = CardLibrary.getCard(id);
			if (card instanceof AbstractAliceMarisaCard)
				((AbstractAliceMarisaCard) card).setImages(id);
			else
				AliceHelper.logger.info("AliceConfigHelper.updateAllMarisaCards: Card {} is not an instance of AbstractAliceMarisaCard.", id);
		}
	}
	
	public static boolean enableAlternativeMarisaCardImage() {
		return conf.getBool(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE);
	}
	
	public static void setEnableAlternativeMarisaCardImage(boolean enableAlternativeMarisaCardImage) {
		conf.setBool(ENABLE_ALTERNATIVE_MARISA_CARD_IMAGE, enableAlternativeMarisaCardImage);
		
		if (!AliceHelper.isMarisaModAvailable())
			updateAllMarisaCards();
	}
	
	public static boolean usePackMasterStyleMarisaCards() {
		return conf.getBool(USE_PACK_MASTER_STYLE_MARISA_CARDS);
	}
	
	public static void setUsePackMasterStyleMarisaCards(boolean usePackMasterStyleMarisaCards) {
		conf.setBool(USE_PACK_MASTER_STYLE_MARISA_CARDS, usePackMasterStyleMarisaCards);
		
		if (AliceHelper.isMarisaModAvailable())
			updateAllMarisaCards();
	}

	@Deprecated
	public static boolean enableWitchsTeaPartyFeature() {
		return conf.getBool(ENABLE_WITCHS_TEA_PARTY_FEATURE);
	}

	@Deprecated
	public static void setEnableWitchsTeaPartyFeature(boolean enableWitchsTeaPartyFeature) {
		conf.setBool(ENABLE_WITCHS_TEA_PARTY_FEATURE, enableWitchsTeaPartyFeature);
		
		DEPRECATEDWitchsTeaParty.updateAll();
	}

	public static boolean enableShanghaiDollEventForOtherCharacters() {
		return conf.getBool(ENABLE_SHANGHAI_DOLL_EVENT_FOR_OTHER_CHARACTERS);
	}

	public static void setEnableShanghaiDollEventForOtherCharacters(boolean enableShanghaiDollEventForOtherCharacters) {
		conf.setBool(ENABLE_SHANGHAI_DOLL_EVENT_FOR_OTHER_CHARACTERS, enableShanghaiDollEventForOtherCharacters);
	}
	
	public static boolean enableDebugging() {
		return conf.getBool(ENABLE_DEBUGGING);
	}
	
	public static void setEnableDebugging(boolean enableDebugging) {
		conf.setBool(ENABLE_DEBUGGING, enableDebugging);
	}

	@Deprecated
	public static boolean isAliceSkinSelectionUnlocked() {
		return true;
//		return conf.getBool(SKIN_SELECTION_UNLOCKED);
	}
	
//	public static void setAliceSkinSelectionUnlocked(boolean unlocked) {
//		conf.setBool(SKIN_SELECTION_UNLOCKED, unlocked);
//	}
	
	public static String getAliceSkinChosen() {
		return conf.getString(SKIN_CHOSEN);
	}
	
	public static void setAliceSkinChosen(String skin) {
		conf.setString(SKIN_CHOSEN, skin);
		if (skinLabel != null)
			skinLabel.text = strings.get(SKIN_CHOSEN) + skin;
	}

	public static boolean isOldVersionSignatureUnlocked(String id) {
		String key = SIGNATURE_UNLOCKED + id;
		return conf.has(key) && conf.getBool(key);
	}

//	public static void setSignatureUnlocked(String id, boolean unlocked) {
//		conf.setBool(SIGNATURE_UNLOCKED + id, unlocked);
//		save();
//	}

	public static boolean isSignatureEnabled(String id) {
		String key = SIGNATURE_ENABLED + id;
		return conf.has(key) && conf.getBool(key);
	}

	public static void setSignatureEnabled(String id, boolean enabled) {
		conf.setBool(SIGNATURE_ENABLED + id, enabled);
		save();
	}

	public static boolean hasSignatureChecked() {
		return conf.getBool(SIGNATURE_CHECKED);
	}

	public static void setSignatureChecked(boolean checked) {
		conf.setBool(SIGNATURE_CHECKED, checked);
		save();
	}

	public static boolean isSunglassesUnlocked() {
		return conf.getBool(SUNGLASSES_UNLOCKED);
	}

	public static void setSunglassesUnlocked(boolean unlocked) {
		conf.setBool(SUNGLASSES_UNLOCKED, unlocked);
	}

	public static boolean isSunglassesEnabled() {
		return conf.getBool(SUNGLASSES_ENABLED);
	}

	public static void setSunglassesEnabled(boolean enabled) {
		conf.setBool(SUNGLASSES_ENABLED, enabled);
	}
	
	public static void save() {
		try {
			conf.save();
		} catch (IOException e) {
			AliceHelper.log("AliceConfigHelper.save", "Failed to save config.");
		}
	}
	
	public static ModPanel createConfigPanel() {
		ModPanel panel = new ModPanel();
		
		Gson gson = new Gson();
		String json = Gdx.files.internal(AliceHelper.getLocalizationFilePath("config"))
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

		ModLabeledToggleButton enableCardTargetIconsButton = new ModLabeledToggleButton(
				strings.get(ENABLE_CARD_TARGET_ICONS),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableCardTargetIcons(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableCardTargetIcons(button.enabled);
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
		
		ModLabeledToggleButton usePackMasterStyleMarisaCardsButton = new ModLabeledToggleButton(
				strings.get(USE_PACK_MASTER_STYLE_MARISA_CARDS),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				usePackMasterStyleMarisaCards(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setUsePackMasterStyleMarisaCards(button.enabled);
					save();
				}
		);
		
		y -= 50.0F;
		
//		ModLabeledToggleButton enableWitchsTeaPartyFeatureButton = new ModLabeledToggleButton(
//				strings.get(ENABLE_WITCHS_TEA_PARTY_FEATURE),
//				350.0F,
//				y,
//				Settings.CREAM_COLOR,
//				FontHelper.charDescFont,
//				enableWitchsTeaPartyFeature(),
//				panel,
//				(modLabel) -> {},
//				(button) -> {
//					setEnableWitchsTeaPartyFeature(button.enabled);
//					save();
//				}
//		);
//
//		y -= 50.0F;

		ModLabeledToggleButton enableShanghaiDollEventForOtherCharactersButton = new ModLabeledToggleButton(
				strings.get(ENABLE_SHANGHAI_DOLL_EVENT_FOR_OTHER_CHARACTERS),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableShanghaiDollEventForOtherCharacters(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableShanghaiDollEventForOtherCharacters(button.enabled);
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
		panel.addUIElement(enableCardTargetIconsButton);
		panel.addUIElement(useAlternativeMarisaCardImageButton);
		panel.addUIElement(usePackMasterStyleMarisaCardsButton);
//		panel.addUIElement(enableWitchsTeaPartyFeatureButton);
		panel.addUIElement(enableShanghaiDollEventForOtherCharactersButton);
		panel.addUIElement(enableDebuggingButton);
		
		if (enableDebugging()) {
//			y -= 50.0F;
//
//			ModLabeledToggleButton enableSkinSelectionButton = new ModLabeledToggleButton(
//					strings.get(SKIN_SELECTION_UNLOCKED),
//					350.0F,
//					y,
//					Settings.CREAM_COLOR,
//					FontHelper.charDescFont,
//					isAliceSkinSelectionUnlocked(),
//					panel,
//					(modLabel) -> {
//					},
//					(button) -> {
//						setAliceSkinSelectionUnlocked(button.enabled);
//						save();
//					}
//			);
			
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
			
//			panel.addUIElement(enableSkinSelectionButton);
			panel.addUIElement(skinLabel);
		}
		
		return panel;
	}
}
