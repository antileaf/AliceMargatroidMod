package me.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.interfaces.OnDollOperateHook;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class DollJudgePower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String SIMPLE_NAME = DollJudgePower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollJudgePower(int amount) {
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
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	@Override
	public void postRecycleDoll(AbstractDoll doll) {
		int block = (int)(doll.block * (this.amount / 100.0F));
		if (block > 0) {
			this.addToTop(new GainBlockAction(AbstractDungeon.player, block));
			this.flash();
		}
	}
}
