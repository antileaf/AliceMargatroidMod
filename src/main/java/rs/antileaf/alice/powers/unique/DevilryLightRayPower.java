package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import rs.antileaf.alice.powers.AbstractAlicePower;

public class DevilryLightRayPower extends AbstractAlicePower {
	public static final String POWER_ID = DevilryLightRayPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DevilryLightRayPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 0;
		
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
	public void atStartOfTurn() {
		this.flash();
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped())
				this.addToBot(new ApplyPowerAction(
						m, this.owner, new WeakPower(m, this.amount, false), this.amount));
		
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
