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
	
	private AbstractDoll hovered = null;
	
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
	
	@Override
	public void setDefaultTarget() {
		this.hovered = DollManager.get().hasDoll() ? DollManager.get().getFirstDoll() : null;
	}
	
	@Override
	public int getDefaultTargetX() {
		return (int) (DollManager.get().hasDoll() ? DollManager.get().getFirstDoll().hb.cX :
				AbstractDungeon.player.hb.cX);
	}
	
	@Override
	public int getDefaultTargetY() {
		return (int) (DollManager.get().hasDoll() ? DollManager.get().getFirstDoll().hb.cY :
				AbstractDungeon.player.hb.cY);
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
			if (this.hovered != null) {
				newTarget = direction > 0 ? DollManager.get().getPrevDoll(this.hovered) :
						DollManager.get().getNextDoll(this.hovered);
				
				if (newTarget == null)
					newTarget = direction > 0 ? DollManager.get().getLastDoll() : DollManager.get().getFirstDoll();
			}
			else
				newTarget = direction > 0 ? DollManager.get().getFirstDoll() : DollManager.get().getLastDoll();
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
