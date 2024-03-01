package rs.antileaf.alice.doll.targeting;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.TargetingHandler;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;

public abstract class AbstractAliceMagtroidTargeting extends TargetingHandler<Object> {
	public static AbstractCreature getTarget(AbstractCard card) {
		return CustomTargeting.getCardTarget(card);
	}
	
	protected Object hovered = null;
	
	protected AbstractDoll updateHoveredDoll() {
		for (AbstractDoll doll : DollManager.getInstance(AbstractDungeon.player).getDolls()) {
			doll.hb.update();
			
			if (doll.hb.hovered)
				return doll;
		}
		
		return null;
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
	public void renderReticle(SpriteBatch sb) {
		if (this.hovered != null) {
			if (this.hovered instanceof AbstractDoll)
				((AbstractDoll) this.hovered).renderReticle(sb);
			else if (this.hovered instanceof AbstractCreature)
				((AbstractCreature) this.hovered).renderReticle(sb);
			else
				assert false : "Invalid hovered object type";
		}
	}
}
