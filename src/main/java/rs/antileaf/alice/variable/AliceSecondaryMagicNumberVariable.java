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
			return aliceCard.isSecondaryMagicNumberModified;
		}
		return false;
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.secondaryMagicNumber;
		}
		return -1;
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.baseSecondaryMagicNumber;
		}
		return -1;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.upgradedSecondaryMagicNumber;
		}
		return false;
	}
}
