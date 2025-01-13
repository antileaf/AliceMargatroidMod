package rs.antileaf.alice.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.alice.Bookmark;
import rs.antileaf.alice.utils.AliceHelper;

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
