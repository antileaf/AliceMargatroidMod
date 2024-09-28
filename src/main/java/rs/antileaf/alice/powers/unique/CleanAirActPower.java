package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.powers.AbstractAlicePower;

public class CleanAirActPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = CleanAirActPower.class.getSimpleName();
	public static final String POWER_ID = SIMPLE_NAME;
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public CleanAirActPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		if (this.amount <= 1)
			this.description = powerStrings.DESCRIPTIONS[0];
		else
			this.description = String.format(powerStrings.DESCRIPTIONS[1], this.amount);
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (!isPlayer)
			return;

		boolean trigged = false;

		for (AbstractCard c : AbstractDungeon.player.hand.group)
			if (!c.isEthereal && c.type == AbstractCard.CardType.SKILL) {
				c.retain = true;
				trigged = true;
			}

		if (trigged)
			this.flash();
	}

	@Override
	public void atEndOfRound() {
		if (this.amount == 0)
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
		else
			this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
	}
}
