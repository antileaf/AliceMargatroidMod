package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;

public class SemiAutomatonPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String SIMPLE_NAME = SemiAutomatonPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public SemiAutomatonPower(int amount) {
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
		this.description = String.format(
				powerStrings.DESCRIPTIONS[0],
				this.amount
		);
	}

	@Override
	public void postSpawnDoll(AbstractDoll doll) {
		this.flash();

		for (int i = 0; i < this.amount; i++)
			this.addToBot(new DollActAction(doll));

		AliceHelper.addActionToBuffer(new RemoveSpecificPowerAction(
				this.owner, this.owner, this));
	}
}
