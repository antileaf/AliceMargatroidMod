package me.antileaf.alice.targeting;

import com.badlogic.gdx.graphics.Texture;
import me.antileaf.alice.utils.AliceHelper;

public class AliceTargetIcon {
	public static final AliceTargetIcon ALICE, DOLL, SLOT, ENEMY, NONE;
	public static final float WIDTH = 40.0F;
	public static final float BG_SCALE = 1F;

	public String id;
	public Texture img, bg;
	public float scaleModifier;

	public AliceTargetIcon(String id, String img, String bg, float scaleModifier) {
		this.id = id;
		this.img = new Texture(img);
		this.bg = new Texture(bg);
		this.scaleModifier = scaleModifier;
	}

	static {
		ALICE = new AliceTargetIcon(
				"Alice",
				AliceHelper.getImgFilePath("UI/targetIcons", "alice"),
				AliceHelper.getImgFilePath("UI/targetIcons", "alice_bg"),
				0.88F
		);
		DOLL = new AliceTargetIcon(
				"Doll",
				AliceHelper.getImgFilePath("UI/targetIcons", "doll"),
				AliceHelper.getImgFilePath("UI/targetIcons", "doll_bg"),
				0.82F
		);
		SLOT = new AliceTargetIcon(
				"Slot",
				AliceHelper.getImgFilePath("UI/targetIcons", "slot"),
				AliceHelper.getImgFilePath("UI/targetIcons", "slot_bg"),
				0.8F
		);
		ENEMY = new AliceTargetIcon(
				"Enemy",
				AliceHelper.getImgFilePath("UI/targetIcons", "enemy"),
				AliceHelper.getImgFilePath("UI/targetIcons", "enemy_bg"),
				0.8F
		);
		NONE = new AliceTargetIcon(
				"None",
				AliceHelper.getImgFilePath("UI/targetIcons", "none"),
				AliceHelper.getImgFilePath("UI/targetIcons", "none_bg"),
				0.8F
		);
	}
}
