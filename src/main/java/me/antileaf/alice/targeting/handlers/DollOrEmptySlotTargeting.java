package me.antileaf.alice.targeting.handlers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;

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
	
	private AbstractDoll hovered = null;
	
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
	
	@Override
	public void setDefaultTarget() {
		this.hovered = DollManager.get().getDolls().get(0);
	}
	
	@Override
	public int getDefaultTargetX() {
		return (int) DollManager.get().getDolls().get(0).hb.cX;
	}
	
	@Override
	public int getDefaultTargetY() {
		return (int) DollManager.get().getDolls().get(0).hb.cY;
	}
	
	@Override
	public void updateKeyboardTarget() {
		AbstractDoll newTarget = null;
		
		int direction = 0; // -1 for left, 1 for right, 0 for none
		if (InputActionSet.left.isJustPressed() ||
				CInputActionSet.left.isJustPressed() ||
				CInputActionSet.altLeft.isJustPressed())
			direction--;
		if (InputActionSet.right.isJustPressed() ||
				CInputActionSet.right.isJustPressed() ||
				CInputActionSet.altRight.isJustPressed())
			direction++;
		
		if (direction != 0) {
			int index = 0;
			
			if (this.hovered != null) {
				index = DollManager.get().getIndex(this.hovered);
				index -= direction;
				
				if (index < 0)
					index = DollManager.MAX_DOLL_SLOTS - 1;
				else if (index >= DollManager.MAX_DOLL_SLOTS)
					index = 0;
			}
			
			newTarget = DollManager.get().getDolls().get(index);
		}
		
		if (newTarget != null) {
			this.hovered = newTarget;
			
			Gdx.input.setCursorPosition((int) this.hovered.hb.cX, Settings.HEIGHT - (int) this.hovered.hb.cY);
			
			ReflectionHacks.setPrivate(AbstractDungeon.player, AbstractPlayer.class,
					"isUsingClickDragControl", true);
			AbstractDungeon.player.isDraggingCard = true;
		}
	}
}
