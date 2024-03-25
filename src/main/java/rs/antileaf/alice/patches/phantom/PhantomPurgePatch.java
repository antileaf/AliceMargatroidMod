package rs.antileaf.alice.patches.phantom;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.cardmodifier.PhantomCardModifier;
import rs.antileaf.alice.utils.AliceSpireKit;

public class PhantomPurgePatch {
//	@SpirePatch(clz = UseCardAction.class, method = "update")
//	public static class UseCardPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static SpireReturn<Void> Insert(UseCardAction _inst, AbstractCard ___targetCard) {
//			if (PhantomCardModifier.check(___targetCard)) {
//				AliceSpireKit.addEffect(new ExhaustCardEffect(___targetCard));
//				AliceSpireKit.addToBot(new HandCheckAction());
//				ReflectionHacks.privateMethod(AbstractGameAction.class, "tickDuration")
//						.invoke(_inst);
//
//				return SpireReturn.Return();
//			}
//			else
//				return SpireReturn.Continue();
//		}
//	}
	
	@SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile")
	public static class MoveToDiscardPilePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(CardGroup.class, "resetCardBeforeMoving"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(CardGroup _inst, AbstractCard c) {
			if (PhantomCardModifier.check(c)) {
				c.darken(false);
				AliceSpireKit.addEffect(new ExhaustCardEffect(c));
				AliceSpireKit.addToBot(new HandCheckAction());
				AbstractDungeon.player.onCardDrawOrDiscard();
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
	public static class MoveToExhaustPilePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(AbstractPlayer.class, "exhaustPile"));
				return new int[]{tmp[tmp.length - 1] - 1};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(CardGroup _inst, AbstractCard c) {
			if (PhantomCardModifier.check(c)) {
				c.darken(false);
				AbstractDungeon.player.onCardDrawOrDiscard();
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = CardGroup.class, method = "moveToDeck")
	public static class MoveToDeckPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(CardGroup.class, "resetCardBeforeMoving"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(CardGroup _inst, AbstractCard c, boolean randomSpot) {
			if (PhantomCardModifier.check(c)) {
				c.darken(false);
				AliceSpireKit.addEffect(new ExhaustCardEffect(c));
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = CardGroup.class, method = "moveToBottomOfDeck")
	public static class MoveToBottomOfDeckPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(CardGroup.class, "resetCardBeforeMoving"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(CardGroup _inst, AbstractCard c) {
			if (PhantomCardModifier.check(c)) {
				c.darken(false);
				AliceSpireKit.addEffect(new ExhaustCardEffect(c));
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = ExhaustCardEffect.class, method = "render")
	public static class ExhaustEffectUpdateTransparencyPatch {
		@SpirePrefixPatch
		public static void Prefix(ExhaustCardEffect _inst, SpriteBatch sb, AbstractCard ___c) {
			ReflectionHacks.privateMethod(AbstractCard.class, "updateTransparency").invoke(___c);
		}
	}
	
	@SpirePatch(clz = ExhaustCardEffect.class, method = "update")
	public static class ExhaustEffectStopGlowingPatch {
		@SpirePrefixPatch
		public static void Prefix(ExhaustCardEffect _inst, AbstractCard ___c) {
			if (_inst.duration == 1.0F)
				___c.stopGlowing();
		}
	}
}
