package rs.antileaf.alice.powers.unique;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class PunishPower extends AbstractPower {
	public static final String POWER_ID = PunishPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public PunishPower(AbstractMonster owner, int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		
		this.type = PowerType.DEBUFF;
		this.updateDescription();
		this.img = new Texture(AliceSpireKit.getPowerImgFilePath("default"));
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
