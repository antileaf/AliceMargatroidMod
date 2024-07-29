package rs.antileaf.alice.variable;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.antileaf.alice.cards.AbstractAliceCard;

public class AliceSecondaryDamageVariable extends DynamicVariable {
	@Override
	public String key() {
		return "aliceD2";
	}
	
	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.isSecondaryDamageModified;
		}
		return false;
	}
	
	public int value(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.secondaryDamage;
		}
		return -1;
	}
	
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.baseSecondaryDamage;
		}
		return -1;
	}
	
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractAliceCard) {
			AbstractAliceCard aliceCard = (AbstractAliceCard) card;
			return aliceCard.upgradedSecondaryDamage;
		}
		return false;
	}
}
