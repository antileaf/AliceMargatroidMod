package me.antileaf.alice.patches.card.unique;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.cards.alice.Perihelion;

@SuppressWarnings("unused")
public class PerihelionInitializePatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep", paramtypez = {})
	public static class InitializePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(CardGroup.class, "initializeDeck"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractPlayer _inst) {
			_inst.masterDeck.group.stream()
					.filter(c -> c instanceof Perihelion)
					.forEach(c -> ((Perihelion) c).setInitialState(null));
		}
	}
}
