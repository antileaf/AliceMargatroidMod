package rs.antileaf.alice.doll.interfaces;

import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;

// Applied before original powers.
public interface PlayerOrEnemyDollAmountModHook {
	boolean isFinal();
	
	float modifyDollAmount(
			float amount,
			AbstractDoll doll,
			DollAmountType amountType,
			DollAmountTime amountTime);
}
