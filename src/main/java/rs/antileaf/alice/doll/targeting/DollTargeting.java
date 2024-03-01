package rs.antileaf.alice.doll.targeting;

import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollTargeting extends AbstractAliceMagtroidTargeting {
	@Override
	public boolean hasTarget() {
		return this.hovered != null;
	}
	
	@Override
	public void updateHovered() {
		this.hovered = this.updateHoveredDoll();
		if (this.hovered instanceof EmptyDollSlot)
			this.hovered = null;
	}
}
