package me.antileaf.alice.patches.doll;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.utils.AliceHelper;

import java.util.WeakHashMap;

@SuppressWarnings("unused")
public class DollMechanicsPatch {
//	@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CONSTRUCTOR)
//	public static class InitDollManagerPatch {
//		@SpirePostfixPatch
//		public static void Postfix(AbstractPlayer _inst, String name, AbstractPlayer.PlayerClass clz) {
//			DollManager.getInstance(_inst);
//		}
//	}
	
//	@SpirePatch(clz = AbstractPlayer.class, method = "update")
//	public static class UpdateDollManagerPatch {
//		@SpirePostfixPatch
//		public static void Postfix(AbstractPlayer _inst) {
//			DollManager.getInstance(_inst).update();
//		}
//	}
	
//	@SpirePatches({
//			@SpirePatch(clz = Ironclad.class, method = "renderOrb"),
//			@SpirePatch(clz = TheSilent.class, method = "renderOrb"),
//			@SpirePatch(clz = Defect.class, method = "renderOrb"),
//			@SpirePatch(clz = Watcher.class, method = "renderOrb")
//	})
//	public static class RenderOriginalPlayerDollPatch {
//		@SpirePostfixPatch
//		public static void Postfix(AbstractCreature _inst, SpriteBatch sb,
//		                           boolean enabled, float current_x, float current_y) {
//			DollManager.getInstance((AbstractPlayer) _inst).render(sb);
//		}
//	}
//
//	@SpirePatch(clz = CustomPlayer.class, method = "renderOrb")
//	public static class RenderModPlayerDollPatch {
//		@SpirePostfixPatch
//		public static void Postfix(AbstractCreature _inst, SpriteBatch sb,
//		                           boolean enabled, float current_x, float current_y) {
//			DollManager.getInstance((AbstractPlayer) _inst).render(sb);
//		}
//	}
	
	@SpirePatch(clz = AbstractPlayer.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class RenderPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] orbs = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(AbstractPlayer.class, "orbs"));
				int[] inst = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.InstanceOfMatcher(RestRoom.class));
				for (int line : inst)
					if (line > orbs[orbs.length - 1])
						return new int[] {line};
				
				throw new PatchingException("DollMechanicsPatch.RenderPatch.Locator failed to find insertion point.");
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractPlayer _inst, SpriteBatch sb) {
			DollManager.getInstance(_inst).render(sb);
		}
	}
	
	@SpirePatch(clz = AbstractCreature.class, method = "updateHealthBar")
	public static class UpdateHealthBarOfDollsPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst) {
			if (_inst instanceof AbstractPlayer)
				DollManager.getInstance((AbstractPlayer) _inst).updateHealthBar();
		}
	}
	
	@SpirePatch(clz = AbstractCreature.class, method = "healthBarUpdatedEvent")
	public static class HealthBarUpdatedEventOfDollsPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst) {
			assert !(_inst instanceof AbstractPlayer);
			
//			for (AbstractDoll doll : DollManager.getInstance((AbstractPlayer) _inst).getDolls()) {
//				doll.healthBarUpdatedEvent();
//			}
		}
	}
	
	@SpirePatch(clz = AbstractCreature.class, method = "showHealthBar")
	public static class ShowHealthBarOfDollsPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst) {
			if (_inst instanceof AbstractPlayer) {
				AliceHelper.log("Show health bar!");
				
				for (AbstractDoll doll : DollManager.getInstance((AbstractPlayer) _inst).getDolls()) {
					doll.showHealthBar();
				}
			}
		}
	}
	
	@SpirePatch(clz = AbstractCreature.class, method = "hideHealthBar")
	public static class HideHealthBarOfDollsPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst) {
			if (_inst instanceof AbstractPlayer) {
//				AliceHelper.log("Hide health bar!");
				for (AbstractDoll doll : DollManager.getInstance((AbstractPlayer) _inst).getDolls()) {
					doll.hideHealthBar();
				}
			}
		}
	}
	
	@SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
	public static class EndOfTurnTriggerPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst) {
			if (_inst instanceof AbstractPlayer)
				DollManager.getInstance((AbstractPlayer) _inst).onEndOfTurn();
		}
	}
	
	@SpirePatch(clz = AbstractRoom.class, method = "endTurn")
	public static class EndOfTurnLockDamageTargetPatch {
		@SpireInsertPatch(rloc = 24)
		public static void Insert(AbstractRoom _inst) {
			AliceHelper.addToBot(new AnonymousAction(() -> {
				DollManager.get().onEndOfTurnLockDamageTarget();
			}));
		}
	}
	
//	@SpirePatch(clz = DamageAction.class, method = "update")
//	public static class DollDamageApplyPowersPatch {
//		@SpirePrefixPatch
//		public static void Prefix(DamageAction _inst) {
//			DamageInfo info = null;
//			try {
//				info = (DamageInfo) AliceReflectKit.getField(_inst.getClass(), "info").get(_inst);
//			}
//			catch (IllegalAccessException e) {
//				AliceSpireKit.log(DollDamageApplyPowersPatch.class, "Failed to access damage info.");
//			}
//
//			if (info == null) {
//				AliceSpireKit.log(DollDamageApplyPowersPatch.class, "Damage info is null.");
//				return;
//			}
//
//			boolean shouldCancelAction = (_inst.target == null ||
//					_inst.source != null && _inst.source.isDying ||
//					_inst.target.isDeadOrEscaped());
//			if (shouldCancelAction || info.type != DamageInfo.DamageType.THORNS)
//				return;
//
//			AliceSpireKit.log(DollDamageApplyPowersPatch.class, "Patching...");
//
//			if (info instanceof DollDamageInfo)
//				info.applyPowers(_inst.source, _inst.target);
//
//			AliceSpireKit.log(DollDamageApplyPowersPatch.class, "Patched damage = " + info.output);
//		}
//	}

	@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
	public static class DamageInfoField {
		public static WeakHashMap<DamageInfo, Boolean> blockedByDoll = new WeakHashMap<>();
	}

	@SpirePatch(clz = AbstractPlayer.class, method = "damage",
			paramtypez = {DamageInfo.class})
	public static class OnDamagedDoNotShowEffectPatch {
		@SpireInsertPatch(rlocs = {162, 164, 170})
		public static SpireReturn<Void> Insert(AbstractPlayer _inst, DamageInfo info) {
			if (DamageInfoField.blockedByDoll.getOrDefault(info, false))
				return SpireReturn.Return(null);
			else
				return SpireReturn.Continue();
		}
	}
}
