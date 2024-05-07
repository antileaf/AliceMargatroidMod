package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.dolls.LondonDoll;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.powers.interfaces.PlayerOrEnemyDollAmountModPower;

public class ThePhantomOfTheGrandGuignolPower extends AbstractAlicePower implements PlayerOrEnemyDollAmountModPower {
	public static final String POWER_ID = ThePhantomOfTheGrandGuignolPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public ThePhantomOfTheGrandGuignolPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
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
	public boolean isFinal() {
		return false;
	}
	
	@Override
	public float modifyDollAmount(float amount, AbstractDoll doll, DollAmountType amountType, DollAmountTime amountTime) {
		if (doll instanceof LondonDoll)
			return amount;
		
		return amount + this.amount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
}
