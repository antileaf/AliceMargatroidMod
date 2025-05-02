package me.antileaf.alice.targeting.handlers;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;

public class DollOrEmptySlotOrNoneTargeting extends TargetingHandler<AbstractDoll> {
	public static AbstractDoll getTarget(AbstractCard card) {
		return CustomTargeting.getCardTarget(card);
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
		return true;
	}
	
	@Override
	public boolean isValidTarget(AbstractDoll doll) {
		return doll == null || DollManager.get().contains(doll);
	}
}
