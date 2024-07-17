package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceMiscKit;

public class DollJudgePower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String POWER_ID = DollJudgePower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollJudgePower(int amount) {
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
	public void updateDescription() {
		this.description = AliceMiscKit.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount + "%",
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void postDollGainedBlock(AbstractDoll doll, int block) {
		int blockGain = (int) (block * this.amount / 100.0F);
		if (blockGain > 0)
			this.addToBot(new GainBlockAction(this.owner, this.owner, blockGain));
	}
}
