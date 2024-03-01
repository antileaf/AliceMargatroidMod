package rs.antileaf.alice.doll.targeting;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollOrNoneTargeting extends AbstractAliceMagtroidTargeting {
	@Override
	public boolean hasTarget() {
		return true;
	}
	
	@Override
	public void updateHovered() {
		this.hovered = this.updateHoveredDoll();
		if (this.hovered instanceof EmptyDollSlot)
			this.hovered = null;
	}
}
