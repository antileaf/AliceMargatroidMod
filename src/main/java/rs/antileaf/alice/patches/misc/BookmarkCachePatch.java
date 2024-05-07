package rs.antileaf.alice.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import rs.antileaf.alice.cards.AliceMargatroid.Bookmark;

public class BookmarkCachePatch {
	@SpirePatch(
			clz = AbstractRoom.class,
			method = "applyEndOfTurnPreCardPowers",
			paramtypez = {}
	)
	public static class EndOfTurnUpdateCachePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractRoom _inst) {
			Bookmark.updateCache();
		}
	}
}
