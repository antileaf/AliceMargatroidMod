package me.antileaf.alice;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.abstracts.DynamicVariable;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.CardBorderGlowManager;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.icons.CustomIconHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.cards.alice.Bookmark;
import me.antileaf.alice.cards.colorless.CreateDoll;
import me.antileaf.alice.characters.AliceMargatroid;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.events.LilyOfTheValleyFlowerField;
import me.antileaf.alice.events.PuppeteersHouse;
import me.antileaf.alice.icon.AliceCustomIcon;
import me.antileaf.alice.patches.doll.DollMechanicsPatch;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.enums.AbstractPlayerEnum;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.patches.enums.LibraryTypeEnum;
import me.antileaf.alice.potions.ConcentrationPotion;
import me.antileaf.alice.potions.WeavingPotion;
import me.antileaf.alice.powers.unique.UnlockMysticPower;
import me.antileaf.alice.prediction.PredictionInitializer;
import me.antileaf.alice.relics.*;
import me.antileaf.alice.save.AliceSaveData;
import me.antileaf.alice.strings.*;
import me.antileaf.alice.targeting.handlers.*;
import me.antileaf.alice.ui.SkinSelectScreen;
import me.antileaf.alice.utils.*;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@SpireInitializer
public class AliceMargatroidMod implements
		PostExhaustSubscriber,
		PostBattleSubscriber,
		PostDungeonInitializeSubscriber,
		StartGameSubscriber,
		EditCharactersSubscriber,
		PostInitializeSubscriber,
		EditRelicsSubscriber,
		EditCardsSubscriber,
		EditStringsSubscriber,
		OnCardUseSubscriber,
		EditKeywordsSubscriber,
		OnPowersModifiedSubscriber,
		PostDrawSubscriber,
		PostEnergyRechargeSubscriber,
		OnPlayerLoseBlockSubscriber,
		OnPlayerDamagedSubscriber,
		OnStartBattleSubscriber,
		OnPlayerTurnStartSubscriber,
		PostPlayerUpdateSubscriber,
		RenderSubscriber,
		AddAudioSubscriber,
		CustomSavable<AliceSaveData> {
	public static final String SIMPLE_NAME = AliceMargatroidMod.class.getSimpleName();
	
	public static final Logger logger = LogManager.getLogger(AliceMargatroidMod.class.getName());
	
//	private static final String MOD_BADGE = "img/UI/badge.png";
	
	private static String getCCPath(String s) {
		return "AliceMargatroidMod/img/512/" + s + ".png";
	}
	
	private static String getCCPortraitPath(String s) {
		return "AliceMargatroidMod/img/1024/" + s + ".png";
	}
	
	//card backgrounds
	private static final String ATTACK_CC = getCCPath("attack");
	private static final String SKILL_CC = getCCPath("skill");
	private static final String POWER_CC = getCCPath("power");
	private static final String ENERGY_ORB_CC = getCCPath("orb");
	
	private static final String ATTACK_CC_PORTRAIT = getCCPortraitPath("attack");
	private static final String SKILL_CC_PORTRAIT = getCCPortraitPath("skill");
	private static final String POWER_CC_PORTRAIT = getCCPortraitPath("power");
	private static final String ENERGY_ORB_CC_PORTRAIT = getCCPortraitPath("orb");
	
	public static final Color ALICE_IMPRESSION_COLOR = CardHelper.getColor(255,215,0);
	public static final Color ALICE_PUPPETEER_FLAVOR = CardHelper.getColor(250,250,210);
	public static final String CARD_ENERGY_ORB = "AliceMargatroidMod/img/UI/AliceMargatroid/energyOrb.png";
	
	private static final String CHARACTER_BUTTON = "AliceMargatroidMod/img/charSelect/AliceMargatroid/Button.png";
	private static final String ALICE_PORTRAIT = "AliceMargatroidMod/img/charSelect/AliceMargatroid/alice.png";
	
	private final ArrayList<Keyword> dollKeywords = new ArrayList<>();

	private static AliceSaveData saveData = new AliceSaveData();
	
	public AliceMargatroidMod() {
		BaseMod.subscribe(this);
		BaseMod.addSaveField(AliceMargatroidMod.SIMPLE_NAME + ":SaveData", this);

		logger.info("creating the color : ALICE_COLOR and ALICE_DERIVATION_COLOR");
		BaseMod.addColor(
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				ALICE_IMPRESSION_COLOR,
				ALICE_IMPRESSION_COLOR,
				ALICE_IMPRESSION_COLOR,
				ALICE_IMPRESSION_COLOR,
				ALICE_IMPRESSION_COLOR,
				ALICE_IMPRESSION_COLOR,
				ALICE_IMPRESSION_COLOR,
				ATTACK_CC,
				SKILL_CC,
				POWER_CC,
				ENERGY_ORB_CC,
				ATTACK_CC_PORTRAIT,
				SKILL_CC_PORTRAIT,
				POWER_CC_PORTRAIT,
				ENERGY_ORB_CC_PORTRAIT,
				CARD_ENERGY_ORB
		);
		
		if (!AliceHelper.isMarisaModAvailable()) {
			logger.info("creating the color : ALICE_MARISA_COLOR");
			Color starlight = CardHelper.getColor(0, 10, 125);
			
			BaseMod.addColor(
					AbstractCardEnum.ALICE_MARISA_COLOR,
					starlight,
					starlight,
					starlight,
					starlight,
					starlight,
					starlight,
					starlight,
					"AliceMargatroidMod/img/Marisa_PackMaster/512/attack.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/512/skill.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/512/power.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/512/orb.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/1024/attack.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/1024/skill.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/1024/power.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/1024/orb.png",
					"AliceMargatroidMod/img/Marisa_PackMaster/512/orbIcon.png"
			);
		}
	}
	
	public void receiveEditCharacters() {
		logger.info("begin editing characters");

        logger.info("add {}", AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS.toString());
		BaseMod.addCharacter(
				new AliceMargatroid("Alice Margatroid"),
				CHARACTER_BUTTON,
				ALICE_PORTRAIT,
				AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS
		);
		logger.info("done editing characters");
	}
	
	public void receiveEditRelics() {
		logger.info("Begin editing relics.");
		
		BaseMod.addRelicToCustomPool(
				new AlicesGrimoire(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new SuspiciousCard(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new AlicesDarkGrimoire(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new SwordOfLight_Supernova(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new StringRing(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new ColorContacts(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new SacrificialDoll(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelicToCustomPool(
				new MagicPotion(),
				AbstractCardEnum.ALICE_MARGATROID_COLOR
		);
		BaseMod.addRelic(
				new ShanghaiDollRelic(),
				RelicType.SHARED
		);
		BaseMod.addRelic(
				new BlackTeaRelic(),
				RelicType.SHARED
		);
		
		logger.info("Relics editing finished.");
	}
	
	public void receiveEditCards() {
		this.loadCustomIcons();

		this.loadVariables();
		
		logger.info("starting editing cards");

		new AutoAdd("AliceMargatroidMod")
				.packageFilter("me.antileaf.alice.cards.alice")
				.setDefaultSeen(true)
				.cards();

		new AutoAdd("AliceMargatroidMod")
				.packageFilter("me.antileaf.alice.cards.colorless")
				.setDefaultSeen(true)
				.cards();

		if (!AliceHelper.isMarisaModAvailable()) {
			new AutoAdd("AliceMargatroidMod")
					.packageFilter("me.antileaf.alice.cards.marisa")
					.setDefaultSeen(true)
					.cards();
		}
		
		logger.info("done editing cards");
	}

	@SuppressWarnings("unused")
	public static void initialize() {
		new AliceMargatroidMod();
		AliceConfigHelper.loadConfig();
	}
	
	private static String loadJson(String jsonPath) {
		return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
	}
	
	@Override
	public void receiveEditKeywords() {
		logger.info("Setting up custom keywords");

		String keywordsPath = AliceHelper.getLocalizationFilePath("keywords");

		Gson gson = new Gson();
		Keyword[] keywords = gson.fromJson(loadJson(keywordsPath), Keyword[].class);
		for (Keyword key : keywords) {
			if (Arrays.asList(AbstractDoll.dollClasses).contains(key.NAMES[0])) {
				this.dollKeywords.add(key);
				continue;
			}

            logger.info("Loading keyword : {}", key.NAMES[0]);
			BaseMod.addKeyword("alicemargatroid", key.NAMES[0], key.NAMES, key.DESCRIPTION);
		}
		
		logger.info("Loading doll keywords");
		AliceKeywordsHelper.addDollKeywords(this.dollKeywords);
		
		logger.info("Keywords setting finished.");
	}
	
	@Override
	public void receiveEditStrings() {
		logger.info("start editing strings");

		AliceHelper.loadCustomStrings(CharacterStrings.class, "character");
		AliceHelper.loadCustomStrings(RelicStrings.class, "relics");
		AliceHelper.loadCustomStrings(CardStrings.class, "cards");
		AliceHelper.loadCustomStrings(PowerStrings.class, "powers");
		AliceHelper.loadCustomStrings(PotionStrings.class, "potions");
//		AliceSpireKit.loadCustomStrings(OrbStrings.class, "dolls");
		AliceHelper.loadCustomStrings(MonsterStrings.class, "monsters");
		AliceHelper.loadCustomStrings(EventStrings.class, "events");
		AliceHelper.loadCustomStrings(UIStrings.class, "ui");
		AliceHelper.loadCustomStrings(TutorialStrings.class, "tutorial");
		AliceHelper.loadCustomStrings(ScoreBonusStrings.class, "score");

		AliceHelper.loadCustomStrings(CardStrings.class, "cards_loli");
		
		logger.info("Loading doll strings...");
		AliceDollStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("dolls"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceDollStrings>>() {}).getType()));
		
		logger.info("Loading card modifier strings...");
		AliceCardModifierStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("cardmodifier"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceCardModifierStrings>>() {}).getType()));

		logger.info("Loading card target strings...");
		AliceTargetIconStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("target"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType()));
		
		logger.info("Loading card cardnote strings...");
		AliceCardNoteStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("cardnote"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceCardNoteStrings>>() {}).getType()));
		
		logger.info("Loading card cardsign strings...");
		AliceCardSignStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("cards"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceCardSignStrings>>() {}).getType()));
		
		logger.info("Loading card language strings...");
		AliceLanguageStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("language"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType()));
		
		logger.info("Loading skin strings...");
		AliceSkinStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceHelper.getLocalizationFilePath("skin"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceSkinStrings>>() {}).getType()));

		logger.info("done editing strings");
	}
	
	@Override
	public void receiveAddAudio() {
		logger.info("Adding audio");
		AliceAudioMaster.init();
	}
	
	@Override
	public void receivePostExhaust(AbstractCard card) {
		// Auto-generated method stub
	}
	
	@Override
	public void receivePostBattle(AbstractRoom room) {
		DollManager.get().clearPostBattle();
		Bookmark.postBattle();
		BaseMod.MAX_HAND_SIZE = BaseMod.DEFAULT_MAX_HAND_SIZE;
//		SkinSelectScreen.inst.resetCurrentSkin();
//		AliceSpireKit.log("triggers receivePostBattle()");
	}
	
	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		DollManager.get().initPreBattle();
		Bookmark.onBattleStart();
		if (AbstractDungeon.player instanceof AliceMargatroid &&
				AliceConfigHelper.shouldOpenTutorial()) {
			AliceTutorialHelper.openTutorial();
			AliceConfigHelper.setShouldOpenTutorial(false);
			AliceConfigHelper.save();
		}
	}
	
	@Override
	public void receiveCardUsed(AbstractCard c) {
//		DollManager.get().debug();
		logger.debug("receive card used, clearing doll actions");
		DollManager.get().clearDollActions();
	}
	
	@Override
	public void receivePostEnergyRecharge() {
		DollManager.get().postEnergyRecharge();
		
		for (AbstractCard card : AbstractDungeon.player.hand.group) {
			if (card instanceof AbstractAliceCard)
				((AbstractAliceCard) card).aliceTriggerAtStartOfTurn();
		}
	}
	
	@Override
	public void receivePowersModified() {
		DollManager.get().applyPowers();
	}
	
	@Override
	public void receivePostDungeonInitialize() {
//		SkinSelectScreen.inst.resetCurrentSkin();
	}

	@Override
	public void receiveStartGame() {
		for (AbstractRelic r : AbstractDungeon.player.relics) {
			if (r instanceof ShanghaiDollRelic)
				((ShanghaiDollRelic) r).checkNameAndFlavor();
			else if (r instanceof BlackTeaRelic)
				((BlackTeaRelic) r).checkFlavor();
		}
	}
	
	@Override
	public void receivePostDraw(AbstractCard card) {
	}
	
	@Override
	public void receivePostInitialize() {
		logger.debug("AliceMargatroidMod.receivePostInitialize");
//		postInitialize = true;
		
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL, new DollTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_EMPTY_SLOT, new DollOrEmptySlotTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE, new DollOrEmptySlotOrNoneTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_ENEMY, new DollOrEnemyTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_ENEMY, new DollOrEmptySlotOrEnemyTargeting());
		CustomTargeting.registerCustomTargeting(CardTargetEnum.DOLL_OR_NONE, new DollOrNoneTargeting());
		
		BaseMod.addPotion(WeavingPotion.class, Color.YELLOW, Color.GOLD, Color.CLEAR, WeavingPotion.ID,
				AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS);
		BaseMod.addPotion(ConcentrationPotion.class, Color.ROYAL, Color.BLUE, Color.CLEAR, ConcentrationPotion.ID,
				AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS);
//		BaseMod.addPotion(BottledDoll.class, Color.GOLD, Color.GOLDENROD, Color.CLEAR, "BottledDoll",
//				AliceMargatroidModClassEnum.ALICE_MARGATROID);
		
		ModPanel settingsPanel = AliceConfigHelper.createConfigPanel();
		BaseMod.registerModBadge(
				ImageMaster.loadImage("AliceMargatroidMod/img/UI/badge.png"),
				"Alice Margatroid",
				"antileaf, Little Wolf",
				"",
				settingsPanel
		);
		
		AliceImageMaster.loadImages();
		
		BaseMod.addEvent(
				new AddEventParams.Builder(LilyOfTheValleyFlowerField.ID, LilyOfTheValleyFlowerField.class)
						.playerClass(AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS)
						.eventType(EventUtils.EventType.NORMAL)
						.create()
		);

		BaseMod.addEvent(
				new AddEventParams.Builder(PuppeteersHouse.ID, PuppeteersHouse.class)
						.dungeonID(TheCity.ID)
						.bonusCondition(() -> AbstractDungeon.player.hasRelic(ShanghaiDollRelic.ID))
						.eventType(EventUtils.EventType.NORMAL)
						.create()
		);

		CardBorderGlowManager.addGlowInfo(new UnlockMysticPower.UnlockMysticGlowInfo());
		
		logger.info("Initializing skin select screen...");
		SkinSelectScreen.init();

//		PuppeteersHouse.AliceCardReward.loadIcon();
//		BaseMod.registerCustomReward(
//				PuppeteersHouse.AliceCardReward.Enums.ALICE_CARD_REWARD,
//				(rewardSave) -> null,
//				(rewardSave) -> null
//		);

//		AliceSpireKit.logger.info("Adding ShanghaiDoll (as monster) to the game...");
//		BaseMod.addMonster(
//				"ShanghaiDoll",
//				ShanghaiDollAsMonster.monsterStrings.NAME,
//				() -> new ShanghaiDollAsMonster()
//		);
		
//		DEPRECATEDWitchsTeaParty.updateAll();

		CreateDoll.registerLoopPreview();

		if (AliceHelper.isRandomPredictionModAvailable())
			(new PredictionInitializer()).initialize();

		SignatureHelper.noDebuggingPrefix("AliceMargatroid:");

		if (!AliceConfigHelper.hasSignatureChecked()) {
			for (AbstractCard card : CardLibrary.getCardList(LibraryTypeEnum.ALICE_MARGATROID_COLOR))
				if (card instanceof AbstractAliceCard) {
					AbstractAliceCard ac = (AbstractAliceCard) card;

					if (ac.hasSignature && !SignatureHelper.isUnlocked(ac.cardID) &&
							AliceConfigHelper.isOldVersionSignatureUnlocked(ac.cardID)) {
						logger.info("Auto unlocking card : {}", ac);
						SignatureHelper.unlock(ac.cardID, true);
					}
				}

			AliceConfigHelper.setSignatureChecked(true);
		}

		SignatureHelper.registerEasyUnlock(new AliceEasyUnlock());
	}
	
	@Override
	public void receiveOnPlayerTurnStart() {
//		monstersDestroyedFranceDoll.clear();
		DollManager.getInstance(AbstractDungeon.player).onStartOfTurn();
	}
	
	@Override
	public void receivePostPlayerUpdate() {
		DollManager.get().update();
	}
	
//	HashSet<AbstractMonster> monstersDestroyedFranceDoll = new HashSet<>();
	
	public int receiveOnPlayerDamaged(int amount, DamageInfo damageInfo) {
		if (!AliceHelper.isInBattle())
			return amount;
		
		if (damageInfo.type == DamageInfo.DamageType.NORMAL) {
			if (!(damageInfo.owner instanceof AbstractMonster))
				return amount;
			
			AbstractMonster monster = (AbstractMonster) damageInfo.owner;
			if (!DollManager.get().damageTarget.containsKey(monster)) {
				AliceHelper.log("AliceMargatroidMod.receiveOnPlayerDamaged",
						"damageTarget does not contain " + monster.name);
				return amount;
			}
			
			int index = DollManager.get().damageTarget.get(monster);
			AbstractDoll doll = DollManager.get().getDolls().get(index);
			if (doll instanceof EmptyDollSlot)
				return amount;

			DollMechanicsPatch.DamageInfoField.blockedByDoll.put(damageInfo, true);
			
			int remaining = doll.onPlayerDamaged(amount);
			boolean destroyed = DollManager.get().dollTakesDamage(doll, amount - remaining);
			return remaining;
		}
		
//		else if (AbstractDungeon.player.hasPower(UsokaePower.POWER_ID)) {
//			int remaining = amount;
//			for (int i = DollManager.MAX_DOLL_SLOTS - 1; i >= 0; i--) {
//				AbstractDoll doll = DollManager.get().getDolls().get(i);
//				if (!(doll instanceof EmptyDollSlot)) {
//					int tmp = Math.min(remaining, doll.block);
//					remaining -= tmp;
//					doll.block -= tmp;
//				}
//
//				if (remaining == 0)
//					break;
//			}
//
//			if (remaining < amount)
//				AbstractDungeon.player.getPower(UsokaePower.POWER_ID).flash();
//			return remaining;
//		}
		
		return amount;
	}
	
	public int receiveOnPlayerLoseBlock(int amount) {
//		DollManager.get().updatePreservedBlock();
		logger.info("AliceMargatroidMod.receiveOnPlayerLoseBlock: amount = {}", amount);
		DollManager.get().startOfTurnClearBlock(amount);
		int preserve = DollManager.get().getPreservedBlock();
		
//		DollManager.get().startOfTurnResetHouraiPassiveAmount();
		
		return Integer.min(amount, AbstractDungeon.player.currentBlock - preserve);
	}
	
	public void receiveRender(SpriteBatch sb) {
//		DollManager.get().render(sb);
	}

	private void loadCustomIcons() {
		CustomIconHelper.addCustomIcon(new AliceCustomIcon("Alice", "alice_icon"));
		CustomIconHelper.addCustomIcon(new AliceCustomIcon("Doll", "doll_icon"));
		CustomIconHelper.addCustomIcon(new AliceCustomIcon("Slot", "slot_icon"));
		CustomIconHelper.addCustomIcon(new AliceCustomIcon("Enemy", "enemy_icon"));
		CustomIconHelper.addCustomIcon(new AliceCustomIcon("None", "none_icon"));
		CustomIconHelper.addCustomIcon(new AliceCustomIcon("Info", "info"));
	}

	private void loadVariables() {
		// 注册次要 damage, block, magicNumber
		// 虽然只有三个变量，但是 AutoAdd 很酷！
		new AutoAdd("AliceMargatroidMod")
				.packageFilter("me.antileaf.alice.cards.utils.variables")
				.any(DynamicVariable.class, (info, variable) -> {
					logger.info("Adding dynamic variable : {}", variable.getClass().getSimpleName());
					BaseMod.addDynamicVariable(variable);
				});
	}

	private static String serialize(AliceSaveData saveData) {
		return saveData == null ? "null" : "Shanghai: " + saveData.getHasTriggeredShanghaiDollEvent();
	}

	@Override
	public AliceSaveData onSave() {
		logger.info("AliceMargatroidMod.onSave(): {}", serialize(saveData));
		return saveData;
	}

	@Override
	public void onLoad(AliceSaveData saveData) {
		logger.info("AliceMargatroidMod.onLoad(): {}", serialize(saveData));
		if (saveData != null)
			AliceMargatroidMod.saveData = saveData;
	}

	public static AliceSaveData getSaveData() {
		return saveData;
	}
}
