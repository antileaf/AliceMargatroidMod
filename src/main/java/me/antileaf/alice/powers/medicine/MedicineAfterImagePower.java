package me.antileaf.alice.powers.medicine;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineAfterImagePower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = MedicineAfterImagePower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	private AbstractCreature source;
	
	public MedicineAfterImagePower(AbstractCreature owner, AbstractCreature source, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		
		this.updateDescription();
		this.loadRegion("afterImage");
		this.type = PowerType.BUFF;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}
	
	// Effect is hard-coded in MedicineCardItem
}
