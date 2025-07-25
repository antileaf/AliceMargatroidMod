package me.antileaf.alice.characters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import me.antileaf.alice.AliceMargatroidMod;
import me.antileaf.alice.cards.alice.Chant;
import me.antileaf.alice.cards.alice.Defend_AliceMargatroid;
import me.antileaf.alice.cards.alice.DollPlacement;
import me.antileaf.alice.cards.alice.Strike_AliceMargatroid;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.AbstractPlayerEnum;
import me.antileaf.alice.relics.AlicesGrimoire;
import me.antileaf.alice.relics.SwordOfLight_Supernova;
import me.antileaf.alice.ui.SkinSelectScreen;
import me.antileaf.alice.utils.AliceConfigHelper;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class AliceMargatroid extends CustomPlayer {
	private static final CharacterStrings characterStrings =
			CardCrawlGame.languagePack.getCharacterString(AliceMargatroid.class.getSimpleName());

	private static final String ALICE_SHOULDER_2 = "AliceMargatroidMod/img/char/AliceMargatroid/shoulder.png"; // shoulder2 / shoulder_1
	private static final String ALICE_SHOULDER_1 = "AliceMargatroidMod/img/char/AliceMargatroid/shoulder.png"; // shoulder1 / shoulder_2
	private static final String ALICE_CORPSE = "AliceMargatroidMod/img/char/AliceMargatroid/corpse.png"; // dead corpse
	private static final String[] ORB_TEXTURES = {
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/4.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/3.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/2.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/1.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/0.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/4d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/3d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/2d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/1d.png"
	};
	private static final String ORB_VFX = "AliceMargatroidMod/img/UI/AliceMargatroid/vfx.png";
	private static final float[] LAYER_SPEED =
			{0.0F, -16.0F, 0.0F, 20.0F, 0.0F};

	private static final Texture SWORD_OF_LIGHT = ImageMaster.loadImage(
			AliceHelper.getImgFilePath("char/AliceMargatroid", "Sword"));
	
	public AliceMargatroid(String name) {
		super(
				name,
				AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS,
				ORB_TEXTURES,
				ORB_VFX,
				LAYER_SPEED,
				null,
				null
		);
		
		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values
		
		AliceHelper.logger.info("init Alice Margatroid");
		
		this.initializeClass(
				"AliceMargatroidMod/img/char/AliceMargatroid/alice.png",
				ALICE_SHOULDER_2, // required call to load textures and setup energy/loadout
				ALICE_SHOULDER_1,
				ALICE_CORPSE,
				this.getLoadout(),
				20.0F, -10.0F, 220.0F, 290.0F,
				new EnergyManager(3)
		);
		
//		this.maxOrbs = 0;

		if (SkinSelectScreen.inst != null)
			this.updateSkin();

		AliceHelper.logger.info("init finish");
	}
	
	public ArrayList<String> getStartingDeck() { // 初始卡组
		ArrayList<String> ret = new ArrayList<>();
		
		ret.add(DollPlacement.ID);
		ret.add(Chant.ID);
		
		for (int i = 0; i < 4; i++)
			ret.add(Strike_AliceMargatroid.ID);
		
		for (int i = 0; i < 4; i++)
			ret.add(Defend_AliceMargatroid.ID);
		
		return ret;
	}
	
	public ArrayList<String> getStartingRelics() { // 初始遗物
		ArrayList<String> ret = new ArrayList<>();
		ret.add(AlicesGrimoire.ID);
		UnlockTracker.markRelicAsSeen(AlicesGrimoire.ID);
		return ret;
	}
	
	private static final int STARTING_HP = 77;
	private static final int MAX_HP = 77;
	private static final int STARTING_GOLD = 99;
	private static final int HAND_SIZE = 5;
	private static final int ASCENSION_MAX_HP_LOSS = 7;
	
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(
				characterStrings.NAMES[0],
				characterStrings.TEXT[0],
				STARTING_HP,
				MAX_HP,
				0,
				STARTING_GOLD,
				HAND_SIZE,
				this,
				this.getStartingRelics(),
				this.getStartingDeck(),
				false
		);
	}
	
	public AbstractCard.CardColor getCardColor() {
		return AbstractCardEnum.ALICE_MARGATROID_COLOR;
	}
	
	public AbstractCard getStartCardForEvent() {
		return new DollPlacement();
	}
	
	public String getTitle(PlayerClass playerClass) { // 称号
		String title = characterStrings.NAMES[2];

		int easterEgg = MathUtils.random(1, 50); // 谁不喜欢彩蛋呢？
		if (easterEgg == 1)
			title = characterStrings.NAMES[3];
		else if (easterEgg == 2 && characterStrings.NAMES.length > 4)
			title = characterStrings.NAMES[4];

		return title;
	}
	
	public Color getCardTrailColor() {
		return AliceMargatroidMod.ALICE_IMPRESSION_COLOR.cpy();
	}
	
	public int getAscensionMaxHPLoss() {
		return ASCENSION_MAX_HP_LOSS;
	}
	
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontBlue;
	}
	
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.play(this.getCustomModeCharacterButtonSoundKey());
		CardCrawlGame.screenShake.shake(
				ScreenShake.ShakeIntensity.LOW,
				ScreenShake.ShakeDur.SHORT,
				false
		);
	}
	
	public String getCustomModeCharacterButtonSoundKey() {
		return SkinSelectScreen.inst.getSoundKey();
	}
	
	public String getLocalizedCharacterName() {
		return characterStrings.NAMES[0];
//		String char_name;
//		if ((Settings.language == Settings.GameLanguage.ZHS)
//				|| (Settings.language == Settings.GameLanguage.ZHT)) {
//			char_name = "爱丽丝·玛格特罗伊德";
//		} else if (Settings.language == Settings.GameLanguage.JPN) {
//			char_name = "アリス・マーガトロイド";
//		} else {
//			char_name = "Alice Margatroid";
//		}
//		return char_name;
	}

	public String getCustomModeModCharacterName() { // Used in patch
		return characterStrings.NAMES[1];
	}
	
	public AbstractPlayer newInstance() {
		return new AliceMargatroid(this.name);
	}
	
	public String getVampireText() {
		return Vampires.DESCRIPTIONS[1];
	}
	
	public Color getCardRenderColor() {
		return AliceMargatroidMod.ALICE_IMPRESSION_COLOR.cpy();
	}
	
//	@Override
//	public void updateOrb(int orbCount) {
//		this.energyOrb.updateOrb(orbCount);
//	}
	
//	public TextureAtlas.AtlasRegion getOrb() {
//		return new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AliceMargatroidMod.CARD_ENERGY_ORB), 0, 0, 24, 24);
//	}
	
	public Color getSlashAttackColor() {
		return AliceMargatroidMod.ALICE_IMPRESSION_COLOR.cpy();
	}
	
	public AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[]{
				AttackEffect.SLASH_HEAVY,
				AttackEffect.FIRE,
				AttackEffect.SLASH_DIAGONAL,
				AttackEffect.SLASH_HEAVY,
				AttackEffect.FIRE,
				AttackEffect.SLASH_DIAGONAL
		};
	}
	
	@Override
	public String getSpireHeartText() {
		return characterStrings.TEXT[1];
	}
	
	@Override
	public String getSensoryStoneText() {
		return CardCrawlGame.languagePack.getEventString(AliceHelper.makeID("SensoryStone")).DESCRIPTIONS[0];
	}
	
	@Override
	public void damage(DamageInfo info) {
//		if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS)
//				&& (info.output - this.currentBlock > 0)) {
//			AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
//			this.state.addAnimation(0, "Idle", true, 0.0F);
//			e.setTimeScale(1.0F);
//		}
		super.damage(info);
	}
	
	@Override
	public void applyPreCombatLogic() {
		super.applyPreCombatLogic();
	}

	@Override
	public ArrayList<CutscenePanel> getCutscenePanels() {
		ArrayList<CutscenePanel> panels = new ArrayList<>();
		panels.add(new CutscenePanel(AliceHelper.getImgFilePath("char/cg", "CutScene1"),
				"ATTACK_FAST"));
		panels.add(new CutscenePanel(AliceHelper.getImgFilePath("char/cg", "CutScene2")));
		panels.add(new CutscenePanel(AliceHelper.getImgFilePath("char/cg", "CutScene3")));
		return panels;
	}
	
//	public void setSkin(SkinSelectScreen.Skin skin) {
//		this.shouldUpdateSkin = skin;
//	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);

		if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom) &&
				!(boolean) ReflectionHacks.getPrivate(this, AbstractPlayer.class, "renderCorpse") &&
				(SkinSelectScreen.inst.getSkinEnum() == SkinSelectScreen.SkinEnum.BA ||
						SkinSelectScreen.inst.getSkinEnum() == SkinSelectScreen.SkinEnum.MAID) &&
				this.hasRelic(SwordOfLight_Supernova.ID)) {
			sb.setColor(Color.WHITE);
			sb.draw(SWORD_OF_LIGHT,
					this.drawX - SWORD_OF_LIGHT.getWidth() / 2.0F * Settings.scale + this.animX,
					this.drawY + (SWORD_OF_LIGHT.getWidth() - this.img.getWidth()) / 2.0F * Settings.scale,
					SWORD_OF_LIGHT.getWidth() * Settings.scale,
					SWORD_OF_LIGHT.getHeight() * Settings.scale,
					0, 0, SWORD_OF_LIGHT.getWidth(), SWORD_OF_LIGHT.getHeight(),
					this.flipHorizontal, this.flipVertical);
		}
	}
	
	public void updateSkin() {
		SkinSelectScreen.Skin skin = SkinSelectScreen.inst.getSkin();

		this.img = ImageMaster.loadImage(AliceConfigHelper.isSunglassesEnabled() ?
				skin.ss : skin.img);
		
		if (this.img != null)
			this.atlas = null;
		
		this.shoulderImg = ImageMaster.loadImage(skin.shoulder);
		this.shoulder2Img = ImageMaster.loadImage(skin.shoulder);
		this.corpseImg = ImageMaster.loadImage(skin.corpse);
	}
}
