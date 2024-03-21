package rs.antileaf.alice.doll;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.powers.interfaces.PlayerOrEnemyDollAmountModPower;

public class DollDamageInfo extends DamageInfo {
	public AbstractDoll doll;
	public DollAmountType amountType;
	public DollAmountTime amountTime;
	
	public DollDamageInfo(int base, AbstractDoll doll, DollAmountType amountType, DollAmountTime amountTime) {
		super(AbstractDungeon.player, base, DamageType.THORNS);
		
		this.doll = doll;
		this.amountType = amountType;
		this.amountTime = amountTime;
	}
	
	@Override
	public void applyPowers(AbstractCreature owner, AbstractCreature target) {
		this.output = this.base;
		this.isModified = false;
		float res = (float) this.output;
		
		assert owner.isPlayer : "DollDamageInfo.applyPowers() called with non-player owner";
		
//		for (AbstractPower power : owner.powers)
//			if (power instanceof PlayerDollAmountModPower)
//				tmp = ((PlayerDollAmountModPower) power).modifyDollAmount(tmp, this.dollClass);
	
		for (AbstractPower power : target.powers) {
			if (power instanceof PlayerOrEnemyDollAmountModPower &&
					!((PlayerOrEnemyDollAmountModPower) power).isFinalReceive())
				res = ((PlayerOrEnemyDollAmountModPower) power)
						.modifyDollAmount(res, this.doll, this.amountType, this.amountTime);
		}
		
		for (AbstractPower power : target.powers) {
			float tmpNormal = power.atDamageReceive(res, DamageType.NORMAL);
			float tmpThorns = power.atDamageReceive(res, DamageType.THORNS);
			
			res = Math.max(tmpNormal, tmpThorns);
		}
		
		for (AbstractPower power : owner.powers) {
			if (power instanceof PlayerOrEnemyDollAmountModPower &&
					((PlayerOrEnemyDollAmountModPower) power).isFinalReceive())
				res = ((PlayerOrEnemyDollAmountModPower) power)
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
	                                       DollAmountType amountType, DollAmountTime amountTime) {
		int[] res = new int[AbstractDungeon.getMonsters().monsters.size()];
		
		for (int i = 0; i < res.length; i++) {
			DollDamageInfo info = new DollDamageInfo(baseDamage, doll, amountType, amountTime);
			info.applyPowers(AbstractDungeon.player, AbstractDungeon.getMonsters().monsters.get(i));
			res[i] = info.output;
		}
		
		return res;
	}
}
