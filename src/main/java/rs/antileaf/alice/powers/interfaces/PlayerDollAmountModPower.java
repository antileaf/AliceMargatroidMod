package rs.antileaf.alice.powers.interfaces;

import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;

public interface PlayerDollAmountModPower {
	float modifyDollAmount(float amount, Class<?> dollClass, DollAmountType amountType, DollAmountTime amountTime);
}
