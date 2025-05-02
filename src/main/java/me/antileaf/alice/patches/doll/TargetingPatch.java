package me.antileaf.alice.patches.doll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.CardTargetEnum;
import me.antileaf.alice.targeting.AliceHoveredTargets;

@SuppressWarnings("unused")
public class TargetingPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "renderHoverReticle")
	public static class RenderHoverReticlePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst, SpriteBatch sb, AbstractMonster ___hoveredMonster) {
			AbstractCard hoveredCard = _inst.hoveredCard;
			
			if (CardTargetEnum.isDollTarget(hoveredCard.target)) {
				AbstractDoll doll = DollManager.get().getHoveredDoll();
				if (doll != null) {
					if (!(doll instanceof EmptyDollSlot) ||
							hoveredCard.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT ||
							hoveredCard.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_NONE ||
							hoveredCard.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT_OR_ENEMY)
						doll.renderReticle(sb);
				}
			}
			
			if (hoveredCard instanceof AbstractAliceCard) {
				AliceHoveredTargets o = ((AbstractAliceCard) hoveredCard).getHoveredTargets(
						___hoveredMonster, DollManager.get().getHoveredDoll());
				
				if (o.player)
					_inst.renderReticle(sb);
				
				for (AbstractMonster m : o.monsters)
					m.renderReticle(sb);
				
				for (AbstractDoll d : o.dolls)
					d.renderReticle(sb);
			}
		}
	}
}
