package me.antileaf.alice.powers.medicine;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicinePoisonPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = MedicinePoisonPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	private AbstractCreature source;
	
	public MedicinePoisonPower(AbstractCreature owner, AbstractCreature source, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.source = source;
		this.amount = amount;
		if (this.amount >= 9999)
			this.amount = 9999;
		
		this.updateDescription();
		this.loadRegion("poison");
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		
		this.priority = 999;
	}
	
	@Override
	public void playApplyPowerSfx() {
		CardCrawlGame.sound.play("POWER_POISON", 0.05F);
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			this.flashWithoutSound();
			
		}
	}
}
