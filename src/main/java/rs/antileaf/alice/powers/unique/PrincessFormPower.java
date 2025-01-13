package rs.antileaf.alice.powers.unique;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.cardmodifier.PhantomCardModifier;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;

public class PrincessFormPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = PrincessFormPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public PrincessFormPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 5;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.costForTurn == 1 && !CardModifierManager.hasModifier(card, PhantomCardModifier.ID)) {
			this.flash();

			AbstractCard copy = card.makeStatEquivalentCopy();
			CardModifierManager.addModifier(copy, new PhantomCardModifier());

			this.addToBot(new MakeTempCardInHandAction(copy, this.amount));
		}
	}
}
