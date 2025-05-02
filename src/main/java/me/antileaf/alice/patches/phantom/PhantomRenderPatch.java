package me.antileaf.alice.patches.phantom;

// This patch may get into performance issues, as it is called every frame.
public class PhantomRenderPatch {
//	@SpirePatch(clz = AbstractCard.class, method = "renderCard")
//	public static class AbstractCardRenderPatch {
//		private static Color temp = new Color();
//
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractCard.class, "renderGlow"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
//			if (CardModifierManager.hasModifier(_inst, PhantomCardModifier.ID)) {
//				temp = sb.getColor();
//				Color color = temp.cpy();
//				color.a = 0.8F;
//				sb.setColor(color);
//			}
//		}
//
//		@SpirePostfixPatch
//		public static void Postfix(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
//			if (CardModifierManager.hasModifier(_inst, PhantomCardModifier.ID)) {
//				sb.setColor(temp);
//			}
//		}
//	}
}
