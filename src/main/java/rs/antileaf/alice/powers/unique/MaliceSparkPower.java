package rs.antileaf.alice.powers.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceMiscKit;

public class MaliceSparkPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = MaliceSparkPower.class.getSimpleName();
	public static final String POWER_ID = SIMPLE_NAME;
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
//	private int diff = 0;
	
	public MaliceSparkPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.DEBUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
		this.updateDescription();
		BaseMod.MAX_HAND_SIZE -= stackAmount;
	}
	
	@Override
	public void onInitialApplication() {
//		this.diff = BaseMod.MAX_HAND_SIZE - this.amount;
		BaseMod.MAX_HAND_SIZE -= this.amount;
	}
	
	@Override
	public void onRemove() {
		BaseMod.MAX_HAND_SIZE += this.amount;
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer)
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
	
	@Override
	public void updateDescription() {
		this.description = AliceMiscKit.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
}
