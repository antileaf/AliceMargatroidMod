package rs.antileaf.alice.powers.deprecated;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;

public class SealOfLightPower extends AbstractAlicePower {
	public static final String SIMPLE_NAME = SealOfLightPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	private int percent;
	
	public SealOfLightPower(AbstractMonster owner, int percent) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = -1;
		this.percent = percent;
		
		this.type = PowerType.DEBUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.onInitialApplication();
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.percent);
	}
	
	@Override
	public void onInitialApplication() {
		if (this.owner instanceof AbstractMonster) {
			AbstractMonster m = (AbstractMonster)this.owner;
			
			m.damage(new DamageInfo(null, m.currentHealth * this.percent / 100,
					DamageInfo.DamageType.HP_LOSS));
			m.decreaseMaxHealth(m.maxHealth * this.percent / 100);
		}
	}
}
