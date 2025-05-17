package me.antileaf.alice.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.random.Random;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.cards.interfaces.ConditionalExhaustCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@SuppressWarnings("unused")
public class ConditionalExhaustCardsPatch {
	private static final Logger logger = LogManager.getLogger(ConditionalExhaustCardsPatch.class.getName());

	@SpirePatch(
			clz = UseCardAction.class,
			method = "update"
	)
	public static class UseCardActionUpdatePatch {
		private static class Locator extends SpireInsertLocator {
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

		@SpireInsertPatch(rloc = 48, localvars = {"spoonProc"})
		public static void SpoonInsert(UseCardAction _inst, AbstractCard ___targetCard,
									   @ByRef boolean[] spoonProc) {
			if (___targetCard instanceof ConditionalExhaustCard &&
					((ConditionalExhaustCard) ___targetCard).ignoreSpoon()) {
				if (spoonProc[0])
					logger.info("Spoon proc ignored for {}", ___targetCard.cardID);

				spoonProc[0] = false;
			}
		}
	}
}
