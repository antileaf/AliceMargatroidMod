package rs.antileaf.alice.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.strings.AliceSkinStrings;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.HashMap;
import java.util.Map;

public class SkinSelectScreen {
	private static final Logger logger = LogManager.getLogger(SkinSelectScreen.class.getName());

	public static final String SAVABLE_KEY = "alice_margatroid_skin";
	private static final float MAN = -1000.0F;

	private static final int[] KONAMI_CODE = {
			Input.Keys.UP,
			Input.Keys.UP,
			Input.Keys.DOWN,
			Input.Keys.DOWN,
			Input.Keys.LEFT,
			Input.Keys.RIGHT,
			Input.Keys.LEFT,
			Input.Keys.RIGHT,
			Input.Keys.B,
			Input.Keys.A
	};
	
	public static boolean shouldUpdateBackground = false;
	public static Map<SkinEnum, Skin> skins;
	
	public static SkinSelectScreen inst;
	
	public Hitbox prevHb, nextHb, sunglassesHb;
	
	boolean unlocked = false;
	SkinEnum cur;
	
	Texture img;
	
	String title, option;
	String name, description;

	int konamiIndex = 0;
	
	public SkinSelectScreen() {
		this.prevHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		this.nextHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		this.sunglassesHb = new Hitbox(240.0F * Settings.scale, 80.0F * Settings.scale);
	}
	
	public void loadAnimation(String atlas, String skeleton, float scale) {
		// There is no animation now.
	}
	
	public void refresh() {
		AliceSkinStrings skinStrings = AliceSkinStrings.get(this.cur.name());
		if (skinStrings != null) {
			this.name = skinStrings.NAME;
			this.description = skinStrings.DESCRIPTION;
			if (!AliceConfigHelper.isSunglassesEnabled())
				this.img = ImageMaster.loadImage(skins.get(this.cur).img);
			else
				this.img = ImageMaster.loadImage(skins.get(this.cur).ss);
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

	public SkinEnum getSkinEnum() {
		return this.cur;
	}

	public String getSoundKey() {
		if (SkinSelectScreen.inst.getSkinEnum() != SkinSelectScreen.SkinEnum.BA)
			return "AliceMargatroid:CHAR_SELECT_" + MathUtils.random(1, 3);
		else
			return "AliceMargatroid:ARIS_SELECT_" + MathUtils.random(1, 2);
	}
	
	public String getPortrait() {
		AliceHelper.log("Skin: " + this.cur);
		return skins.get(this.cur).background;
	}
	
	private void updateInput() {
		if (CardCrawlGame.chosenCharacter == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS) {
			this.prevHb.update();
			this.nextHb.update();
			
			SkinEnum prev = SkinEnum.prev(this.cur), next = SkinEnum.next(this.cur);
			SkinEnum temp = this.cur;
			
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

			if (temp != this.cur && (temp == SkinEnum.BA) != (this.cur == SkinEnum.BA))
					CardCrawlGame.sound.play(this.getSoundKey());

			if (AliceConfigHelper.isSunglassesUnlocked()) {
				this.sunglassesHb.update();

				if (this.sunglassesHb.justHovered)
					CardCrawlGame.sound.play("UI_HOVER");

				if (this.sunglassesHb.clicked) {
					this.sunglassesHb.clicked = false;
					CardCrawlGame.sound.play("UI_CLICK_1");
					AliceConfigHelper.setSunglassesEnabled(!AliceConfigHelper.isSunglassesEnabled());
					AliceConfigHelper.save();
					this.refresh();
				}
			}
			
			if (InputHelper.justClickedLeft) {
				if (this.prevHb.hovered)
					this.prevHb.clickStarted = true;
				if (this.nextHb.hovered)
					this.nextHb.clickStarted = true;
				if (this.sunglassesHb.hovered)
					this.sunglassesHb.clickStarted = true;
			}

			if (konamiIndex < KONAMI_CODE.length) {
				boolean pressed = false;
				for (int key : KONAMI_CODE) {
					if (Gdx.input.isKeyJustPressed(key)) {
						pressed = true;
						break;
					}
				}

				if (pressed) {
					int cur = KONAMI_CODE[konamiIndex];
					boolean match = Gdx.input.isKeyJustPressed(cur);

					for (int key : KONAMI_CODE)
						if (key != cur && Gdx.input.isKeyJustPressed(key)) {
							match = false;
							break;
						}

					if (match) {
						konamiIndex++;
//						CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
						logger.info("Successfully pressed key: {}", Input.Keys.toString(cur));
					}
					else {
						konamiIndex = 0;
//						int roll = MathUtils.random(2);
//						if (roll == 0) {
//							CardCrawlGame.sound.play("VO_MERCHANT_2A");
//						} else if (roll == 1) {
//							CardCrawlGame.sound.play("VO_MERCHANT_2B");
//						} else {
//							CardCrawlGame.sound.play("VO_MERCHANT_2C");
//						}
						logger.info("Mismatched key: {}", Input.Keys.toString(cur));
					}

					if (konamiIndex == KONAMI_CODE.length) {
						AliceConfigHelper.setSunglassesUnlocked(true);
						AliceConfigHelper.setSunglassesEnabled(true);
						AliceConfigHelper.save();

						CardCrawlGame.sound.play("UNLOCK_PING");

						this.refresh();

						konamiIndex = 0;
					}
				}

			}
			else
				konamiIndex = 0;
		}
		else {
			konamiIndex = 0;
		}
	}
	
	public void update() {
//		if (!AliceConfigHelper.isAliceSkinSelectionUnlocked())
//			return;
		
		float centerX = Settings.WIDTH * 0.86F;
		float centerY = Settings.HEIGHT * 0.55F;
		
		if (SkinEnum.prev(this.cur) != null)
			this.prevHb.move(centerX - 140.0F * Settings.scale, centerY);
		else
			this.prevHb.move(MAN, MAN);
		if (SkinEnum.next(this.cur) != null)
			this.nextHb.move(centerX + 140.0F * Settings.scale, centerY);
		else
			this.nextHb.move(MAN, MAN);

		if (AliceConfigHelper.isSunglassesUnlocked())
			this.sunglassesHb.move(centerX, centerY - 260.0F * Settings.scale);
		else
			this.sunglassesHb.move(MAN, MAN);
		
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

		if (AliceConfigHelper.isSunglassesUnlocked()) {
			float x = centerX + 20.0F * Settings.scale;

			sb.draw(ImageMaster.CHECKBOX,
					x - 80.0F * Settings.scale - 32.0F,
					centerY - 260.0F * Settings.scale - 32.0F,
					32.0F, 32.0F,
					64.0F, 64.0F,
					Settings.scale, Settings.scale,
					0.0F, 0, 0, 64, 64,
					false, false);

			FontHelper.renderFontLeft(sb,
					FontHelper.cardTitleFont,
					this.option,
					 x - 45.0F * Settings.scale,
					centerY - 260.0F * Settings.scale,
					this.sunglassesHb.hovered ? Settings.BLUE_TEXT_COLOR : Color.WHITE
			);

			if (AliceConfigHelper.isSunglassesEnabled()) {
				sb.setColor(Color.WHITE);
				sb.draw(ImageMaster.TICK,
						x - 80.0F * Settings.scale - 32.0F,
						centerY - 260.0F * Settings.scale - 32.0F,
						32.0F, 32.0F,
						64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F, 0, 0, 64, 64,
						false, false);
			}

			this.sunglassesHb.render(sb);
		}
		
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

		this.prevHb.render(sb);
		this.nextHb.render(sb);
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
				AliceHelper.getImgFilePath("charSelect/AliceMargatroid", "original"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "alice"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "shoulder"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "corpse")
		));
		
		AliceSkinStrings ms = AliceSkinStrings.get(SkinEnum.MS.name().toLowerCase());
		assert(ms != null);
		skins.put(SkinEnum.MS, new Skin(
				ms.NAME,
				ms.DESCRIPTION,
				AliceHelper.getImgFilePath("charSelect/AliceMargatroid", "ms"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "alice_ms"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "shoulder"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "corpse_ms")
		));

		AliceSkinStrings ba = AliceSkinStrings.get(SkinEnum.BA.name().toLowerCase());
		assert(ba != null);
		skins.put(SkinEnum.BA, new Skin(
				ba.NAME,
				ba.DESCRIPTION,
				AliceHelper.getImgFilePath("charSelect/AliceMargatroid", "ba"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "aris"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "aris_shoulder"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "aris_corpse")
		));
		
		inst.unlocked = AliceConfigHelper.isAliceSkinSelectionUnlocked();
		inst.cur = SkinEnum.valueOf(AliceConfigHelper.getAliceSkinChosen());
		
		inst.refresh();
		inst.title = CardCrawlGame.languagePack.getUIString(
				AliceHelper.makeID(SkinSelectScreen.class.getSimpleName())).TEXT[0];
		inst.option = CardCrawlGame.languagePack.getUIString(
				AliceHelper.makeID(SkinSelectScreen.class.getSimpleName())).TEXT[1];
		
//		findAliceAndSetSkin();
	}
	
	public enum SkinEnum {
		ORIGINAL,
		MS, // Mystic Square
		BA; // Blue Archive
		
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
		public String background, img, ss, shoulder, corpse;
		
		public Skin(String name, String description, String background, String img, String shoulder, String corpse) {
			this.name = name;
			this.description = description;
			
			this.background = background;
			this.img = img;
			this.ss = img.replace(".png", "_ss.png");
			this.shoulder = shoulder;
			this.corpse = corpse;
		}
	}
}
