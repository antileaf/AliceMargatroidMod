package me.antileaf.alice.patches.card.unique;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.alice.Bookmark;
import me.antileaf.alice.utils.AliceHelper;

@SuppressWarnings("unused")
public class BookmarkCachePatch {
	@SpirePatch(
			clz = AbstractPlayer.class,
			method = "applyStartOfTurnPostDrawRelics",
			paramtypez = {}
	)
	public static class StartOfTurnUpdateCachePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst) {
			AliceHelper.addToBot(new AnonymousAction(Bookmark::updateCache));
		}
	}
	
//	@SpirePatch(
//			clz = AbstractPlayer.class,
//			method = "applyStartOfCombatLogic",
//			paramtypez = {}
//	)
//	public static class StartOfCombatUpdateCachePatch {
//		@SpirePostfixPatch
//		public static void Postfix(AbstractPlayer _inst) {
//			AliceSpireKit.addToBot(new AnonymousAction(Bookmark::updateCache));
//		}
//	}
}
