package rs.antileaf.alice.doll.targeting;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollOrEmptySlotTargeting extends TargetingHandler<AbstractDoll> {
	public static AbstractDoll getTarget(AbstractCard card) {
		AbstractDoll target = CustomTargeting.getCardTarget(card);
		if (target == null) {
			for (AbstractDoll doll : DollManager.get().getDolls()) {
				if (doll instanceof EmptyDollSlot) {
					target = doll;
					break;
				}
			}
			
			if (target == null)
				target = DollManager.get().getDolls().get(DollManager.MAX_DOLL_SLOTS - 1);
		}
		
		return target;
	}
	
	protected AbstractDoll hovered = null;
	
	@Override
	public void updateHovered() {
		this.hovered = DollManager.get().getHoveredDoll();
	}
	
	@Override
	public AbstractDoll getHovered() {
		return this.hovered;
	}
	
	@Override
	public void clearHovered() {
		this.hovered = null;
	}
	
	@Override
	public boolean hasTarget() {
		return this.hovered != null;
	}
	
	@Override
	public boolean isValidTarget(AbstractDoll doll) {
		return DollManager.get().contains(doll);
	}
}
