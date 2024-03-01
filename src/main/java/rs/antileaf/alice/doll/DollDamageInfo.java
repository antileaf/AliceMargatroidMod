package rs.antileaf.alice.doll;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.powers.interfaces.EnemyDollDamageModPower;
import rs.antileaf.alice.patches.enums.DamageTypeEnum;

public class DollDamageInfo extends DamageInfo {
	public Class<? extends AbstractDoll> dollClass;
	public AbstractDoll.AmountType amountType;
	public AbstractDoll.AmountTime amountTime;
	
	public DollDamageInfo(int base, Class<? extends AbstractDoll> dollClass,
	                      AbstractDoll.AmountType amountType, AbstractDoll.AmountTime amountTime) {
		super(AbstractDungeon.player, base, DamageTypeEnum.DOLL);
		
		this.dollClass = dollClass;
		this.amountType = amountType;
		this.amountTime = amountTime;
	}
	
	// TODO: Overwrite applyPowers()
	@Override
	public void applyPowers(AbstractCreature owner, AbstractCreature target) {
		this.output = this.base;
		this.isModified = false;
		float tmp = (float) this.output;
		
		assert owner.isPlayer : "DollDamageInfo.applyPowers() called with non-player owner";
		
//		for (AbstractPower power : owner.powers)
//			if (power instanceof PlayerDollAmountModPower)
//				tmp = ((PlayerDollAmountModPower) power).modifyDollAmount(tmp, this.dollClass);
	
		for (AbstractPower power : target.powers)
			if (power instanceof EnemyDollDamageModPower)
				tmp = ((EnemyDollDamageModPower) power).modifyDollAmount(tmp);
	}
}
