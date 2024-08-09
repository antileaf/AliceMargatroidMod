package rs.antileaf.alice.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import rs.antileaf.alice.patches.enums.AliceMargatroidModClassEnum;
import rs.antileaf.alice.strings.AliceSkinStrings;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.HashMap;
import java.util.Map;

public class SkinSelectScreen {
	public static final String SAVABLE_KEY = "alice_margatroid_skin";
	
	public static boolean shouldUpdateBackground = false;
	public static Map<SkinEnum, Skin> skins;
	
	public static SkinSelectScreen inst;
	
	public Hitbox prevHb, nextHb;
	
	boolean unlocked = false;
	SkinEnum cur;
	
	Texture img;
	
	String title;
	String name, description;
	
	public SkinSelectScreen() {
		this.prevHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		this.nextHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
	}
	
	public void loadAnimation(String atlas, String skeleton, float scale) {
		// There is no animation now.
	}
	
	public void refresh() {
		AliceSkinStrings skinStrings = AliceSkinStrings.get(this.cur.name());
		if (skinStrings != null) {
			this.name = skinStrings.NAME;
			this.description = skinStrings.DESCRIPTION;
			this.img = ImageMaster.loadImage(skins.get(this.cur).img);
		}
		else {
			this.name = "???";
			this.description = "???";
			this.img = ImageMaster.loadImage(skins.get(SkinEnum.ORIGINAL).img);
		}
		
		shouldUpdateBackground = true;
		
		AliceConfigHelper.setAliceSkinChosen(this.cur.name());
		AliceConfigHelper.save();
		
//		findAliceAndSetSkin();
	}
	
	public Skin getSkin() {
		return skins.get(this.cur);
	}
	
	public String getPortrait() {
		AliceSpireKit.log("Skin: " + this.cur);
		return skins.get(this.cur).background;
	}
	
	private void updateInput() {
		if (CardCrawlGame.chosenCharacter == AliceMargatroidModClassEnum.ALICE_MARGATROID) {
			this.prevHb.update();
			this.nextHb.update();
			
			SkinEnum prev = SkinEnum.prev(this.cur), next = SkinEnum.next(this.cur);
			
			if (prev != null && this.prevHb.clicked) {
				this.prevHb.clicked = false;
				CardCrawlGame.sound.play("UI_CLICK_1");
				this.cur = prev;
				this.refresh();
			}
			
			if (next != null && this.nextHb.clicked) {
				this.nextHb.clicked = false;
				CardCrawlGame.sound.play("UI_CLICK_1");
				this.cur = next;
				this.refresh();
			}
			
			if (InputHelper.justClickedLeft) {
				if (this.prevHb.hovered)
					this.prevHb.clickStarted = true;
				if (this.nextHb.hovered)
					this.nextHb.clickStarted = true;
			}
		}
	}
	
	public void update() {
//		if (!AliceConfigHelper.isAliceSkinSelectionUnlocked())
//			return;
		
		float centerX = Settings.WIDTH * 0.86F;
		float centerY = Settings.HEIGHT * 0.55F;
		
		if (SkinEnum.prev(this.cur) != null)
			this.prevHb.move(centerX - 140.0F * Settings.scale, centerY);
		if (SkinEnum.next(this.cur) != null)
			this.nextHb.move(centerX + 140.0F * Settings.scale, centerY);
		
		this.updateInput();
	}
	
	public void render(SpriteBatch sb) {
//		if (!AliceConfigHelper.isAliceSkinSelectionUnlocked())
//			return;
		
		float centerX = Settings.WIDTH * 0.86F;
		float centerY = Settings.HEIGHT * 0.55F;
		
//		Color color = Color.BLACK.cpy();
//		color.a = 0.3F;
		
		FontHelper.renderFontCentered(sb,
				FontHelper.cardTitleFont,
				this.title,
				centerX,
				centerY + 150.0F * Settings.scale,
				Color.WHITE,
				1.25F
		);
		
		sb.draw(this.img, centerX - this.img.getWidth() / 2.0F, centerY - this.img.getHeight() / 2.0F);
		
		FontHelper.renderFontCentered(sb,
				FontHelper.cardTitleFont,
				this.name,
				centerX,
				centerY - 150.0F * Settings.scale,
				Color.WHITE,
				1.25F
		);
		
		FontHelper.renderFontCentered(sb,
				FontHelper.cardTitleFont,
				this.description,
				centerX,
				centerY - 205.0F * Settings.scale,
				Color.WHITE,
				0.85F
		);
		
		if (SkinEnum.prev(this.cur) != null) {
			if (this.prevHb.hovered)
				sb.setColor(Color.LIGHT_GRAY);
			else
				sb.setColor(Color.WHITE);
			
			sb.draw(ImageMaster.CF_LEFT_ARROW, this.prevHb.cX - 24.0F, this.prevHb.cY - 24.0F,
					24.0F, 24.0F,
					48.0F, 48.0F,
					Settings.scale, Settings.scale,
					0.0F, 0, 0, 48, 48, false, false);
		}
		
		if (SkinEnum.next(this.cur) != null) {
			if (this.nextHb.hovered)
				sb.setColor(Color.LIGHT_GRAY);
			else
				sb.setColor(Color.WHITE);
			
			sb.draw(ImageMaster.CF_RIGHT_ARROW, this.nextHb.cX - 24.0F, this.nextHb.cY - 24.0F,
					24.0F, 24.0F,
					48.0F, 48.0F,
					Settings.scale, Settings.scale,
					0.0F, 0, 0, 48, 48, false, false);
		}
	}
	
//	private static void findAliceAndSetSkin() {
//		for (AbstractPlayer p : CardCrawlGame.characterManager.getAllCharacters()) {
//			if (p instanceof AliceMargatroid) {
//				AliceMargatroid alice = (AliceMargatroid) p;
//				alice.setSkin(inst.getSkin());
//				break;
//			}
//		}
//	}
	
	public static void init() {
		inst = new SkinSelectScreen();
		skins = new HashMap<>();
		
		AliceSkinStrings original = AliceSkinStrings.get(SkinEnum.ORIGINAL.name().toLowerCase());
		assert(original != null);
		skins.put(SkinEnum.ORIGINAL, new Skin(
				original.NAME,
				original.DESCRIPTION,
				AliceSpireKit.getImgFilePath("charSelect/AliceMargatroid", "original"),
				AliceSpireKit.getImgFilePath("char/AliceMargatroid", "alice"),
				AliceSpireKit.getImgFilePath("char/AliceMargatroid", "shoulder1"),
				AliceSpireKit.getImgFilePath("char/AliceMargatroid", "corpse")
		));
		
		AliceSkinStrings ms = AliceSkinStrings.get(SkinEnum.MS.name().toLowerCase());
		assert(ms != null);
		skins.put(SkinEnum.MS, new Skin(
				ms.NAME,
				ms.DESCRIPTION,
				AliceSpireKit.getImgFilePath("charSelect/AliceMargatroid", "ms"),
				AliceSpireKit.getImgFilePath("char/AliceMargatroid", "alice_ms"),
				AliceSpireKit.getImgFilePath("char/AliceMargatroid", "shoulder1"),
				AliceSpireKit.getImgFilePath("char/AliceMargatroid", "corpse")
		));
		
		inst.unlocked = AliceConfigHelper.isAliceSkinSelectionUnlocked();
		inst.cur = SkinEnum.valueOf(AliceConfigHelper.getAliceSkinChosen());
		
		inst.refresh();
		inst.title = CardCrawlGame.languagePack.getUIString("AliceSkinSelectionScreen").TEXT[0];
		
//		findAliceAndSetSkin();
	}
	
	public enum SkinEnum {
		ORIGINAL,
		MS; // MS = Mystic Square
		
		public static SkinEnum prev(SkinEnum skin) {
			if (skin.ordinal() == 0)
				return null;
			return SkinEnum.values()[skin.ordinal() - 1];
		}
		
		public static SkinEnum next(SkinEnum skin) {
			if (skin.ordinal() == SkinEnum.values().length - 1)
				return null;
			return SkinEnum.values()[skin.ordinal() + 1];
		}
	}
	
	public static class Skin {
		public String name, description;
		public String background, img, shoulder, corpse;
		
		public Skin(String name, String description, String background, String img, String shoulder, String corpse) {
			this.name = name;
			this.description = description;
			
			this.background = background;
			this.img = img;
			this.shoulder = shoulder;
			this.corpse = corpse;
		}
	}
}
