package rs.antileaf.alice.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.dynamicdynamic.DynamicDynamicVariable;
import com.evacipated.cardcrawl.mod.stslib.dynamicdynamic.DynamicProvider;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.strings.AliceCardModifierStrings;

import java.util.UUID;

public class RevelationCardModifier extends AbstractCardModifier implements DynamicProvider {
	private static final String SIMPLE_NAME = RevelationCardModifier.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceCardModifierStrings cardModifierStrings = AliceCardModifierStrings.get(SIMPLE_NAME);
	
	private final UUID uuid;
	private boolean isRandom = true;
	private int baseAmount;
	private int amount;
	
	public RevelationCardModifier(int baseAmount) {
		this.uuid = UUID.randomUUID();
		this.amount = this.baseAmount = baseAmount;
		this.priority = 98;
	}
	
	public void stackAmount(int stackAmount) {
		this.baseAmount += stackAmount;
	}
	
	@Override
	public String modifyDescription(String rawDescription, AbstractCard card) {
		return rawDescription + " NL " +
				(this.isRandom ? cardModifierStrings.EXTENDED_DESCRIPTION[0] : "") +
				String.format(cardModifierStrings.DESCRIPTION,
						DynamicProvider.generateKey(card, this, true)
				);
	}
	
	@Override
	public void onInitialApplication(AbstractCard card) {
		if (card.target == AbstractCard.CardTarget.ENEMY)
			this.isRandom = false;
		
		DynamicDynamicVariable.registerVariable(card, this);
	}
	
	@Override
	public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
//		super.onUse(card, target, action);
		
		if (!(target instanceof AbstractMonster) || this.isRandom)
			this.addToBot(new DamageRandomEnemyAction(
					new DamageInfo(AbstractDungeon.player, this.amount, card.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		else
			this.addToBot(new DamageAction(
					target,
					new DamageInfo(AbstractDungeon.player, this.amount, card.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
	}
	
	@Override
	public void onApplyPowers(AbstractCard card) {
		float tmp = this.baseAmount;
		
		for (AbstractRelic r : AbstractDungeon.player.relics)
			tmp = r.atDamageModify(tmp, card);
		
		for (AbstractPower p : AbstractDungeon.player.powers)
			tmp = p.atDamageGive(tmp, card.damageTypeForTurn, card);
		
//		for (AbstractCardModifier mod : CardModifierManager.modifiers(card))
//			tmp = mod.modifyDamage(tmp, card.damageTypeForTurn, card, null);
		
		tmp = AbstractDungeon.player.stance.atDamageGive(tmp, card.damageTypeForTurn, card);
		
		for (AbstractPower p : AbstractDungeon.player.powers)
			tmp = p.atDamageFinalGive(tmp, card.damageTypeForTurn, card);
		
		if (tmp < 0.0F)
			tmp = 0.0F;
		this.amount = MathUtils.floor(tmp);
	}
	
	@Override
	public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
		float tmp = this.baseAmount;
		
		for (AbstractRelic r : AbstractDungeon.player.relics)
			tmp = r.atDamageModify(tmp, card);
		
		for (AbstractPower p : AbstractDungeon.player.powers)
			tmp = p.atDamageGive(tmp, card.damageTypeForTurn, card);
		
		tmp = AbstractDungeon.player.stance.atDamageGive(tmp, card.damageTypeForTurn, card);
		
		if (mo != null)
			for (AbstractPower p : mo.powers)
				tmp = p.atDamageReceive(tmp, card.damageTypeForTurn, card);
		
		for (AbstractPower p : AbstractDungeon.player.powers)
			tmp = p.atDamageFinalGive(tmp, card.damageTypeForTurn, card);
		
		if (mo != null)
			for (AbstractPower p : mo.powers)
				tmp = p.atDamageFinalReceive(tmp, card.damageTypeForTurn, card);
		
		if (tmp < 0.0F)
			tmp = 0.0F;
		this.amount = MathUtils.floor(tmp);
	}
	
	@Override
	public AbstractCardModifier makeCopy() {
		return new RevelationCardModifier(this.baseAmount);
	}
	
	@Override
	public UUID getDynamicUUID() {
		return this.uuid;
	}
	
	@Override
	public boolean isModified(AbstractCard card) {
		return this.amount != this.baseAmount;
	}
	
	@Override
	public int value(AbstractCard card) {
		return this.amount;
	}
	
	@Override
	public int baseValue(AbstractCard card) {
		return this.baseAmount;
	}
	
	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}
	
	@Override
	public boolean shouldApply(AbstractCard card) {
		return !CardModifierManager.hasModifier(card, ID);
	}
}
