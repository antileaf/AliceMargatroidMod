package me.antileaf.alice.patches.doll;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.Su_san;
import me.antileaf.alice.patches.misc.qol.FasterPoisonLoseHpActionPatch;
import me.antileaf.alice.utils.AliceHelper;

@SuppressWarnings("unused")
public class SusanPassiveEffectPatch {
	@SpirePatch(clz = PoisonPower.class, method = "atStartOfTurn", paramtypez = {})
	public static class PoisonPowerAtStartOfTurnPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.NewExprMatcher(PoisonLoseHpAction.class));
//			}
//		}

		public static boolean handle(PoisonPower _inst, AbstractGameAction lastAction) {
//			if (!(lastAction instanceof PoisonLoseHpAction))
//				return;

			int count = (int) DollManager.get().getDolls().stream()
					.filter(d -> d instanceof Su_san)
					.count();

			if (count > 0) {
				FasterPoisonLoseHpActionPatch.Fields.speed.set(lastAction, 0.5F);

				final AbstractMonster m = (AbstractMonster) _inst.owner;
				final AbstractCreature source = ReflectionHacks.getPrivate(_inst, PoisonPower.class, "source");

				for (int i = 0; i < count; i++)
					AliceHelper.addToBot(new AnonymousAction(() -> {
						if (!m.isDeadOrEscaped() && m.hasPower(PoisonPower.POWER_ID)) {
							int amount = m.getPower(PoisonPower.POWER_ID).amount;
							if (amount > 0) {
								PoisonLoseHpAction action = new PoisonLoseHpAction(m, source, amount,
										AbstractGameAction.AttackEffect.POISON);
								FasterPoisonLoseHpActionPatch.Fields.speed.set(action, 0.5F);
								FasterPoisonLoseHpActionPatch.Fields.noWait.set(action, true);
								AliceHelper.addToTop(action);
							}
						}
					}));

				AliceHelper.addToBot(new AnonymousAction(() -> {
					if (!m.isDeadOrEscaped() && m.hasPower(PoisonPower.POWER_ID)) {
						int amount = m.getPower(PoisonPower.POWER_ID).amount;
						if (amount > 0) {
							lastAction.amount = amount;
							AliceHelper.addToTop(lastAction);
						}
						else
							AliceHelper.addToTop(new WaitAction(0.1F));
					}
				}));

				return false;
			}
			else
				return true;
		}

		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("addToBot"))
						m.replace("{ if (" + PoisonPowerAtStartOfTurnPatch.class.getName() +
								".handle(this, $1)) { $proceed($$); } }");
				}
			};
		}
	}

	// Faster PoisonLoseHpAction is implemented in qol patches.
}
