package me.antileaf.alice.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import me.antileaf.alice.strings.AliceCardModifierStrings;
import me.antileaf.alice.strings.AliceLanguageStrings;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.AliceMiscHelper;

import java.util.ArrayList;
import java.util.List;

public class PhantomCardModifier extends AbstractCardModifier {
	private static final String SIMPLE_NAME = PhantomCardModifier.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final AliceCardModifierStrings cardModifierStrings = AliceCardModifierStrings.get(ID);
	
	public static boolean check(AbstractCard card) {
		return CardModifierManager.hasModifier(card, ID);
	}
	
	public PhantomCardModifier() {
		this.priority = 99; // The smaller the number, the higher the priority
	}
	
	@Override
	public String modifyDescription(String rawDescription, AbstractCard card) {
		return rawDescription + " NL *" + AliceMiscHelper.join(
				cardModifierStrings.NAME,
				AliceLanguageStrings.PERIOD
		);
	}
	
	@Override
	public List<TooltipInfo> additionalTooltips(AbstractCard card) {
		ArrayList<TooltipInfo> tips = new ArrayList<>();
		tips.add(new TooltipInfo(cardModifierStrings.NAME, cardModifierStrings.DESCRIPTION));
		return tips;
	}
	
	@Override
	public void onInitialApplication(AbstractCard card) {
//		PurgeField.purge.set(card, true);
	}
	
	@Override
	public AbstractCardModifier makeCopy() {
		return new PhantomCardModifier();
	}
	
//	@Override
//	public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
//		card.stopGlowing();
//	}
	
	@Override
	public void atEndOfTurn(AbstractCard card, CardGroup group) {
		assert check(card);
		this.addToBot(new ExhaustSpecificCardAction(card, group));
		AliceHelper.addEffect(new ExhaustCardEffect(card));
	}
	
	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}
	
	@Override
	public boolean shouldApply(AbstractCard card) {
		return !CardModifierManager.hasModifier(card, ID);
	}
	
	@Override
	public Color getGlow(AbstractCard card) {
		return Color.PURPLE.cpy();
	}
}
