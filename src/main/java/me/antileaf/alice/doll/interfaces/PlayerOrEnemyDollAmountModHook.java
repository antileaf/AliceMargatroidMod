package me.antileaf.alice.doll.interfaces;

import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.enums.DollAmountTime;
import me.antileaf.alice.doll.enums.DollAmountType;

// Applied before original powers.
public interface PlayerOrEnemyDollAmountModHook {
	boolean isFinal();
	
	float modifyDollAmount(
			float amount,
			AbstractDoll doll,
			DollAmountType amountType,
			DollAmountTime amountTime);
}
