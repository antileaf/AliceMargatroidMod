package rs.antileaf.alice.patches.phantom;

public class PhantomOriginalCardsPatch {
//	@SpirePatch(clz = DualWieldAction.class, method = "isDualWieldable")
//	public static class DualWieldPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<Boolean> Prefix(DualWieldAction _inst, AbstractCard card) {
//			if (PhantomCardModifier.check(card))
//				return SpireReturn.Return(false);
//			else
//				return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = NightmareAction.class, method = "update")
//	public static class NightmarePatch {
//		private static class BeforeFirstGetBottomCardLocator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return new int[] {LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(CardGroup.class, "getBottomCard"))[0] - 1};
//			}
//		}
//
//		@SpireInsertPatch(locator = BeforeFirstGetBottomCardLocator.class)
//		public static SpireReturn<Void> InsertBeforeFirstGetBottomCard(NightmareAction _inst) {
//			if (PhantomCardModifier.check(AbstractDungeon.player.hand.getBottomCard())) {
//				assert AbstractDungeon.player.hand.size() == 1;
//				_inst.isDone = true;
//				AliceSpireKit.log("Hello, Return() here!");
//				return SpireReturn.Return();
//			} else
//				return SpireReturn.Continue();
//		}
//
//		private static class BeforeFirstOpenLocator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return new int[] {LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(HandCardSelectScreen.class, "open"))[0] - 1};
//			}
//		}
//
//		private static ArrayList<AbstractCard> phantomCards = null;
//
//		@SpireInsertPatch(locator = BeforeFirstOpenLocator.class)
//		public static SpireReturn<Void> InsertBeforeFirstOpen(NightmareAction _inst) {
//			phantomCards = new ArrayList<>();
//			for (AbstractCard c : AbstractDungeon.player.hand.group) {
//				if (PhantomCardModifier.check(c))
//					phantomCards.add(c);
//			}
//
//			if (phantomCards.isEmpty()) {
//				phantomCards = null;
//				AliceSpireKit.log("Continue() here!");
//				return SpireReturn.Continue();
//			}
//			AliceSpireKit.log("Before first open, phantom cards: " + phantomCards.size());
//
//			if (phantomCards.size() == AbstractDungeon.player.hand.size()) {
//				_inst.isDone = true;
//				phantomCards = null;
//				return SpireReturn.Return();
//			}
//
//			if (AbstractDungeon.player.hand.size() - phantomCards.size() == 1) {
//				for (AbstractCard card : AbstractDungeon.player.hand.group) {
//					if (!phantomCards.contains(card)) {
//						AliceSpireKit.addToTop(new ApplyPowerAction(
//								AbstractDungeon.player,
//								AbstractDungeon.player,
//								new NightmarePower(AbstractDungeon.player, _inst.amount, card)
//						));
//						break;
//					}
//				}
//				_inst.isDone = true;
//				phantomCards = null;
//				return SpireReturn.Return();
//			}
//
//			AbstractDungeon.player.hand.group.removeAll(phantomCards);
//
////			if (AbstractDungeon.player.hand.size() == 1) {
////				AliceSpireKit.addToTop(new ApplyPowerAction(
////						AbstractDungeon.player,
////						AbstractDungeon.player,
////						new NightmarePower(AbstractDungeon.player, _inst.amount,
////								AbstractDungeon.player.hand.getTopCard())
////				));
////
////				for (AbstractCard c : phantomCards)
////					AbstractDungeon.player.hand.addToTop(c);
////				AbstractDungeon.player.hand.refreshHandLayout();
////
////				_inst.isDone = true;
////				return SpireReturn.Return();
////			}
//
//			return SpireReturn.Continue();
//		}
//
//		private static class AfterDoneLocator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				int[] retrieved = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "wereCardsRetrieved"));
//				int[] clear = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(CardGroup.class, "clear"));
//				return new int[] { Math.max(
//						retrieved[retrieved.length - 1],
//						clear[clear.length - 1]
//				) };
//			}
//		}
//
//		@SpireInsertPatch(locator = AfterDoneLocator.class)
//		public static void InsertAfterDone(NightmareAction _inst) {
//			if (phantomCards != null) {
//				AliceSpireKit.log("Returning phantom cards: " + phantomCards.size());
//
//				for (AbstractCard c : phantomCards)
//					AbstractDungeon.player.hand.addToTop(c);
//				AbstractDungeon.player.hand.refreshHandLayout();
//			}
//
//			phantomCards = null;
//		}
//	}
}
