package me.antileaf.alice.patches.card.interfaces;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.alice.cards.interfaces.OnCreatedCard;

@SuppressWarnings("unused")
public class OnCreatedCardPatch {
	@SpirePatch(clz = StSLib.class, method = "onCreateCard", paramtypez = {AbstractCard.class})
	public static class SpawnInCombatPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractCard card) {
			if (card instanceof OnCreatedCard)
				((OnCreatedCard) card).onCreated();
		}
	}
}
