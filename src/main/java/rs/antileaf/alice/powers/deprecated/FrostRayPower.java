package rs.antileaf.alice.powers.deprecated;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.PowerIconShowEffect;
import rs.antileaf.alice.cardmodifier.PhantomCardModifier;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceHelper;
import rs.antileaf.alice.utils.AliceMiscHelper;

public class FrostRayPower extends AbstractAlicePower {
	public static final String POWER_ID = FrostRayPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public FrostRayPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
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
		this.description = AliceMiscHelper.join(
				powerStrings.DESCRIPTIONS[0],
				"#b" + this.amount,
				powerStrings.DESCRIPTIONS[1]
		);
	}
	
	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (card.purgeOnUse || !action.exhaustCard ||
				(card.type != AbstractCard.CardType.ATTACK && card.type != AbstractCard.CardType.SKILL) ||
				CardModifierManager.hasModifier(card, PhantomCardModifier.ID))
			return;
		
		AliceHelper.addEffect(new PowerIconShowEffect(this));
		this.flash();
		action.exhaustCard = false;
		
		this.amount -= 1;
		if (this.amount <= 0)
			this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
