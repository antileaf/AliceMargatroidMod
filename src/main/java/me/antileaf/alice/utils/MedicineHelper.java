package me.antileaf.alice.utils;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.powers.medicine.MedicineDeliriumPower;

public abstract class MedicineHelper {
	public static boolean isDelirium() {
		if (!AliceHelper.isInBattle())
			return false;
		
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (m.hasPower(MedicineDeliriumPower.POWER_ID))
				return true;
		
		return false;
	}
}
