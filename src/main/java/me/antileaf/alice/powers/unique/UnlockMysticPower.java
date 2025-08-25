package me.antileaf.alice.powers.unique;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TreeSet;

public class UnlockMysticPower extends AbstractAlicePower {
	private static final Logger logger = LogManager.getLogger(UnlockMysticPower.class);

	public static final String SIMPLE_NAME = UnlockMysticPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	TreeSet<String> triggered = new TreeSet<>();

	public UnlockMysticPower() {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.flash();
		this.triggered.clear();
		this.updateDescription();
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];

		if (!this.triggered.isEmpty())
			this.description += " NL " + powerStrings.DESCRIPTIONS[1] +
					this.triggered.stream()
							.map(id -> {
								AbstractCard c = CardLibrary.getCard(id);
								if (c == null) {
									logger.warn("Card with ID {} not found in CardLibrary", id);
									return (AliceHelper.getLangShort().equals("zhs") ?
											"未知卡牌" : "Unknown Card") + " (" + id + ")";
								}

								return c.name;
							})
							.reduce((a, b) -> a + " NL " + b)
							.orElse("");
	}

	private static boolean checkIsCommandCard(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard ac = (AbstractAliceCard) card;

			return ac.isCommandCard ||
					(ac.dollTarget && AbstractDungeon.player.hasPower(ForbiddenMagicPower.POWER_ID));
		}

		return false;
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard ac = (AbstractAliceCard) card;

//			if (ac.isCommandCard)
//				logger.info("Command Card: {}, target = {}", ac.cardID, ac.getTargetedDoll());

			if (checkIsCommandCard(card) && ac.getTargetedDoll() != null) {
//				logger.info("triggered command effect");

				if (!this.triggered.contains(ac.cardID)) {
					this.flash();

					this.addToBot(new GainEnergyAction(1));
					this.addToBot(new DrawCardAction(1));

					this.triggered.add(ac.cardID);
					this.updateDescription();
				}
			}
		}
	}

	public static class UnlockMysticGlowInfo extends CardBorderGlowManager.GlowInfo {
		@Override
		public String glowID() {
			return AliceHelper.makeID(UnlockMysticGlowInfo.class.getSimpleName());
		}

		@Override
		public boolean test(AbstractCard card) {
			return AbstractDungeon.player != null && AliceHelper.isInBattle() &&
					checkIsCommandCard(card) &&
					AbstractDungeon.player.hasPower(UnlockMysticPower.POWER_ID) &&
					!((UnlockMysticPower) AbstractDungeon.player.getPower(UnlockMysticPower.POWER_ID))
							.triggered.contains(card.cardID);
		}

		@Override
		public Color getColor(AbstractCard card) {
			return Color.PINK.cpy();
		}
	}
}
