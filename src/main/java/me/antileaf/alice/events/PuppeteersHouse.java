package me.antileaf.alice.events;

import ThMod.patches.ThModClassEnum;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomReward;
import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import me.antileaf.alice.cards.alice.*;
import me.antileaf.alice.patches.enums.AbstractPlayerEnum;
import me.antileaf.alice.relics.BlackTeaRelic;
import me.antileaf.alice.relics.ShanghaiDollRelic;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class PuppeteersHouse extends PhasedEvent {
	public static final String SIMPLE_NAME = PuppeteersHouse.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	public static final EventStrings touhouStrings = CardCrawlGame.languagePack.getEventString(ID + "_Greetings");

	private static final String INTRO = "INTRO", CONTINUE = "CONTINUE", CHOICE = "CHOICE",
			RETURN = "RETURN", REFUSE = "REFUSE",
			CARD = "CARD", TEA = "TEA", LEAVE = "LEAVE";

	private static enum CharType {
		NORMAL, ALICE, MARISA, TOUHOU
	}

	// TODO: Add Touhou characters
	public static final String[] TOUHOU_CHARS = new String[]{"QWQ_PLACE_HOLDER"};

	public static final String[] COMMON_CARDS = new String[]{
			Pause.ID,
			Hail.ID,
			SpectreMystery.ID,
			TheSouthernCross.ID
	};

	public static final String[] UNCOMMON_CARDS = new String[]{
			FriendsHelp.ID,
			Revelation.ID,
			DevilryLight.ID,
			Lantern.ID,
			IllusoryMoon.ID,
			Dessert.ID,
			BlackTea.ID,
			Geyser.ID,
			Ultimatum.ID,
			MagicianRay.ID
	};

	public static final String[] RARE_CARDS = new String[]{
			Perihelion.ID,
			Bookmark.ID,
			CollapsingWorlds.ID,
			SurpriseSpring.ID,
			PolarNight.ID,
			AliceInWonderland.ID,
			MaliceSpark.ID,
			PrincessForm.ID
	};

	public static final int CARD_COUNT = 3;
	public static final int CARD_COUNT_HIGH_ASC = 2;
	public static final int HEAL_HIGH_ASC = 7;

	private final CharType charType;
	private int touhouIndex = -1;

	public int getCardCount() {
		return AbstractDungeon.ascensionLevel >= 15 ? CARD_COUNT_HIGH_ASC : CARD_COUNT;
	}

	public int getHeal() {
		return HEAL_HIGH_ASC;
	}

	public ArrayList<AbstractCard> getRewardCards() {
		ArrayList<AbstractCard> rewards = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			boolean ok = false;
			while (!ok) {
				int roll = AbstractDungeon.cardRng.random(99);
				String id;

				if (this.charType == CharType.ALICE) {
					if (roll < 55)
						id = AbstractDungeon.getCard(AbstractCard.CardRarity.COMMON).cardID;
					else if (roll < 85)
						id = AbstractDungeon.getCard(AbstractCard.CardRarity.UNCOMMON).cardID;
					else
						id = AbstractDungeon.getCard(AbstractCard.CardRarity.RARE).cardID;
				}
				else {
					if (roll < 55)
						id = COMMON_CARDS[AbstractDungeon.cardRng.random(COMMON_CARDS.length - 1)];
					else if (roll < 85)
						id = UNCOMMON_CARDS[AbstractDungeon.cardRng.random(UNCOMMON_CARDS.length - 1)];
					else
						id = RARE_CARDS[AbstractDungeon.cardRng.random(RARE_CARDS.length - 1)];
				}

				if (rewards.stream().noneMatch(c -> c.cardID.equals(id))) {
					rewards.add(CardLibrary.getCard(id).makeCopy());
					ok = true;
				}
			}
		}

		if (this.charType == CharType.ALICE)
			rewards.forEach(AbstractCard::upgrade);

		return rewards;
	}

	public String getIntroDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[0];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[8];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[15];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[23];
		else
			return "ERROR";
	}

	public String getKnockOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[0];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[8];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[15];
		else
			return "ERROR";
	}

	public String getContinueDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[1];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[9];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[16];
		else if (this.charType == CharType.TOUHOU)
			return String.format(eventStrings.DESCRIPTIONS[24], touhouStrings.DESCRIPTIONS[this.touhouIndex]);
		else
			return "ERROR";
	}

	public String getContinueOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[1];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[9];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[16];
		else
			return "ERROR";
	}

	public String getChoiceDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[2];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[10];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[17];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[25];
		else
			return "ERROR";
	}

	public String getReturnOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[2];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[10];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[17];
		else
			return "ERROR";
	}

	public String getRefuseOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[3];
		else if (this.charType == CharType.ALICE)
			return ""; // 没有这个选项
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[18];
		else
			return "ERROR";
	}

	public String getReturnDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[3];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[11];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[18];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[26];
		else
			return "ERROR";
	}

	public String getCardOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[4];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[11];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[19];
		else
			return "ERROR";
	}

	public String getTeaOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[5];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[12];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[20];
		else
			return "ERROR";
	}

	public String getRefuseDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[4];
		else if (this.charType == CharType.ALICE)
			return ""; // 没有这个选项
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[19];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[27];
		else
			return "ERROR";
	}

	public String getByeByeOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[6];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[13];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[21];
		else
			return "ERROR";
	}

	public String getCardDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[5];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[12];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[20];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[28];
		else
			return "ERROR";
	}

	public String getTeaDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[6];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[13];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[21];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[29];
		else
			return "ERROR";
	}

	public String getLeaveDesc() {
		if (this.charType == CharType.NORMAL)
			return eventStrings.DESCRIPTIONS[7];
		else if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[14];
		else if (this.charType == CharType.MARISA)
			return eventStrings.DESCRIPTIONS[22];
		else if (this.charType == CharType.TOUHOU)
			return eventStrings.DESCRIPTIONS[30];
		else
			return "ERROR";
	}

	public String getLeaveOption() {
		if (this.charType == CharType.NORMAL || this.charType == CharType.TOUHOU)
			return eventStrings.OPTIONS[7];
		else if (this.charType == CharType.ALICE)
			return eventStrings.OPTIONS[14];
		else if (this.charType == CharType.MARISA)
			return eventStrings.OPTIONS[22];
		else
			return "ERROR";
	}

	public String getCardRewardTitle() {
		if (this.charType == CharType.ALICE)
			return eventStrings.DESCRIPTIONS[32];
		else
			return eventStrings.DESCRIPTIONS[31];
	}

	public String getTeaRewardTitle() {
		return eventStrings.DESCRIPTIONS[34];
	}

	public void changeImg(String name) {
		this.imageEventText.loadImage(AliceHelper.getEventImgFilePath(SIMPLE_NAME + "/" + name));
		ReflectionHacks.setPrivate(this.imageEventText, GenericEventDialog.class, "imgColor",
				new Color(1.0F, 1.0F, 1.0F, 0.5F));
	}

	public PuppeteersHouse() {
		super(ID, eventStrings.NAME, AliceHelper.getEventImgFilePath(SIMPLE_NAME + "/outside"));

		if (AbstractDungeon.player.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS)
			this.charType = CharType.ALICE;
		else if (AliceHelper.isMarisaModAvailable() &&
				AbstractDungeon.player.chosenClass == ThModClassEnum.MARISA)
			this.charType = CharType.MARISA;
		else if (Arrays.stream(TOUHOU_CHARS).anyMatch(s -> s.equals(AbstractDungeon.player.chosenClass.name()))) {
			this.charType = CharType.TOUHOU;
			this.touhouIndex = Arrays.asList(TOUHOU_CHARS).indexOf(AbstractDungeon.player.chosenClass.name());
		}
		else
			this.charType = CharType.NORMAL;

		this.registerPhase(INTRO, new TextPhase(this.getIntroDesc())
				.addOption(this.getKnockOption(), (i) -> {
					this.transitionKey(CONTINUE);

					if (this.charType == CharType.ALICE)
						this.changeImg("lolice");
					else if (this.charType == CharType.MARISA)
						this.changeImg("marisa");
					else
						this.changeImg("alice");
				}));

		this.registerPhase(CONTINUE, new TextPhase(this.getContinueDesc())
				.addOption(this.getContinueOption(), (i) -> this.transitionKey(CHOICE)));

		TextPhase choicePhase = new TextPhase(this.getChoiceDesc())
				.addOption(this.getReturnOption(), new ShanghaiDollRelic(), (i) -> {
					this.transitionKey(RETURN);
					AbstractDungeon.player.loseRelic(ShanghaiDollRelic.ID);

					AliceHelper.getSaveData().setReturnedShanghaiDoll(true);
					AliceHelper.logger.info("You returned Shanghai Doll to Alice. Thank you!");
				});
		if (this.charType != CharType.ALICE)
			choicePhase.addOption(this.getRefuseOption(), (i) -> this.transitionKey(REFUSE));
		this.registerPhase(CHOICE, choicePhase);

		this.registerPhase(RETURN, new TextPhase(this.getReturnDesc())
				.addOption(String.format(this.getCardOption(), this.getCardCount()), (i) -> {
					AbstractDungeon.getCurrRoom().rewards.clear();
					for (int j = 0; j < this.getCardCount(); j++)
						AbstractDungeon.getCurrRoom().rewards.add(new AliceCardReward(this.getRewardCards()));
					AbstractDungeon.combatRewardScreen.clear();
					AbstractDungeon.combatRewardScreen.open(this.getCardRewardTitle());
					this.transitionKey(CARD);
				})
				.addOption(String.format(this.getTeaOption(), this.getHeal(), this.getHeal()),
						new BlackTeaRelic(), (i) -> {
					AbstractDungeon.getCurrRoom().rewards.clear();
					AbstractDungeon.getCurrRoom().addRelicToRewards(new BlackTeaRelic());
					AbstractDungeon.combatRewardScreen.clear();
					AbstractDungeon.combatRewardScreen.open(this.getTeaRewardTitle());
					this.transitionKey(TEA);
				}));

		this.registerPhase(REFUSE, new TextPhase(this.getRefuseDesc())
				.addOption(this.getByeByeOption(), (i) -> {
					this.transitionKey(LEAVE);
					this.changeImg("outside");
				}));

		this.registerPhase(CARD, new TextPhase(this.getCardDesc())
				.addOption(this.getByeByeOption(), (i) -> {
					this.transitionKey(LEAVE);
					this.changeImg("outside");
				}));

		this.registerPhase(TEA, new TextPhase(this.getTeaDesc())
				.addOption(this.getByeByeOption(), (i) -> {
					this.transitionKey(LEAVE);
					this.changeImg("outside");
				}));

		this.registerPhase(LEAVE, new TextPhase(this.getLeaveDesc())
				.addOption(this.getLeaveOption(), (i) -> this.openMap()));

		this.noCardsInRewards = true;
		this.transitionKey(INTRO);
	}

	public static class AliceCardReward extends CustomReward {
		public static Texture ICON = new Texture(AliceHelper.getImgFilePath("UI", "AliceCardReward"));

		ArrayList<AbstractCard> aliceCards;

		public AliceCardReward(ArrayList<AbstractCard> cards) {
			super(
					ICON,
					eventStrings.DESCRIPTIONS[33],
					Enums.ALICE_CARD_REWARD
			);

			assert Gdx.files.internal(AliceHelper.getImgFilePath("UI", "AliceCardReward")).exists();

			this.aliceCards = cards;
		}

		@Override
		public boolean claimReward() {
			AbstractDungeon.cardRewardScreen.open(this.aliceCards, this, eventStrings.DESCRIPTIONS[33]);
			AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
			return false;
		}

		public static class Enums {
			@SpireEnum
			public static RewardItem.RewardType ALICE_CARD_REWARD;
		}
	}
}
