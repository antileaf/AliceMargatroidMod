package rs.antileaf.alice.targeting;

import com.badlogic.gdx.graphics.Texture;
import rs.antileaf.alice.utils.AliceSpireKit;

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
				AliceSpireKit.getImgFilePath("UI/targetIcons", "alice"),
				AliceSpireKit.getImgFilePath("UI/targetIcons", "alice_bg"),
				0.88F
		);
		DOLL = new AliceTargetIcon(
				"Doll",
				AliceSpireKit.getImgFilePath("UI/targetIcons", "doll"),
				AliceSpireKit.getImgFilePath("UI/targetIcons", "doll_bg"),
				0.82F
		);
		SLOT = new AliceTargetIcon(
				"Slot",
				AliceSpireKit.getImgFilePath("UI/targetIcons", "slot"),
				AliceSpireKit.getImgFilePath("UI/targetIcons", "slot_bg"),
				0.8F
		);
		ENEMY = new AliceTargetIcon(
				"Enemy",
				AliceSpireKit.getImgFilePath("UI/targetIcons", "enemy"),
				AliceSpireKit.getImgFilePath("UI/targetIcons", "enemy_bg"),
				0.8F
		);
		NONE = new AliceTargetIcon(
				"None",
				AliceSpireKit.getImgFilePath("UI/targetIcons", "none"),
				AliceSpireKit.getImgFilePath("UI/targetIcons", "none_bg"),
				0.8F
		);
	}
}
