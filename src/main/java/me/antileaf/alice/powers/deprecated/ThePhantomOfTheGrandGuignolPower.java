package me.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.NetherlandsDoll;
import me.antileaf.alice.doll.dolls.deprecated.DEPRECATEDLondonDoll;
import me.antileaf.alice.doll.enums.DollAmountTime;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.doll.interfaces.PlayerOrEnemyDollAmountModHook;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

@Deprecated
public class ThePhantomOfTheGrandGuignolPower extends AbstractAlicePower implements PlayerOrEnemyDollAmountModHook {
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
	
	private void updatePowers(int diff) {
		int netherlands_count = (int)DollManager.get().getDolls().stream()
				.filter(doll -> doll instanceof NetherlandsDoll)
				.count();
		
		int amt = this.amount * netherlands_count;
		if (amt == 0)
			return;
		
		AliceHelper.addActionToBuffer(new ApplyPowerAction(
				this.owner,
				this.owner,
				new StrengthPower(this.owner, amt),
				amt
		));
		AliceHelper.addActionToBuffer(new ApplyPowerAction(
				this.owner,
				this.owner,
				new DexterityPower(this.owner, amt),
				amt
		));
		
		AliceHelper.commitBuffer();
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
		
		this.updatePowers(stackAmount);
	}
	
	@Override
	public void onInitialApplication() {
		this.updatePowers(this.amount);
	}
	
	@Override
	public void onRemove() {
		this.updatePowers(-this.amount);
	}
	
	@Override
	public boolean isFinal() {
		return false;
	}
	
	@Override
	public float modifyDollAmount(float amount, AbstractDoll doll, DollAmountType amountType, DollAmountTime amountTime) {
		if (doll instanceof DEPRECATEDLondonDoll)
			return amount;
		
		return amount + this.amount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
}
