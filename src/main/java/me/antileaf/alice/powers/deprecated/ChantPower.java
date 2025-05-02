package me.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.action.doll.DollActAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class ChantPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = ChantPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public ChantPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 5;
		
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
		
		int amount = this.amount;
		this.addToBot(new AnonymousAction(() -> {
			for (AbstractDoll doll : DollManager.get().getDolls())
				for (int i = 0; i < amount; i++)
					if (!(doll instanceof EmptyDollSlot))
						AliceHelper.addActionToBuffer(new DollActAction(doll));
			AliceHelper.commitBuffer();
		}));
		
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
