package rs.antileaf.alice.utils;

import com.badlogic.gdx.graphics.Texture;

public class AliceImageMaster {
	public static Texture GOLD_END_TURN_BUTTON_GLOW;
	
	public static void loadImages() {
		GOLD_END_TURN_BUTTON_GLOW = new Texture(AliceSpireKit.getImgFilePath("UI", "goldEndTurnButtonGlow"));
	}
}
