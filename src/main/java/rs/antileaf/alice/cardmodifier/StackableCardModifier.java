package rs.antileaf.alice.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class StackableCardModifier extends AbstractCardModifier {
	public int amount = 1;
	public boolean canGoNegative = false;

	public StackableCardModifier(int amount, boolean canGoNegative) {
		this.amount = amount;
		this.canGoNegative = canGoNegative;
	}

	public StackableCardModifier(int amount) {
		this.amount = amount;
	}

	public StackableCardModifier() {
	}

	public void stackAmount(AbstractCard card, int amt) {
		this.amount += amt;
		this.afterStackAmount(card, amt);
	}

	public void reduceAmount(AbstractCard card, int amt) {
		this.amount -= amt;

		if (!this.canGoNegative) {
			this.amount = Integer.max(this.amount, 0);

			if (this.amount == 0)
				CardModifierManager.removeSpecificModifier(
						card, this, true);
		}
		
		this.afterReduceAmount(card, amt);
	}
	
	public void afterStackAmount(AbstractCard card, int amt) {
	
	}
	
	public void afterReduceAmount(AbstractCard card, int amt) {
	
	}
}
