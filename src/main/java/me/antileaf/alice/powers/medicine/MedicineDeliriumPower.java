package me.antileaf.alice.powers.medicine;

import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import me.antileaf.alice.powers.AbstractAlicePower;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineDeliriumPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = MedicineDeliriumPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public MedicineDeliriumPower(AbstractCreature owner) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = -1;
		
		this.updateDescription();
		this.initializeImage(POWER_ID);
		this.type = NeutralPowertypePatch.NEUTRAL;
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}
}
