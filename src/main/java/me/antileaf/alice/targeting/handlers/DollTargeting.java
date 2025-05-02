package me.antileaf.alice.targeting.handlers;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollTargeting extends TargetingHandler<AbstractDoll> {
	public static AbstractDoll getTarget(AbstractCard card) {
		AbstractDoll target = CustomTargeting.getCardTarget(card);
		if (target == null) {
			for (AbstractDoll doll : DollManager.get().getDolls()) {
				if (!(doll instanceof EmptyDollSlot)) {
					target = doll;
					break;
				}
			}
		}
		
		return target;
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
		return this.hovered != null && !(this.hovered instanceof EmptyDollSlot);
	}
	
	@Override
	public boolean isValidTarget(AbstractDoll doll) {
		return !(doll instanceof EmptyDollSlot) && DollManager.get().contains(doll);
	}
}
