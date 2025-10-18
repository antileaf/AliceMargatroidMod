package me.antileaf.alice.patches.misc.qol;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import javassist.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class FasterPoisonLoseHpActionPatch {
	private static final Logger logger = LogManager.getLogger(FasterPoisonLoseHpActionPatch.class);

	@SpirePatch(clz = PoisonLoseHpAction.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Float> speed = new SpireField<>(() -> 1.0f);
		public static SpireField<Boolean> hasApplied = new SpireField<>(() -> false);
		public static SpireField<Boolean> noWait = new SpireField<>(() -> false);
	}

	public static void handle(PoisonLoseHpAction action) {
		if (!Fields.hasApplied.get(action)) {
			float duration = ReflectionHacks.getPrivate(action, AbstractGameAction.class, "duration");
			duration *= Fields.speed.get(action);
			ReflectionHacks.setPrivate(action, AbstractGameAction.class, "duration", duration);
			Fields.hasApplied.set(action, true);
		}
	}

	@SpirePatch(clz = PoisonLoseHpAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class ChangeDurationPatch {
		@SpireRawPatch
		public static void Raw(CtBehavior ctBehavior) throws CannotCompileException {
			CtClass ctClass = ctBehavior.getDeclaringClass();
//			System.out.println("ctClass = " + ctClass.toString());
			CtMethod method = CtNewMethod.make(CtClass.voidType, "tickDuration",
					null, null,
					"{ " + FasterPoisonLoseHpActionPatch.class.getName() +
							".handle(this); super.tickDuration(); }", ctClass);
			ctClass.addMethod(method);
		}
	}

	@SpirePatch(clz = PoisonLoseHpAction.class, method = "update", paramtypez = {})
	public static class NoWaitPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.NewExprMatcher(WaitAction.class));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(PoisonLoseHpAction _inst) {
			if (Fields.noWait.get(_inst))
				return SpireReturn.Return();
			else
				return SpireReturn.Continue();
		}
	}
}
