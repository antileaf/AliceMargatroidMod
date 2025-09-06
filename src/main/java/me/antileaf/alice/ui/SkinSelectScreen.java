package me.antileaf.alice.ui;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.characters.AliceMargatroid;
import me.antileaf.alice.patches.enums.AbstractPlayerEnum;
import me.antileaf.alice.patches.enums.LibraryTypeEnum;
import me.antileaf.alice.strings.AliceSkinStrings;
import me.antileaf.alice.utils.*;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SkinSelectScreen {
	private static final Logger logger = LogManager.getLogger(SkinSelectScreen.class.getName());

	@Deprecated
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

	private static final int[] UNLOCK_ALL_SIGNATURE = {
			Input.Keys.Y,
			Input.Keys.O,
			Input.Keys.U,
			Input.Keys.R,
			Input.Keys.SPACE,
			Input.Keys.E,
			Input.Keys.Y,
			Input.Keys.E,
			Input.Keys.S,
			Input.Keys.SPACE,
			Input.Keys.A,
			Input.Keys.R,
			Input.Keys.E,
			Input.Keys.SPACE,
			Input.Keys.S,
			Input.Keys.H,
			Input.Keys.I,
			Input.Keys.N,
			Input.Keys.I,
			Input.Keys.N,
			Input.Keys.G,
			Input.Keys.SPACE,
			Input.Keys.D,
			Input.Keys.I,
			Input.Keys.A,
			Input.Keys.M,
			Input.Keys.O,
			Input.Keys.N,
			Input.Keys.D,
			Input.Keys.S,
			Input.Keys.PERIOD
	}; // Your eyes are shining diamonds.

	private static final int[] UNLOCK_A20 = {
			Input.Keys.W,
			Input.Keys.H,
			Input.Keys.E,
			Input.Keys.R,
			Input.Keys.E,
			Input.Keys.SPACE,
			Input.Keys.A,
			Input.Keys.L,
			Input.Keys.L,
			Input.Keys.SPACE,
			Input.Keys.M,
			Input.Keys.I,
			Input.Keys.R,
			Input.Keys.A,
			Input.Keys.C,
			Input.Keys.L,
			Input.Keys.E,
			Input.Keys.S,
			Input.Keys.SPACE,
			Input.Keys.B,
			Input.Keys.E,
			Input.Keys.G,
			Input.Keys.I,
			Input.Keys.N
	}; // Vol.F. Where All Miracles Begin
	
	public static boolean shouldUpdateBackground = false;
	public static Map<SkinEnum, Skin> skins;
	
	public static SkinSelectScreen inst;
	
	public Hitbox prevHb, nextHb, sunglassesHb;

	@Deprecated
	boolean unlocked = true;
	SkinEnum cur;
	
	Texture img;
	
	String title, option;
	String thanks, alsoTry;
	String name, description;

//	int konamiIndex = 0;
//	int easterEggIndex = 0;

	EasterEgg konami, signatureEasterEgg, unlockA20;
	
	public SkinSelectScreen() {
		this.prevHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		this.nextHb = new Hitbox(70.0F * Settings.scale, 70.0F * Settings.scale);
		this.sunglassesHb = new Hitbox(240.0F * Settings.scale, 80.0F * Settings.scale);

		this.konami = new EasterEgg(KONAMI_CODE,
				() -> {
					AliceConfigHelper.setSunglassesUnlocked(true);
					AliceConfigHelper.setSunglassesEnabled(true);
					AliceConfigHelper.save();

					CardCrawlGame.sound.play("UNLOCK_PING");

					this.refresh();
				},
				(key) -> logger.debug("Successfully pressed key: {}",
						Input.Keys.toString(key)),
				(key) -> logger.debug("Failed to press key: {}",
						Input.Keys.toString(key)),
				true
		);

		this.signatureEasterEgg = new EasterEgg(UNLOCK_ALL_SIGNATURE,
				() -> {
					logger.info("Congratulations! You have unlocked the Easter Egg!");

					for (AbstractCard card : CardLibrary.getCardList(LibraryTypeEnum.ALICE_MARGATROID_COLOR))
						if (card instanceof AbstractAliceCard) {
							AbstractAliceCard ac = (AbstractAliceCard) card;

							if (ac.hasSignature && !SignatureHelper.isUnlocked(ac.cardID)) {
								SignatureHelper.unlock(ac.cardID, true);
								SignatureHelper.enable(ac.cardID, true);

								logger.info("Unlocked card: {}", ac.cardID);
							}
						}

					CardCrawlGame.sound.play(AliceAudioMaster.EASTER_EGG);
				}
		);

		this.unlockA20 = new EasterEgg(UNLOCK_A20,
				() -> {
					logger.info("Vol.F. Where All Miracles Begin");

					AbstractPlayer character = CardCrawlGame.characterManager.getCharacter(
							AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS);
					Prefs pref = character.getPrefs();

					if (pref.getInteger("WIN_COUNT", 0) <= 0)
						pref.putInteger("WIN_COUNT", 1);

					if (pref.getInteger("ASCENSION_LEVEL", 1) == 20)
						logger.info("Already unlocked A20");
					else {
						pref.putInteger("ASCENSION_LEVEL", 20);
						pref.putInteger("LAST_ASCENSION_LEVEL", 20);
						logger.info("Unlocked A20");
					}

					pref.flush();
					character.refreshCharStat();

					CardCrawlGame.mainMenuScreen.charSelectScreen.isAscensionMode = true;

					CardCrawlGame.mainMenuScreen.charSelectScreen.options.stream()
									.filter(o -> o.c instanceof AliceMargatroid)
											.forEach(o -> {
												ReflectionHacks.setPrivate(o, CharacterOption.class,
														"maxAscensionLevel", 20);

												o.incrementAscensionLevel(20);
											});

					Stream.of(new String[] { "CROW", "DONUT", "WIZARD" })
							.forEach(UnlockTracker::markBossAsSeen);

					CardCrawlGame.sound.play(AliceAudioMaster.ARIS_COMMON_SKILL);
				});
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

		if (AbstractDungeon.player instanceof AliceMargatroid)
			((AliceMargatroid) AbstractDungeon.player).updateSkin();
	}
	
	public Skin getSkin() {
		return skins.get(this.cur);
	}

	public SkinEnum getSkinEnum() {
		return this.cur;
	}

	public String getSoundKey() {
		if (SkinSelectScreen.inst.getSkinEnum() != SkinSelectScreen.SkinEnum.BA &&
				SkinSelectScreen.inst.getSkinEnum() != SkinSelectScreen.SkinEnum.MAID)
			return "AliceMargatroid:CHAR_SELECT_" + MathUtils.random(1, 3);
		else if (SkinSelectScreen.inst.getSkinEnum() == SkinSelectScreen.SkinEnum.BA)
			return "AliceMargatroid:ARIS_SELECT_" + MathUtils.random(1, 2);
		else
			return "AliceMargatroid:ARIS_MAID_SELECT_" + MathUtils.random(1, 2);
	}
	
	public String getPortrait() {
		logger.info("Skin: {}", this.cur);
		return skins.get(this.cur).background;
	}
	
	private void updateInput() {
		if (CardCrawlGame.chosenCharacter == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS) {
			this.prevHb.update();
			this.nextHb.update();

			SkinEnum prev = SkinEnum.prev(this.cur), next = SkinEnum.next(this.cur);
			SkinEnum temp = this.cur;

			if (prev != null) {
				boolean clicked = false;
				
				if (!Settings.isControllerMode) {
					if (this.prevHb.justHovered)
						CardCrawlGame.sound.play("UI_HOVER");
					
					if (this.prevHb.clicked) {
						clicked = true;
						this.prevHb.clicked = false;
					}
				}
				else {
					if (!AliceControllerHelper.isRightTriggerPressed()) {
						if (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) {
							CInputActionSet.up.unpress();
							CInputActionSet.altUp.unpress();
							clicked = true;
						}
					}
				}
				
				if (clicked) {
					CardCrawlGame.sound.play("UI_CLICK_1");
					this.cur = prev;
					this.refresh();
				}
			}

			if (next != null) {
				boolean clicked = false;
				
				if (!Settings.isControllerMode) {
					if (this.nextHb.justHovered)
						CardCrawlGame.sound.play("UI_HOVER");
					
					if (this.nextHb.clicked) {
						clicked = true;
						this.nextHb.clicked = false;
					}
				}
				else {
					if (!AliceControllerHelper.isRightTriggerPressed()) {
						if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
							CInputActionSet.down.unpress();
							CInputActionSet.altDown.unpress();
							clicked = true;
						}
					}
				}
				
				if (clicked) {
					CardCrawlGame.sound.play("UI_CLICK_1");
					this.cur = next;
					this.refresh();
				}
			}

			if (temp != this.cur && !((temp == SkinEnum.ORIGINAL || temp == SkinEnum.MS) &&
					(this.cur == SkinEnum.ORIGINAL || this.cur == SkinEnum.MS)))
				CardCrawlGame.sound.play(this.getSoundKey());

			if (AliceConfigHelper.isSunglassesUnlocked()) {
				this.sunglassesHb.update();
				
				boolean clicked = false;

				if (!Settings.isControllerMode) {
					if (this.sunglassesHb.justHovered)
						CardCrawlGame.sound.play("UI_HOVER");
					
					if (this.sunglassesHb.clicked) {
						clicked = true;
						this.sunglassesHb.clicked = false;
					}
				}
				else {
					if (!AliceControllerHelper.isRightTriggerPressed()) {
						if (CInputActionSet.peek.isJustPressed()) {
							CInputActionSet.peek.unpress();
							clicked = true;
						}
					}
				}
				
				if (clicked) {
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

			this.konami.updateInput();
			this.signatureEasterEgg.updateInput();
			this.unlockA20.updateInput();
		}
		else {
			this.konami.clear();
			this.signatureEasterEgg.clear();
			this.unlockA20.clear();
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

		if (this.getSkinEnum() == SkinEnum.BA) {
			FontHelper.renderFontCentered(sb,
					FontHelper.cardTitleFont,
					this.thanks,
					centerX,
					centerY + 250.0F * Settings.scale,
					Color.WHITE,
					0.85F
			);

			FontHelper.renderFontCentered(sb,
					FontHelper.cardTitleFont,
					this.alsoTry,
					centerX,
					centerY + 220.0F * Settings.scale,
					Color.WHITE,
					0.85F
			);
		}

		sb.setColor(Color.WHITE);
		float width = this.img.getWidth() * Settings.scale / 1.3F;
		float height = this.img.getHeight() * Settings.scale / 1.3F;
		sb.draw(this.img,
				centerX - width / 2.0F,
				centerY - height / 2.0F,
				width, height);
		
		final float CONTROLLER_IMG_OFFSET = 60.0F * Settings.scale;

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
			
			if (Settings.isControllerMode) {
				sb.setColor(Color.WHITE);
				sb.draw(CInputActionSet.peek.getKeyImg(),
						this.sunglassesHb.x - 32.0F,
						this.sunglassesHb.cY - 32.0F,
						32.0F, 32.0F,
						64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F,
						0, 0,
						64, 64,
						false, false);
			}

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
			
			if (Settings.isControllerMode) {
				sb.setColor(Color.WHITE);
				sb.draw(CInputActionSet.up.getKeyImg(),
						this.prevHb.cX - 32.0F,
						this.prevHb.cY - CONTROLLER_IMG_OFFSET + 3.0F - 32.0F,
						32.0F, 32.0F,
						64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F,
						0, 0,
						64, 64,
						false, false);
			}
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
			
			if (Settings.isControllerMode) {
				sb.setColor(Color.WHITE);
				sb.draw(CInputActionSet.down.getKeyImg(),
						this.nextHb.cX - 32.0F,
						this.nextHb.cY - CONTROLLER_IMG_OFFSET - 3.0F - 32.0F,
						32.0F, 32.0F,
						64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F,
						0, 0,
						64, 64,
						false, false);
			}
		}

		this.prevHb.render(sb);
		this.nextHb.render(sb);
		
		if (Settings.isControllerMode && Gdx.input.isKeyPressed(Input.Keys.BUTTON_R2)) {
			sb.setColor(Color.WHITE);
			sb.draw(CInputActionSet.discardPile.getKeyImg(),
					this.prevHb.cX - CONTROLLER_IMG_OFFSET - 32.0F,
					this.prevHb.cY - 32.0F,
					32.0F, 32.0F,
					64.0F, 64.0F,
					Settings.scale, Settings.scale,
					0.0F,
					0, 0,
					64, 64,
					false, false);
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

		AliceSkinStrings maid = AliceSkinStrings.get(SkinEnum.MAID.name().toLowerCase());
		assert(maid != null);
		skins.put(SkinEnum.MAID, new Skin(
				maid.NAME,
				maid.DESCRIPTION,
				AliceHelper.getImgFilePath("charSelect/AliceMargatroid", "maid"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "aris_maid"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "aris_maid_shoulder"),
				AliceHelper.getImgFilePath("char/AliceMargatroid", "aris_maid_corpse")
		));
		
		inst.unlocked = true; // AliceConfigHelper.isAliceSkinSelectionUnlocked();
		inst.cur = SkinEnum.valueOf(AliceConfigHelper.getAliceSkinChosen());
		
		inst.refresh();
		inst.title = CardCrawlGame.languagePack.getUIString(
				AliceHelper.makeID(SkinSelectScreen.class.getSimpleName())).TEXT[0];
		inst.option = CardCrawlGame.languagePack.getUIString(
				AliceHelper.makeID(SkinSelectScreen.class.getSimpleName())).TEXT[1];
		inst.thanks = CardCrawlGame.languagePack.getUIString(
				AliceHelper.makeID(SkinSelectScreen.class.getSimpleName())).TEXT[2];
		inst.alsoTry = CardCrawlGame.languagePack.getUIString(
				AliceHelper.makeID(SkinSelectScreen.class.getSimpleName())).TEXT[3];
		
//		findAliceAndSetSkin();
	}
	
	public enum SkinEnum {
		ORIGINAL,
		MS, // Mystic Square
		BA, // Blue Archive
		MAID; // Aris (Maid)
		
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
