package me.antileaf.alice.patches.card.unique;

import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.alice.cardmodifier.ForbiddenMagicVFXCardModifier;

@SuppressWarnings("unused")
public class ForbiddenMagicVFXRemovalPatch {
	@SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers", paramtypez = {})
	public static class RemoveForbiddenMagicVFXAtEndOfTurn {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst) {
			if (_inst.isPlayer) {
				for (CardGroup group : new CardGroup[] {
						AbstractDungeon.player.hand,
						AbstractDungeon.player.drawPile,
						AbstractDungeon.player.discardPile,
						AbstractDungeon.player.exhaustPile
				})
					group.group.forEach(c ->
							CardModifierManager.removeModifiersById(c,
									ForbiddenMagicVFXCardModifier.ID, true));
			}
		}
	}
}
