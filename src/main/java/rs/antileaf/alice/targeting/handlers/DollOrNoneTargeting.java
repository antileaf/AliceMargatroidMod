package rs.antileaf.alice.targeting.handlers;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollOrNoneTargeting extends TargetingHandler<AbstractDoll> {
	public static AbstractDoll getTarget(AbstractCard card) {
		return CustomTargeting.getCardTarget(card);
	}
	
	protected AbstractDoll hovered = null;
	
	@Override
	public void updateHovered() {
		this.hovered = DollManager.get().getHoveredDoll();
		
		if (this.hovered instanceof EmptyDollSlot)
			this.hovered = null;
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
		return !(this.hovered instanceof EmptyDollSlot);
	}
	
	@Override
	public boolean isValidTarget(AbstractDoll doll) {
		return !(doll instanceof EmptyDollSlot) && DollManager.get().contains(doll);
	}
}
