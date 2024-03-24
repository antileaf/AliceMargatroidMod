package rs.antileaf.alice.powers.interfaces;

import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;

// Applied before original powers.
public interface PlayerOrEnemyDollAmountModPower {
	boolean isFinal();
	
	float modifyDollAmount(
			float amount,
			AbstractDoll doll,
			DollAmountType amountType,
			DollAmountTime amountTime);
}
