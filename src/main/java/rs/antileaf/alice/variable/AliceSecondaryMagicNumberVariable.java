package rs.antileaf.alice.variable;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.antileaf.alice.cards.AbstractAliceCard;

public class AliceSecondaryMagicNumberVariable extends DynamicVariable {
	@Override
	public String key() {
		return "aliceM2";
	}
	
	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.secondaryMagicNumber != aliceCard.baseSecondaryMagicNumber;
		}
		return false;
	}
	
	public int value(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.secondaryMagicNumber;
		}
		return -1;
	}
	
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.baseSecondaryMagicNumber;
		}
		return -1;
	}
	
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.isSecondaryMagicNumberUpgraded;
		}
		return false;
	}
}
