package rs.antileaf.alice.powers.unique;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;

public class SwordOfLightPower extends AbstractAlicePower implements InvisiblePower {
	public static final String SIMPLE_NAME = SwordOfLightPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public SwordOfLightPower() {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = -1;
		this.priority = 7;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(SIMPLE_NAME);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		// Do not stack.
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.type == AbstractCard.CardType.ATTACK) {
//			this.flash();
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		}
	}
	
	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		}
	}
	
	@Override
	public float atDamageGive(float damage, DamageInfo.DamageType type) {
		return type == DamageInfo.DamageType.NORMAL ? damage * 1.5F : damage;
	}
}
