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

import java.util.TreeSet;

public class UnlockMysticPower extends AbstractAlicePower {
//	private static final Logger logger = LogManager.getLogger(UnlockMysticPower.class);

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
							.map(id -> CardLibrary.getCard(id).name)
							.reduce((a, b) -> a + " NL " + b)
							.orElse("");
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard ac = (AbstractAliceCard) card;

//			if (ac.isCommandCard)
//				logger.info("Command Card: {}, target = {}", ac.cardID, ac.getTargetedDoll());

			if (ac.isCommandCard && ac.getTargetedDoll() != null) {
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
			return card instanceof AbstractAliceCard && ((AbstractAliceCard) card).isCommandCard &&
					AbstractDungeon.player != null && AliceHelper.isInBattle() &&
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
