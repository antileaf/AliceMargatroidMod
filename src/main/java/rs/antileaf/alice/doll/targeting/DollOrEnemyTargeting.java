package rs.antileaf.alice.doll.targeting;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DollOrEnemyTargeting extends TargetingHandler<Object> {
	public static Object getTarget(AbstractCard card) {
		Object target = CustomTargeting.getCardTarget(card);
		if (target == null)
			target = AbstractDungeon.getMonsters().getRandomMonster(true);
		return target;
	}
	
	protected Object hovered = null;
	
	@Override
	public void updateHovered() {
		this.hovered = DollManager.get().getHoveredDoll();
		if (this.hovered != null) {
			if (this.hovered instanceof EmptyDollSlot)
				this.hovered = null;
		}
		else {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (m.isDeadOrEscaped())
					continue;
				
				m.hb.update();
				if (m.hb.hovered) {
					this.hovered = m;
					break;
				}
			}
		}
	}
	
	@Override
	public Object getHovered() {
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
	public boolean isValidTarget(Object target) {
		if (target instanceof AbstractDoll)
			return !(target instanceof EmptyDollSlot) && DollManager.get().contains((AbstractDoll) target);
		else if (target instanceof AbstractMonster)
			return !((AbstractMonster) target).isDeadOrEscaped();
		else
			return false;
	}
}
