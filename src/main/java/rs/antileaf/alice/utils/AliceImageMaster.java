package rs.antileaf.alice.utils;

import com.badlogic.gdx.graphics.Texture;

public class AliceImageMaster {
	public static Texture TIP_TOP;
	public static Texture TIP_MID;
	public static Texture TIP_BOT;
	public static Texture SHANGHAI_DOLL_CHARGE;
	public static Texture GOLD_END_TURN_BUTTON_GLOW;
	public static Texture ALICE_ARROW;
	public static Texture[] POKERS;
	public static Texture DESSERT_ICON;
	
	public static void loadImages() {
		TIP_TOP = new Texture(AliceHelper.getImgFilePath("UI/tip", "tipTop"));
		TIP_MID = new Texture(AliceHelper.getImgFilePath("UI/tip", "tipMid"));
		TIP_BOT = new Texture(AliceHelper.getImgFilePath("UI/tip", "tipBot"));
		SHANGHAI_DOLL_CHARGE = new Texture(AliceHelper.getImgFilePath("orbs", "Sparkle"));
		GOLD_END_TURN_BUTTON_GLOW = new Texture(AliceHelper.getImgFilePath("UI", "goldEndTurnButtonGlow"));
		ALICE_ARROW = new Texture(AliceHelper.getImgFilePath("UI", "AliceArrow"));
		POKERS = new Texture[] {
				new Texture(AliceHelper.getImgFilePath("vfx", "poker/spade")),
				new Texture(AliceHelper.getImgFilePath("vfx", "poker/heart")),
				new Texture(AliceHelper.getImgFilePath("vfx", "poker/diamond")),
				new Texture(AliceHelper.getImgFilePath("vfx", "poker/club")),
				new Texture(AliceHelper.getImgFilePath("vfx", "poker/joker_small")),
				new Texture(AliceHelper.getImgFilePath("vfx", "poker/joker_big"))
		};
		DESSERT_ICON = new Texture(AliceHelper.getImgFilePath("vfx", "Dessert"));
	}
}
