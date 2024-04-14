package rs.antileaf.alice.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.cards.AliceMargatroid.Thread;
import rs.antileaf.alice.cards.AliceMargatroid.*;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.AliceMargatroidModClassEnum;
import rs.antileaf.alice.relics.AlicesGrimoire;

import java.util.ArrayList;

public class AliceMargatroid extends CustomPlayer {
	
	private static final int ENERGY_PER_TURN = 3; // how much energy you get every turn
	private static final String ALICE_SHOULDER_2 = "AliceMargatroidMod/img/char/AliceMargatroid/shoulder2.png"; // shoulder2 / shoulder_1
	private static final String ALICE_SHOULDER_1 = "AliceMargatroidMod/img/char/AliceMargatroid/shoulder1.png"; // shoulder1 / shoulder_2
	private static final String ALICE_CORPSE = "AliceMargatroidMod/img/char/AliceMargatroid/fallen.png"; // dead corpse
	public static final Logger logger = LogManager.getLogger(AliceMargatroidMod.class.getName());
	//private static final float[] layerSpeeds = { 20.0F, 0.0F, -40.0F, 0.0F, 0.0F, 5.0F, 0.0F, -8.0F, 0.0F, 8.0F };
//	private static final String ALICE_SKELETON_ATLAS = "img/char/Reiuji/MarisaModelv3.atlas";// Marisa_v0 / MarisaModel_v02 /MarisaModelv3
//	private static final String ALICE_SKELETON_JSON = "img/char/Reiuji/MarisaModelv3.json";
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
	//public static final String SPRITER_ANIM_FILEPATH = "img/char/MyCharacter/marisa_test.scml"; // spriter animation scml
	
	public AliceMargatroid(String name) {
		//super(name, setClass, null, null , null ,new SpriterAnimation(SPRITER_ANIM_FILEPATH));
		super(name, AliceMargatroidModClassEnum.ALICE_MARGATROID, ORB_TEXTURES, ORB_VFX, LAYER_SPEED, null, null);
		//super(name, setClass, null, null, (String) null, null);
		
		this.dialogX = (this.drawX + 0.0F * Settings.scale); // set location for text bubbles
		this.dialogY = (this.drawY + 220.0F * Settings.scale); // you can just copy these values
		
		logger.info("init Alice Margatroid");
		
		this.initializeClass(
				"AliceMargatroidMod/img/char/AliceMargatroid/alice.png",
				ALICE_SHOULDER_2, // required call to load textures and setup energy/loadout
				ALICE_SHOULDER_1,
				ALICE_CORPSE,
				this.getLoadout(),
				20.0F, -10.0F, 220.0F, 290.0F,
				new EnergyManager(ENERGY_PER_TURN)
		);
		
//		loadAnimation(REIUJI_SKELETON_ATLAS, REIUJI_SKELETON_JSON, 2.0F);
		// if you're using modified versions of base game animations or made animations in spine make sure to include this bit and the following lines
		/*
		AnimationState.TrackEntry e = this.state.setAnimation(0, REIUJI_ANIMATION, true);
		e.setTime(e.getEndTime() * MathUtils.random());
		this.stateData.setMix("Hit", "Idle", 0.1F);
		e.setTimeScale(1.0F);
		 */
		
//		this.maxOrbs = 0;
		
		logger.info("init finish");
	}
	
	public ArrayList<String> getStartingDeck() { // 初始卡组
		ArrayList<String> ret = new ArrayList<>();
		
		ret.add(LittleLegion.ID);
		ret.add(DollCrusader.ID);
		
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
	
	public CharSelectInfo getLoadout() { // the rest of the character loadout so includes your character select screen info plus hp and starting gold
		String title;
		String flavor;
		if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
			title = "爱丽丝·玛格特罗伊德";
			flavor = "居住在魔法之森的人偶使。 NL 拥有精细操控人偶的能力。";
		} else {
			title = "Alice Margatroid";
			flavor = "A puppeteer living in the Forest of Magic. NL Has the ability to operate dolls with precision.";
		}
		return new CharSelectInfo(
				title,
				flavor,
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
		return new Thread();
	}
	
	public String getTitle(PlayerClass playerClass) { // 称号
		String title;
		if (Settings.language == GameLanguage.ZHS ||
				Settings.language == GameLanguage.ZHT)
			title = "七色的人偶使";
		else if (Settings.language == GameLanguage.JPN)
			title = "七色の人形遣い";
		else
			title = "Seven-Colored Puppeteer";

		return title;
	}
	
	public Color getCardTrailColor() {
		return AliceMargatroidMod.ALICE_PUPPETEER;
	}
	
	public int getAscensionMaxHPLoss() {
		return ASCENSION_MAX_HP_LOSS;
	}
	
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontBlue;
	}
	
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA("SELECT_ALICE_Margatroid", MathUtils.random(-0.1F, 0.1F));
		CardCrawlGame.screenShake.shake(
				ScreenShake.ShakeIntensity.MED,
				ScreenShake.ShakeDur.SHORT,
				false
		);
	}
	
	public String getCustomModeCharacterButtonSoundKey() {
		return "SELECT_ALICE_Margatroid";
	}
	
	public String getLocalizedCharacterName() {
		String char_name;
		if ((Settings.language == Settings.GameLanguage.ZHS)
				|| (Settings.language == Settings.GameLanguage.ZHT)) {
			char_name = "爱丽丝·玛格特罗伊德";
		} else if (Settings.language == Settings.GameLanguage.JPN) {
			char_name = "アリス・マーガトロイド";
		} else {
			char_name = "Alice Margatroid";
		}
		return char_name;
	}
	
	public AbstractPlayer newInstance() {
		return new AliceMargatroid(this.name);
	}
	
	public String getVampireText() { // TODO: Change here to change the vampire text
		return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[1];
	}
	
	public Color getCardRenderColor() {
		return AliceMargatroidMod.ALICE_PUPPETEER;
	}
	
	@Override
	public void updateOrb(int orbCount) {
		this.energyOrb.updateOrb(orbCount);
	}
	
	public TextureAtlas.AtlasRegion getOrb() {
		return new TextureAtlas.AtlasRegion(ImageMaster.loadImage(AliceMargatroidMod.CARD_ENERGY_ORB), 0, 0, 24, 24);
	}
	
	public Color getSlashAttackColor() {
		return AliceMargatroidMod.ALICE_PUPPETEER;
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
//		return com.megacrit.cardcrawl.events.beyond.SpireHeart.DESCRIPTIONS[10];
		return "NL 你的人偶们在你的指挥下，向心脏发动了致命的攻击。";
	}
	
	@Override
	public String getSensoryStoneText() {
		return CardCrawlGame.languagePack.getEventString("AliceSensoryStone").DESCRIPTIONS[0];
	}
	
	public void damage(DamageInfo info) {
//		if ((info.owner != null) && (info.type != DamageInfo.DamageType.THORNS)
//				&& (info.output - this.currentBlock > 0)) {
//			AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
//			this.state.addAnimation(0, "Idle", true, 0.0F);
//			e.setTimeScale(1.0F);
//		}
		super.damage(info);
	}
	
	public void applyPreCombatLogic() {
		super.applyPreCombatLogic();
	}
	
}
