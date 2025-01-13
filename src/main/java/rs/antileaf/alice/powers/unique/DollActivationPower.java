package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;
import rs.antileaf.alice.utils.AliceMiscHelper;

public class DollActivationPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = DollActivationPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollActivationPower(int amount) {
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
		if (this.amount > DollManager.MAX_DOLL_SLOTS)
			this.amount = DollManager.MAX_DOLL_SLOTS;
	}
	
	@Override
	public void updateDescription() {
		if (this.amount <= 1)
			this.description = powerStrings.DESCRIPTIONS[0];
		else
			this.description = AliceMiscHelper.join(
					powerStrings.DESCRIPTIONS[1],
					"#b" + this.amount,
					powerStrings.DESCRIPTIONS[2]
			);
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (!isPlayer)
			return;
		
		if (DollManager.get().hasDoll()) {
			this.flash();
			
			int count = this.amount;
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot)) {
					if (count <= 0)
						break;
					
					this.addToBot(new DollActAction(doll));
					count--;
				}
		}
	}
}
