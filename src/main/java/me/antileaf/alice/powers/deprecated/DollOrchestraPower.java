package me.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.interfaces.OnDollOperateHook;
import me.antileaf.alice.powers.AbstractAlicePower;

public class DollOrchestraPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String POWER_ID = DollOrchestraPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public DollOrchestraPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 99;
		
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
	public void postSpawnDoll(AbstractDoll doll) {
		this.flash();
		this.addToBot(new DrawCardAction(this.amount));
	}
}
