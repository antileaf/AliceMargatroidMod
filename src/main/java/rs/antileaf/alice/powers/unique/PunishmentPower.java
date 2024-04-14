package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceMiscKit;

public class PunishmentPower extends AbstractAlicePower {
	public static final String POWER_ID = PunishmentPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public PunishmentPower(AbstractMonster owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		
		this.type = PowerType.DEBUFF;
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
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void duringTurn() {
		AbstractMonster m = (AbstractMonster) this.owner;
		if (m.intent == AbstractMonster.Intent.ATTACK ||
				m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
				m.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
				m.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
			
			this.flash();
			this.addToBot(new DamageAction(
					m,
					new DamageInfo(null, this.amount, DamageInfo.DamageType.THORNS),
					AbstractGameAction.AttackEffect.LIGHTNING
			));
			this.addToBot(new RemoveSpecificPowerAction(m, m, this));
		}
	}
}
