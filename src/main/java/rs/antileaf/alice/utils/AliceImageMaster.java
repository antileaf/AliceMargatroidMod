package rs.antileaf.alice.utils;

import com.badlogic.gdx.graphics.Texture;

public class AliceImageMaster {
	public static Texture GOLD_END_TURN_BUTTON_GLOW;
	public static Texture ALICE_ARROW;
	public static Texture[] POKERS;
	public static Texture DESSERT_ICON;
	
	public static void loadImages() {
		GOLD_END_TURN_BUTTON_GLOW = new Texture(AliceSpireKit.getImgFilePath("UI", "goldEndTurnButtonGlow"));
		ALICE_ARROW = new Texture(AliceSpireKit.getImgFilePath("UI", "AliceArrow"));
		POKERS = new Texture[] {
				new Texture(AliceSpireKit.getImgFilePath("vfx", "poker/spade")),
				new Texture(AliceSpireKit.getImgFilePath("vfx", "poker/heart")),
				new Texture(AliceSpireKit.getImgFilePath("vfx", "poker/diamond")),
				new Texture(AliceSpireKit.getImgFilePath("vfx", "poker/club")),
				new Texture(AliceSpireKit.getImgFilePath("vfx", "poker/joker_small")),
				new Texture(AliceSpireKit.getImgFilePath("vfx", "poker/joker_big"))
		};
		DESSERT_ICON = new Texture(AliceSpireKit.getImgFilePath("vfx", "Dessert"));
	}
}
