package rs.antileaf.alice;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
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
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.Marisa.Alice6A;
import rs.antileaf.alice.cards.Marisa.AliceAsteroidBelt;
import rs.antileaf.alice.cards.Marisa.AliceDoubleSpark;
import rs.antileaf.alice.cards.Marisa.AliceSpark;
import rs.antileaf.alice.cards.alice.Thread;
import rs.antileaf.alice.cards.alice.*;
import rs.antileaf.alice.cards.colorless.PoisonousSweet;
import rs.antileaf.alice.cards.derivations.CreateDoll;
import rs.antileaf.alice.cards.derivations.CreateSusan;
import rs.antileaf.alice.cards.derivations.Retrace;
import rs.antileaf.alice.cards.loli.*;
import rs.antileaf.alice.characters.AliceMargatroid;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.events.LilyOfTheValleyFlowerField;
import rs.antileaf.alice.events.PuppeteersHouse;
import rs.antileaf.alice.icon.AliceCustomIcon;
import rs.antileaf.alice.loli.AliceLoliManager;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.potions.ConcentrationPotion;
import rs.antileaf.alice.potions.WeavingPotion;
import rs.antileaf.alice.relics.*;
import rs.antileaf.alice.save.AliceSaveData;
import rs.antileaf.alice.strings.*;
import rs.antileaf.alice.targeting.handlers.*;
import rs.antileaf.alice.ui.SkinSelectScreen;
import rs.antileaf.alice.utils.*;
import rs.antileaf.alice.variable.AliceSecondaryDamageVariable;
import rs.antileaf.alice.variable.AliceSecondaryMagicNumberVariable;

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
	private static final String ATTACK_CC = getCCPath("bg_attack_s");
	private static final String SKILL_CC = getCCPath("bg_skill_s");
	private static final String POWER_CC = getCCPath("bg_power_s");
	private static final String ENERGY_ORB_CC = getCCPath("cardOrb_s");
	
	private static final String ATTACK_CC_PORTRAIT = getCCPortraitPath("bg_attack");
	private static final String SKILL_CC_PORTRAIT = getCCPortraitPath("bg_skill");
	private static final String POWER_CC_PORTRAIT = getCCPortraitPath("bg_power");
	private static final String ENERGY_ORB_CC_PORTRAIT = getCCPortraitPath("cardOrb");
	
	public static final Color ALICE_PUPPETEER = CardHelper.getColor(255,215,0);
	public static final Color ALICE_PUPPETEER_FLAVOR = CardHelper.getColor(250,250,210);
	public static final String CARD_ENERGY_ORB = "AliceMargatroidMod/img/UI/AliceMargatroid/energyOrb.png";
	
	private static final String CHARACTER_BUTTON = "AliceMargatroidMod/img/charSelect/AliceMargatroid/Button.png";
	private static final String ALICE_PORTRAIT = "AliceMargatroidMod/img/charSelect/AliceMargatroid/alice.png";
	
	private final ArrayList<AbstractCard> cardsToAdd = new ArrayList<>();
	//private ArrayList<AbstractRelic> relicsToAdd = new ArrayList<>();
	
	private final ArrayList<Keyword> dollKeywords = new ArrayList<>();
	
//	public static boolean postInitialize = false;

	private static AliceSaveData saveData = new AliceSaveData();
	
	public AliceMargatroidMod() {
		BaseMod.subscribe(this);
		BaseMod.addSaveField(AliceMargatroidMod.SIMPLE_NAME + ":SaveData", this);

		logger.info("creating the color : ALICE_COLOR and ALICE_DERIVATION_COLOR");
		BaseMod.addColor(
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
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
		BaseMod.addColor(
				AbstractCardEnum.ALICE_MARGATROID_DERIVATION_COLOR,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
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
		BaseMod.addColor( // TODO: 给它们换一套风格
				AbstractCardEnum.ALICE_LOLI_COLOR,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
				ALICE_PUPPETEER,
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
		
		if (!AliceSpireKit.isMarisaModAvailable()) {
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
		
		this.loadCardsToAdd();
		
		logger.info("adding cards for ALICE_MARGATROID");
		
		for (AbstractCard card : cardsToAdd) {
            logger.info("Adding card : {}", card.name);
			BaseMod.addCard(card);
			
			UnlockTracker.unlockCard(card.cardID);
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

		String keywordsPath = AliceSpireKit.getLocalizationFilePath("keywords");

		Gson gson = new Gson();
		Keyword[] keywords = gson.fromJson(loadJson(keywordsPath), Keyword[].class);
		for (Keyword key : keywords) {
			if (Arrays.asList(AbstractDoll.dollClasses).contains(key.NAMES[0])) {
				this.dollKeywords.add(key);
				continue;
			}

            logger.info("Loading keyword : {}", key.NAMES[0]);
			BaseMod.addKeyword(AliceMargatroidMod.SIMPLE_NAME.toLowerCase(), key.NAMES[0], key.NAMES, key.DESCRIPTION);
		}
		
		logger.info("Loading doll keywords");
		AliceKeywordsHelper.addDollKeywords(this.dollKeywords);
		
		logger.info("Keywords setting finished.");
	}
	
	@Override
	public void receiveEditStrings() {
		logger.info("start editing strings");

		AliceSpireKit.loadCustomStrings(CharacterStrings.class, "character");
		AliceSpireKit.loadCustomStrings(RelicStrings.class, "relics");
		AliceSpireKit.loadCustomStrings(CardStrings.class, "cards");
		AliceSpireKit.loadCustomStrings(PowerStrings.class, "powers");
		AliceSpireKit.loadCustomStrings(PotionStrings.class, "potions");
//		AliceSpireKit.loadCustomStrings(OrbStrings.class, "dolls");
		AliceSpireKit.loadCustomStrings(MonsterStrings.class, "monsters");
		AliceSpireKit.loadCustomStrings(EventStrings.class, "events");
		AliceSpireKit.loadCustomStrings(UIStrings.class, "ui");
		AliceSpireKit.loadCustomStrings(TutorialStrings.class, "tutorial");
		AliceSpireKit.loadCustomStrings(ScoreBonusStrings.class, "score");

		AliceSpireKit.loadCustomStrings(CardStrings.class, "cards_loli");
		
		logger.info("Loading doll strings...");
		AliceDollStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("dolls"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceDollStrings>>() {}).getType()));
		
		logger.info("Loading card modifier strings...");
		AliceCardModifierStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("cardmodifier"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceCardModifierStrings>>() {}).getType()));

		logger.info("Loading card target strings...");
		AliceTargetIconStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("target"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType()));
		
		logger.info("Loading card cardnote strings...");
		AliceCardNoteStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("cardnote"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceCardNoteStrings>>() {}).getType()));
		
		logger.info("Loading card cardsign strings...");
		AliceCardSignStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("cardsign"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, AliceCardSignStrings>>() {}).getType()));
		
		logger.info("Loading card language strings...");
		AliceLanguageStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("language"))
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType()));
		
		logger.info("Loading skin strings...");
		AliceSkinStrings.init((new Gson()).fromJson(
				Gdx.files.internal(AliceSpireKit.getLocalizationFilePath("skin"))
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
		Bookmark.clearCache();
		BaseMod.MAX_HAND_SIZE = BaseMod.DEFAULT_MAX_HAND_SIZE;
//		SkinSelectScreen.inst.resetCurrentSkin();
//		AliceSpireKit.log("triggers receivePostBattle()");
	}
	
	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		DollManager.get().initPreBattle();
		Bookmark.clearCache();
		if (AliceConfigHelper.shouldOpenTutorial()) {
			AliceTutorialHelper.openTutorial();
			AliceConfigHelper.setShouldOpenTutorial(false);
			AliceConfigHelper.save();
		}
	}
	
	@Override
	public void receiveCardUsed(AbstractCard c) {
		DollManager.get().debug();
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
		
		BaseMod.addPotion(WeavingPotion.class, Color.YELLOW, Color.GOLD.cpy(), Color.CLEAR, WeavingPotion.ID,
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
		
		AliceSpireKit.log("Initializing skin select screen...");
		SkinSelectScreen.init();

//		registerLoliCards();
		// TODO: 还没写好，后面如果真的要做萝莉形态的话再写

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
		
		WitchsTeaParty.updateAll();

		DollWorkshop.registerLoopPreview();
		CreateDoll.registerLoopPreview();
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
		if (!AliceSpireKit.isInBattle())
			return amount;
		
		if (damageInfo.type == DamageInfo.DamageType.NORMAL) {
			if (!(damageInfo.owner instanceof AbstractMonster))
				return amount;
			
			AbstractMonster monster = (AbstractMonster) damageInfo.owner;
			if (!DollManager.get().damageTarget.containsKey(monster)) {
				AliceSpireKit.log("AliceMargatroidMod.receiveOnPlayerDamaged",
						"damageTarget does not contain " + monster.name);
				return amount;
			}
			
			int index = DollManager.get().damageTarget.get(monster);
			AbstractDoll doll = DollManager.get().getDolls().get(index);
			if (doll instanceof EmptyDollSlot)
				return amount;
			
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
//		BaseMod.addDynamicVariable(new TempHPVariable());
		BaseMod.addDynamicVariable(new AliceSecondaryMagicNumberVariable());
		BaseMod.addDynamicVariable(new AliceSecondaryDamageVariable());
	}

	private void loadCardsToAdd() {
		this.cardsToAdd.clear();
		
		this.cardsToAdd.add(new Strike_AliceMargatroid());
		this.cardsToAdd.add(new Defend_AliceMargatroid());
		this.cardsToAdd.add(new DollPlacement());
		this.cardsToAdd.add(new Chant());
		
		this.cardsToAdd.add(new Thread());
		this.cardsToAdd.add(new DollCrusader());
		this.cardsToAdd.add(new LittleLegion());
		this.cardsToAdd.add(new ProtectiveSpell());
		this.cardsToAdd.add(new Lantern());
//		this.cardsToAdd.add(new DEPRECATEDEmeraldRay());
//		this.cardsToAdd.add(new MoonlightRay());
//		this.cardsToAdd.add(new KirisameMagicShop());
		this.cardsToAdd.add(new SurpriseSpring());
		this.cardsToAdd.add(new WarFlag());
		this.cardsToAdd.add(new Relay());
		this.cardsToAdd.add(new DollAmbush());
		this.cardsToAdd.add(new DollWar());
//		this.cardsToAdd.add(new DollJudge());
//		this.cardsToAdd.add(new SpiritualPower());
//		this.cardsToAdd.add(new FrostRay());
		this.cardsToAdd.add(new Perihelion());
		this.cardsToAdd.add(new Hail());
		this.cardsToAdd.add(new DollCremation());
		this.cardsToAdd.add(new SevenColoredPuppeteer());
		this.cardsToAdd.add(new Collector());
//		this.cardsToAdd.add(new Punishment());
		this.cardsToAdd.add(new MaliceSpark());
		this.cardsToAdd.add(new CallToDolls());
		this.cardsToAdd.add(new SnowSweeping());
		this.cardsToAdd.add(new FriendsHelp());
		this.cardsToAdd.add(new WillOWisp());
//		this.cardsToAdd.add(new MysteriousMirror());
		this.cardsToAdd.add(new ReturningDolls());
		this.cardsToAdd.add(new SPDoll());
//		this.cardsToAdd.add(new DEPRECATEDKick());
//		this.cardsToAdd.add(new DEPRECATEDInterlacedRay());
//		this.cardsToAdd.add(new DEPRECATEDFireplace());
//		this.cardsToAdd.add(new DEPRECATEDGuidingRay());
//		this.cardsToAdd.add(new DEPRECATEDRefraction());
		this.cardsToAdd.add(new DollLances());
//		this.cardsToAdd.add(new CutiePhalanx());
		this.cardsToAdd.add(new Ultimatum());
		this.cardsToAdd.add(new BlackTea());
//		this.cardsToAdd.add(new PokerTrick());
//		this.cardsToAdd.add(new MysteriousChallenger());
		this.cardsToAdd.add(new Barrier());
		this.cardsToAdd.add(new DollOfRoundTable());
//		this.cardsToAdd.add(new DEPRECATEDTyrant());
		this.cardsToAdd.add(new Dessert());
		this.cardsToAdd.add(new TheSetup());
		this.cardsToAdd.add(new Sale());
//		this.cardsToAdd.add(new DEPRECATEDThrow());
		this.cardsToAdd.add(new DollActivation());
//		this.cardsToAdd.add(new DEPRECATEDTheUnmovingGreatLibrary());
//		this.cardsToAdd.add(new VisitOfThreeFairies());
//		this.cardsToAdd.add(new ScatterTheWeak());
		this.cardsToAdd.add(new AliceInWonderland());
		this.cardsToAdd.add(new Pause());
//		this.cardsToAdd.add(new SealOfLight());
//		this.cardsToAdd.add(new DEPRECATEDTripwire());
		this.cardsToAdd.add(new DollMiraCeti());
		this.cardsToAdd.add(new Revelation());
//		this.cardsToAdd.add(new ExperimentalArmor());
		this.cardsToAdd.add(new ButterflyFlurry());
//		this.cardsToAdd.add(new WitchsTeaParty());
//		this.cardsToAdd.add(new DollOrchestra());
//		this.cardsToAdd.add(new ThePhantomOfTheGrandGuignol());
		this.cardsToAdd.add(new Housework());
		this.cardsToAdd.add(new Bookmark());
		this.cardsToAdd.add(new MaidensBunraku());
		this.cardsToAdd.add(new Usokae());
		this.cardsToAdd.add(new Tripwire());
		this.cardsToAdd.add(new DevilryLight());
		this.cardsToAdd.add(new RefreshingSpringWater());
		this.cardsToAdd.add(new DollArmy());
		this.cardsToAdd.add(new AliceGame());
//		this.cardsToAdd.add(new MagicConduit());
		this.cardsToAdd.add(new SeekerDoll());
		this.cardsToAdd.add(new ArtfulChanter());
		this.cardsToAdd.add(new TheSouthernCross());
		this.cardsToAdd.add(new Masterpiece());
		this.cardsToAdd.add(new MagicianRay());
		this.cardsToAdd.add(new LuminousShanghaiDoll());
		this.cardsToAdd.add(new IllusoryMoon());
		this.cardsToAdd.add(new DollMagic());
		this.cardsToAdd.add(new SeaOfSubconsciousness());
		this.cardsToAdd.add(new ReturnInanimateness());
		this.cardsToAdd.add(new SpectreMystery());
		this.cardsToAdd.add(new DollInSea());
		this.cardsToAdd.add(new ClawMachine());
		this.cardsToAdd.add(new DollWorkshop());
		
		this.cardsToAdd.add(new VivaciousShanghaiDoll());
		this.cardsToAdd.add(new QuietHouraiDoll());
		this.cardsToAdd.add(new MistyLondonDoll());
		this.cardsToAdd.add(new CharitableFranceDoll());
		this.cardsToAdd.add(new RedHairedNetherlandsDoll());
		this.cardsToAdd.add(new CharismaticOrleansDoll());
		this.cardsToAdd.add(new SpringKyotoDoll());

		this.cardsToAdd.add(new Retrace());
//		this.cardsToAdd.add(new MarisasPotion());
		this.cardsToAdd.add(new CreateSusan());
		this.cardsToAdd.add(new CreateDoll());
		
		this.cardsToAdd.add(new PoisonousSweet());
		
//		this.cardsToAdd.add(new CreateShanghaiDoll());
//		this.cardsToAdd.add(new CreateNetherlandsDoll());
//		this.cardsToAdd.add(new CreateHouraiDoll());
//		this.cardsToAdd.add(new CreateFranceDoll());
//		this.cardsToAdd.add(new CreateLondonDoll());
//		this.cardsToAdd.add(new CreateKyotoDoll());
//		this.cardsToAdd.add(new CreateOrleansDoll());
		
		if (!AliceSpireKit.isMarisaModAvailable()) {
			this.cardsToAdd.add(new AliceSpark());
			this.cardsToAdd.add(new AliceDoubleSpark());
			this.cardsToAdd.add(new AliceAsteroidBelt());
			this.cardsToAdd.add(new Alice6A());
		}

//		this.cardsToAdd.add(new Pause_Loli());
		this.cardsToAdd.add(new VivaciousShanghaiDoll_Loli());
		this.cardsToAdd.add(new QuietHouraiDoll_Loli());
		this.cardsToAdd.add(new SpringKyotoDoll_Loli());
		this.cardsToAdd.add(new RedHairedNetherlandsDoll_Loli());
		this.cardsToAdd.add(new CharitableFranceDoll_Loli());
		this.cardsToAdd.add(new MistyLondonDoll_Loli());
		this.cardsToAdd.add(new CharismaticOrleansDoll_Loli());
	}

	private void registerLoliCards() {
		AliceLoliManager.register(Pause_Loli.class);
		AliceLoliManager.register(VivaciousShanghaiDoll_Loli.class);
		AliceLoliManager.register(QuietHouraiDoll_Loli.class);
		AliceLoliManager.register(SpringKyotoDoll_Loli.class);
		AliceLoliManager.register(RedHairedNetherlandsDoll_Loli.class);
		AliceLoliManager.register(CharitableFranceDoll_Loli.class);
		AliceLoliManager.register(MistyLondonDoll_Loli.class);
		AliceLoliManager.register(CharismaticOrleansDoll_Loli.class);
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
