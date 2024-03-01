package rs.antileaf.alice.doll.targeting;

public class DollOrEmptySlotTargeting extends AbstractAliceMagtroidTargeting {
	@Override
	public boolean hasTarget() {
		return this.hovered != null;
	}
	
	@Override
	public void updateHovered() {
		this.hovered = this.updateHoveredDoll();
	}
}
