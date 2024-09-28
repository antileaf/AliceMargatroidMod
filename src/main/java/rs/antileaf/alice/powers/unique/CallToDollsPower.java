package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.doll.interfaces.PlayerOrEnemyDollAmountModHook;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class CallToDollsPower extends AbstractAlicePower implements PlayerOrEnemyDollAmountModHook {
	public static final String SIMPLE_NAME = CallToDollsPower.class.getSimpleName();
	public static final String POWER_ID = SIMPLE_NAME;
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public CallToDollsPower(AbstractMonster owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		
		this.type = PowerType.DEBUFF;
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
		AliceSpireKit.log("this.amount = ", this.amount);
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	@Override
	public void atEndOfRound() {
		if (this.amount > 1)
			this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
		else
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
	
	@Override
	public boolean isFinal() {
		return true;
	}
	
	@Override
	public float modifyDollAmount(
			float amount,
			AbstractDoll doll,
			DollAmountType amountType,
			DollAmountTime amountTime) {
		return amountType == DollAmountType.DAMAGE ? amount * 1.5F : amount;
	}
}
