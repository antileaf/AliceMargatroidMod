package rs.antileaf.alice.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.cards.alice.Chant;
import rs.antileaf.alice.cards.alice.Defend_AliceMargatroid;
import rs.antileaf.alice.cards.alice.DollPlacement;
import rs.antileaf.alice.cards.alice.Strike_AliceMargatroid;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.relics.AlicesGrimoire;
import rs.antileaf.alice.ui.SkinSelectScreen;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class AliceMargatroid extends CustomPlayer {
	private static final CharacterStrings characterStrings =
			CardCrawlGame.languagePack.getCharacterString(AliceMargatroid.class.getSimpleName());

	private static final String ALICE_SHOULDER_2 = "AliceMargatroidMod/img/char/AliceMargatroid/shoulder.png"; // shoulder2 / shoulder_1
	private static final String ALICE_SHOULDER_1 = "AliceMargatroidMod/img/char/AliceMargatroid/shoulder.png"; // shoulder1 / shoulder_2
	private static final String ALICE_CORPSE = "AliceMargatroidMod/img/char/AliceMargatroid/corpse.png"; // dead corpse
	private static final String ALICE_ANIMATION = "Idle";// Sprite / Idle
	private static final String[] ORB_TEXTURES = {
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer5.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer4.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer3.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer2.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer1.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer0.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer5d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer4d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer3d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer2d.png",
			"AliceMargatroidMod/img/UI/AliceMargatroid/EPanel/layer1d.png"
	};
	private static final String ORB_VFX = "AliceMargatroidMod/img/UI/AliceMargatroid/energyBlueVFX.png";
	private static final float[] LAYER_SPEED =
			{-40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F, -5.0F, 0.0F};

	private SkinSelectScreen.Skin skin = null;
	
	public AliceMargatroid(String name) {
		super(name, AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS, ORB_TEXTURES, ORB_VFX, LAYER_SPEED, null, null);
		
		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values
		
		AliceSpireKit.logger.info("init Alice Margatroid");
		
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

		AliceSpireKit.logger.info("init finish");
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
		return AliceMargatroidMod.ALICE_PUPPETEER.cpy();
	}
	
	public int getAscensionMaxHPLoss() {
		return ASCENSION_MAX_HP_LOSS;
	}
	
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontBlue;
	}
	
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.play("AliceMargatroidMod:CHAR_SELECT_" + MathUtils.random(1, 3));
		CardCrawlGame.screenShake.shake(
				ScreenShake.ShakeIntensity.LOW,
				ScreenShake.ShakeDur.SHORT,
				false
		);
	}
	
	public String getCustomModeCharacterButtonSoundKey() {
		return "AliceMargatroidMod:CHAR_SELECT_" + MathUtils.random(1, 3);
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
		return AliceMargatroidMod.ALICE_PUPPETEER.cpy();
	}
	
	@Override
	public void updateOrb(int orbCount) {
		this.energyOrb.updateOrb(orbCount);
	}
	
	public TextureAtlas.AtlasRegion getOrb() {
		return new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AliceMargatroidMod.CARD_ENERGY_ORB), 0, 0, 24, 24);
	}
	
	public Color getSlashAttackColor() {
		return AliceMargatroidMod.ALICE_PUPPETEER.cpy();
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
		return CardCrawlGame.languagePack.getEventString("AliceSensoryStone").DESCRIPTIONS[0];
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
	
	public void render(SpriteBatch sb) {
		if (this.skin != SkinSelectScreen.inst.getSkin()) {
			this.skin = SkinSelectScreen.inst.getSkin();
			this.updateSkin(this.skin.img, this.skin.shoulder, this.skin.corpse);
		}
		
		super.render(sb);
	}
	
//	public void setSkin(SkinSelectScreen.Skin skin) {
//		this.shouldUpdateSkin = skin;
//	}
	
	public void updateSkin(String img, String shoulder, String corpse) {
		this.img = ImageMaster.loadImage(img);
		
		if (this.img != null)
			this.atlas = null;
		
		this.shoulderImg = ImageMaster.loadImage(shoulder);
		this.shoulder2Img = ImageMaster.loadImage(shoulder);
		this.corpseImg = ImageMaster.loadImage(corpse);
	}
}
