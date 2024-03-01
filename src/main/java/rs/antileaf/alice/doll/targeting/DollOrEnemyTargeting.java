package rs.antileaf.alice.doll.targeting;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollOrEnemyTargeting extends AbstractAliceMagtroidTargeting {
	@Override
	public boolean hasTarget() {
		return this.hovered != null;
	}
	
	@Override
	public void updateHovered() {
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (m.hb.hovered) {
				this.hovered = m;
				return;
			}
		}
		
		this.hovered = this.updateHoveredDoll();
		if (this.hovered instanceof EmptyDollSlot)
			this.hovered = null;
	}
}
