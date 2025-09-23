package me.antileaf.alice.patches.misc.qol;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.doll.DollManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class FasterDollActionsPatch {
	private static final Logger logger = LogManager.getLogger(FasterDollActionsPatch.class);
	
	@SpirePatch(clz = GameActionManager.class, method = "update", paramtypez = {})
	public static class GameActionManagerUpdatePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(GameActionManager.class, "previousAction"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(GameActionManager _inst) {
			logger.debug("doll action is done. counter += 1.");
			DollManager.get().addDollActionCounter();
		}
	}
	
	@SpirePatch(clz = GameActionManager.class, method = "getNextAction", paramtypez = {})
	public static class GameActionManagerGetNextActionPatch {
		@SpirePrefixPatch
		public static void Prefix(GameActionManager _inst) {
			if (_inst.actions.isEmpty()) {
				logger.debug("actions is empty, clearing doll actions.");
				DollManager.get().clearDollActions();
			}
		}
	}
	
	@SpirePatch(clz = AbstractGameAction.class, method = "tickDuration", paramtypez = {})
	public static class TickDurationPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractGameAction _inst) {
			if (DollManager.get().isDollAction(_inst) && !DollManager.get().hasApplied(_inst)) {
				DollManager.get().applyDollActionSpeed(_inst);
				DollManager.get().setApplied(_inst);
			}
		}
	}
}
