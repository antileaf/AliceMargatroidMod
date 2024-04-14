package rs.antileaf.alice.utils;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class AliceConfigHelper {
	public static SpireConfig conf = null;
	
	public static void loadConfig() {
		try {
			Properties defaults = new Properties();
			defaults.setProperty("shouldOpenTutorial", "true");
			defaults.setProperty("useAlternativeMarisaCardImage", "true");
			
			conf = new SpireConfig(AliceSpireKit.getModID(), "config", defaults);
		} catch (IOException e) {
			AliceSpireKit.log("AliceConfigHelper.initialize", "Failed to load config.");
		}
	}
	
	public static boolean shouldOpenTutorial() {
		return conf.getBool("shouldOpenTutorial");
	}
	
	public static void setShouldOpenTutorial(boolean shouldOpenTutorial) {
		conf.setBool("shouldOpenTutorial", shouldOpenTutorial);
	}
	
	// TODO: Implement choosing between the two Marisa card images
	public static boolean useAlternativeMarisaCardImage() {
		return conf.getBool("useAlternativeMarisaCardImage");
	}
	
	public static void setUseAlternativeMarisaCardImage(boolean useAlternativeMarisaCardImage) {
		conf.setBool("useAlternativeMarisaCardImage", useAlternativeMarisaCardImage);
			
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
				config.get("shouldOpenTutorial"),
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
		
		ModLabeledToggleButton useAlternativeMarisaCardImageButton = new ModLabeledToggleButton(
				config.get("useAlternativeMarisaCardImage"),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				useAlternativeMarisaCardImage(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setUseAlternativeMarisaCardImage(button.enabled);
					save();
				}
		);
		
		panel.addUIElement(tutorialButton);
		
		return panel;
	}
}
