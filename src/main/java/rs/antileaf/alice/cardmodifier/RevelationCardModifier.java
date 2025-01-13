package rs.antileaf.alice.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.strings.AliceCardModifierStrings;
import rs.antileaf.alice.strings.AliceLanguageStrings;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.List;

public class RevelationCardModifier extends AbstractCardModifier {
	private static final String SIMPLE_NAME = RevelationCardModifier.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final AliceCardModifierStrings cardModifierStrings = AliceCardModifierStrings.get(ID);
	
	public static final int AMOUNT = 2;
	
	private boolean upgraded;
	
	public RevelationCardModifier(boolean upgraded) {
		this.upgraded = upgraded;
		this.priority = 98;
	}
	
	public boolean isUpgraded() {
		return this.upgraded;
	}
	
	public String getModifierName() {
		return cardModifierStrings.NAME + (this.upgraded ? "+" : "");
	}
	
	@Override
	public String modifyDescription(String rawDescription, AbstractCard card) {
		return rawDescription + " NL *" + this.getModifierName() + " " + AliceLanguageStrings.PERIOD;
	}
	
	@Override
	public List<TooltipInfo> additionalTooltips(AbstractCard card) {
		ArrayList<TooltipInfo> tips = new ArrayList<>();
		int i = this.upgraded ? 1 : 0;
		tips.add(new TooltipInfo(this.getModifierName(),
				String.format(cardModifierStrings.DESCRIPTION,
						cardModifierStrings.EXTENDED_DESCRIPTION[i],
						AMOUNT,
						this.getModifierName())));
		return tips;
	}
	
//	@Override
//	public void onInitialApplication(AbstractCard card) {
//
//	}
	
	@Override
	public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
		ArrayList<AbstractCard> attacks = new ArrayList<>();
		for (AbstractCard other : AbstractDungeon.player.hand.group)
			if (other.type == AbstractCard.CardType.ATTACK && other != card)
				attacks.add(other);
		
		if (!attacks.isEmpty()) {
			ArrayList<AbstractCard> chosen;
			if (!this.upgraded) {
				chosen = new ArrayList<>();
				chosen.add(attacks.get(MathUtils.random(attacks.size() - 1)));
			}
			else
				chosen = attacks;
			
			this.addToBot(new AnonymousAction(() -> {
				ArrayList<AbstractCard> failed = new ArrayList<>();
				for (AbstractCard c : chosen) {
					AliceHelper.upgradeCardDamage(c, AMOUNT);
					
					if (AbstractDungeon.player.hand.contains(c)) {
						c.flash();
						
						if (this.shouldApply(c))
							CardModifierManager.addModifier(c, new RevelationCardModifier(this.upgraded));
					}
					else
						failed.add(c);
				}
				
				if (!failed.isEmpty())
					AliceHelper.log(SIMPLE_NAME + ": Chosen card(s) not in hand: " + failed);
			}));
		}
 	}
	
	@Override
	public AbstractCardModifier makeCopy() {
		return new RevelationCardModifier(this.upgraded);
	}
	
	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}
	
	@Override
	public boolean shouldApply(AbstractCard card) {
		if (!CardModifierManager.hasModifier(card, ID))
			return true;
		
		RevelationCardModifier mod = (RevelationCardModifier) CardModifierManager.getModifiers(card, ID).get(0);
		return !mod.upgraded && this.upgraded;
	}
}
