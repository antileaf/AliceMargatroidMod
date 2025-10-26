package me.antileaf.alice.patches.card.unique;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import me.antileaf.alice.doll.DollManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.WeakHashMap;

@SuppressWarnings("unused")
public class UsokaeCompatibilityPatch {
	private static final Logger logger = LogManager.getLogger(UsokaeCompatibilityPatch.class.getName());

	public static final WeakHashMap<AbstractPower, Integer> patched = new WeakHashMap<>();

	public static void patch(AbstractCreature source, AbstractPower power) {
		if (!(source instanceof AbstractMonster))
			return;

		int index = DollManager.get().damageTarget.getOrDefault(source, -1);
		if (index != -1) {
			patched.put(power, index);
			logger.info("Patched: {}, index = {}", power, index);
		}
	}

	@SpirePatch(clz = SporeCloudPower.class, method = "onDeath", paramtypez = {})
	public static class SporeCloudPowerPatch {
//		@SpireInstrumentPatch
//		public static ExprEditor Instrument() {
//			return new ExprEditor() {
//				@Override
//				public void edit(ConstructorCall c) throws CannotCompileException {
//					System.out.println("getClassName() = " + c.getClassName());
//
//					if (c.getClassName().equals(ApplyPowerAction.class.getName()))
//						c.replace("{ " + UsokaeCompatibilityPatch.class.getName() +
//								".patch(this.owner, $3); $_ = proceed($$); }");
//				}
//			};
//		}

		@SpirePostfixPatch
		public static void Postfix(SporeCloudPower _inst) {
			if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
				if (!AbstractDungeon.actionManager.actions.isEmpty() &&
						AbstractDungeon.actionManager.actions.get(0) instanceof ApplyPowerAction) {
					ApplyPowerAction action = (ApplyPowerAction) AbstractDungeon.actionManager.actions.get(0);
					AbstractPower power = ReflectionHacks.getPrivate(action, ApplyPowerAction.class,
							"powerToApply");
					if (power instanceof VulnerablePower)
						patch(_inst.owner, power);
				}
			}
		}
	}
}
