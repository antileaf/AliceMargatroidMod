package me.antileaf.alice.doll;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import me.antileaf.alice.doll.enums.DollAmountTime;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.doll.interfaces.PlayerOrEnemyDollAmountModHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DollDamageInfo extends DamageInfo {
	private static final Logger logger = LogManager.getLogger(DollDamageInfo.class.getName());

	public AbstractDoll doll;
	public DollAmountType amountType;
	public DollAmountTime amountTime;
	public AbstractDoll.DollActModifier modifier;
	
	public DollDamageInfo(int base, AbstractDoll doll, DollAmountType amountType, DollAmountTime amountTime,
						  AbstractDoll.DollActModifier modifier) {
		super(AbstractDungeon.player, base, DamageType.THORNS);
		
		this.doll = doll;
		this.amountType = amountType;
		this.amountTime = amountTime;
		this.modifier = modifier;
	}

	public DollDamageInfo(int base, AbstractDoll doll, DollAmountType amountType, DollAmountTime amountTime) {
		this(base, doll, amountType, amountTime, new AbstractDoll.DollActModifier());
	}
	
	@Override
	public void applyPowers(AbstractCreature owner, AbstractCreature target) {
		if (!owner.isPlayer)
			logger.error("DollDamageInfo.applyPowers() called with non-player owner");

		this.output = this.base;
		this.isModified = false;
		float res = (float) this.output;

		if (this.modifier.halfDamage)
			res *= 0.5F;
		
//		for (AbstractPower power : owner.powers)
//			if (power instanceof PlayerDollAmountModPower)
//				tmp = ((PlayerDollAmountModPower) power).modifyDollAmount(tmp, this.dollClass);
		// This logic should not be implemented here, but in AbstractDoll.
	
		for (AbstractPower power : target.powers) {
			if (power instanceof PlayerOrEnemyDollAmountModHook &&
					!((PlayerOrEnemyDollAmountModHook) power).isFinal())
				res = ((PlayerOrEnemyDollAmountModHook) power)
						.modifyDollAmount(res, this.doll, this.amountType, this.amountTime);
		}
		
		for (AbstractPower power : target.powers) {
			float tmpNormal = power.atDamageReceive(res, DamageType.NORMAL);
			float tmpThorns = power.atDamageReceive(res, DamageType.THORNS);
			
			res = Math.max(tmpNormal, tmpThorns);
		}
		
		for (AbstractPower power : target.powers) {
			if (power instanceof PlayerOrEnemyDollAmountModHook &&
					((PlayerOrEnemyDollAmountModHook) power).isFinal())
				res = ((PlayerOrEnemyDollAmountModHook) power)
						.modifyDollAmount(res, this.doll, this.amountType, this.amountTime);
		}
		
		for (AbstractPower power : target.powers) {
			float tmpNormal = power.atDamageFinalReceive(res, DamageType.NORMAL);
			float tmpThorns = power.atDamageFinalReceive(res, DamageType.THORNS);
			
			res = Math.max(tmpNormal, tmpThorns);
		}
		
		if (res < 0.0F)
			res = 0.0F;
		this.output = MathUtils.floor(res);
	}
	
	public static int[] createDamageMatrix(int baseDamage, AbstractDoll doll,
	                                       DollAmountType amountType, DollAmountTime amountTime,
										   AbstractDoll.DollActModifier modifier) {
		int[] res = new int[AbstractDungeon.getMonsters().monsters.size()];
		
		for (int i = 0; i < res.length; i++) {
			DollDamageInfo info = new DollDamageInfo(baseDamage, doll, amountType, amountTime, modifier);
			info.applyPowers(AbstractDungeon.player, AbstractDungeon.getMonsters().monsters.get(i));
			res[i] = info.output;
		}
		
		return res;
	}

	public static int[] createDamageMatrix(int baseDamage, AbstractDoll doll,
	                                       DollAmountType amountType, DollAmountTime amountTime) {
		return createDamageMatrix(baseDamage, doll, amountType, amountTime,
				new AbstractDoll.DollActModifier());
	}
}
