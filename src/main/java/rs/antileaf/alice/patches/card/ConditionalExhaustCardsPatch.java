package rs.antileaf.alice.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.random.Random;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.cards.interfaces.ConditionalExhaustCard;

import java.util.Arrays;

@SuppressWarnings("unused")
public class ConditionalExhaustCardsPatch {
	@SpirePatch(
			clz = UseCardAction.class,
			method = "update"
	)
	public static class UseCardActionUpdatePatch {
		public static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] a = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic"));
				int[] b = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Random.class, "randomBoolean"));
				
				int[] intersect = Arrays.stream(a)
						.filter(x -> Arrays.stream(b).anyMatch(y -> x == y - 1))
						.toArray();
				
				return new int[]{intersect[0] - 2};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(UseCardAction _inst, AbstractCard ___targetCard) {
			if (!(___targetCard instanceof ConditionalExhaustCard))
				return;
			
			_inst.exhaustCard |= ((ConditionalExhaustCard) ___targetCard).shouldExhaust();
		}
	}
}
