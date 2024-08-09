package rs.antileaf.alice.patches.doll;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.CardTargetEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

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
	
	@SpirePatches({
			@SpirePatch(clz = Ironclad.class, method = "renderOrb"),
			@SpirePatch(clz = TheSilent.class, method = "renderOrb"),
			@SpirePatch(clz = Defect.class, method = "renderOrb"),
			@SpirePatch(clz = Watcher.class, method = "renderOrb")
	})
	public static class RenderOriginalPlayerDollPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst, SpriteBatch sb,
		                           boolean enabled, float current_x, float current_y) {
			DollManager.getInstance((AbstractPlayer) _inst).render(sb);
		}
	}

	@SpirePatch(clz = CustomPlayer.class, method = "renderOrb")
	public static class RenderModPlayerDollPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCreature _inst, SpriteBatch sb,
		                           boolean enabled, float current_x, float current_y) {
			DollManager.getInstance((AbstractPlayer) _inst).render(sb);
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
				AliceSpireKit.log("Show health bar!");
				
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
				AliceSpireKit.log("Hide health bar!");
				for (AbstractDoll doll : DollManager.getInstance((AbstractPlayer) _inst).getDolls()) {
					doll.hideHealthBar();
				}
			}
		}
	}
	
	@SpirePatch(clz = AbstractPlayer.class, method = "renderHoverReticle")
	public static class RenderHoverReticleOfDollsPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst, SpriteBatch sb) {
			AbstractCard hoveredCard = _inst.hoveredCard;
			
			if (CardTargetEnum.isDollTarget(hoveredCard.target)) {
				AbstractDoll doll = DollManager.get().getHoveredDoll();
				if (doll != null) {
					if (hoveredCard.target == CardTargetEnum.DOLL_OR_EMPTY_SLOT || !(doll instanceof EmptyDollSlot))
						doll.renderReticle(sb);
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
	
	@SpirePatch(clz = GameActionManager.class, method = "callEndOfTurnActions")
	public static class EndOfTurnLockDamageTargetPatch {
		@SpirePrefixPatch
		public static void Prefix(GameActionManager _inst) {
			DollManager.get().onEndOfTurnLockDamageTarget();
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
}
