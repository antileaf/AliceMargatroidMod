package me.antileaf.alice.powers.unique;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.GainPowerEffect;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.interfaces.OnDollOperateHook;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class MaidensBunrakuPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String SIMPLE_NAME = MaidensBunrakuPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public MaidensBunrakuPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		this.priority = 0;
		
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
	
//	@Override
//	public void atStartOfTurn() {
//		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
//	}
	
	@Override
	public void preDollAct(AbstractDoll doll) {
		Object effect = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
		if (effect instanceof ArrayList)
			((ArrayList<AbstractGameEffect>) effect).add(new GainPowerEffect(this));
		this.addToTop(new DollGainBlockAction(doll, this.amount));
	}
}
