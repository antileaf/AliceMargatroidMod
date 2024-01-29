package rs.antileaf.alice.doll;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.patches.enums.DamageTypeEnum;

public class DollDamageInfo extends DamageInfo {
	public Class<? extends AbstractDoll> dollClass;
	
	public DollDamageInfo(int base, Class<? extends AbstractDoll> dollClass) {
		super(AbstractDungeon.player, base, DamageTypeEnum.DOLL);
		
		this.dollClass = dollClass;
	}
	
	// TODO: Overwrite applyPowers()
}
