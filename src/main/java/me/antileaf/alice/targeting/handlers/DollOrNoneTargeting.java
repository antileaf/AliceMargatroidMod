package me.antileaf.alice.targeting.handlers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollOrNoneTargeting extends TargetingHandler<AbstractDoll> {
	public static AbstractDoll getTarget(AbstractCard card) {
		return CustomTargeting.getCardTarget(card);
	}
	
	private AbstractDoll hovered = null;
	private AbstractDoll lastHovered = null;
	
	@Override
	public void updateHovered() {
		this.hovered = DollManager.get().getHoveredDoll();
		
		if (this.hovered instanceof EmptyDollSlot)
			this.hovered = null;
		
		if (this.hovered != null)
			this.lastHovered = this.hovered;
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

	@Override
	public void setDefaultTarget() {
		this.hovered = null;
		this.lastHovered = null;
	}

	@Override
	public int getDefaultTargetX() {
		return (int) AbstractDungeon.player.hb.cX;
	}

	@Override
	public int getDefaultTargetY() {
		return (int) AbstractDungeon.player.hb.cY;
	}
	
	@Override
	public void updateKeyboardTarget() {
		AbstractDoll newTarget;
		
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
			if (this.hovered != null)
				newTarget = direction > 0 ? DollManager.get().getPrevDoll(this.hovered) :
						DollManager.get().getNextDoll(this.hovered);
			else
				newTarget = direction > 0 ? DollManager.get().getLastDoll() : DollManager.get().getFirstDoll();
		}
		else if (InputActionSet.up.isJustPressed() || // 矢野你是人？
				CInputActionSet.down.isJustPressed() ||
				CInputActionSet.altDown.isJustPressed() ||
				CInputActionSet.peek.isJustPressed()) {
			if (this.hovered == null) {
				if (this.lastHovered == null)
					this.lastHovered = DollManager.get().getFirstDoll();
				
				newTarget = this.lastHovered;
			}
			else
				newTarget = null;
		}
		else
			newTarget = this.hovered;
		
		if (newTarget != this.hovered) {
			this.hovered = newTarget;
			
			if (this.hovered != null)
				this.lastHovered = this.hovered;
			
			Hitbox targetHb = this.hovered == null ? AbstractDungeon.player.hb : this.hovered.hb;
			Gdx.input.setCursorPosition((int) targetHb.cX, Settings.HEIGHT - (int) targetHb.cY);
			
			ReflectionHacks.setPrivate(AbstractDungeon.player, AbstractPlayer.class,
					"isUsingClickDragControl", true);
			AbstractDungeon.player.isDraggingCard = true;
		}
	}
}
