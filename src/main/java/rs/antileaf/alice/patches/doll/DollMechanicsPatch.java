package rs.antileaf.alice.patches.doll;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import rs.antileaf.alice.doll.DollManager;

public class DollMechanicsPatch {
//	@SpirePatch(clz = AbstractPlayer.class, method = SpirePatch.CONSTRUCTOR)
//	public static class InitDollManagerPatch {
//		@SpirePostfixPatch
//		public static void Postfix(AbstractPlayer _inst, String name, AbstractPlayer.PlayerClass clz) {
//			DollManager.getInstance(_inst);
//		}
//	}
	
	@SpirePatch(clz = AbstractPlayer.class, method = "update")
	public static class UpdateDollManagerPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst) {
			DollManager.getInstance(_inst).update();
		}
	}
	
	@SpirePatches({
			@SpirePatch(clz = Ironclad.class, method = "renderOrb"),
			@SpirePatch(clz = TheSilent.class, method = "renderOrb"),
			@SpirePatch(clz = Defect.class, method = "renderOrb"),
			@SpirePatch(clz = Watcher.class, method = "renderOrb")
	})
	public static class RenderOldPlayerDollPatch {
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
}
