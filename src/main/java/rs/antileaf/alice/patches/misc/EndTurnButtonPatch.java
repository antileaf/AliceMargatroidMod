package rs.antileaf.alice.patches.misc;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import com.megacrit.cardcrawl.vfx.EndTurnGlowEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.powers.common.AliceExtraTurnPower;
import rs.antileaf.alice.utils.AliceImageMaster;
import rs.antileaf.alice.utils.AliceSpireKit;

// @SuppressWarnings("unused")
public class EndTurnButtonPatch {
	@SpirePatch(
			clz = EndTurnButton.class,
			method = "updateText",
			paramtypez = {String.class}
	)
	public static class UpdateTextPatch {
		@SpirePrefixPatch
		public static void Prefix(EndTurnButton _inst, @ByRef String[] msg) {
			if (msg[0].equals(EndTurnButton.END_TURN_MSG) && AliceSpireKit.isInBattle() &&
					AbstractDungeon.player.hasPower(AliceExtraTurnPower.POWER_ID)) {
				msg[0] = CardCrawlGame.languagePack.getUIString("AliceExtraTurnButton").TEXT[0];
				
//				AliceSpireKit.log("EndTurnButtonPatch", "Changed end turn button text to " + msg[0] + "!");
			}
		}
	}
	
//	@SpirePatch(
//			clz = EndTurnButton.class,
//			method = "render",
//			paramtypez = {SpriteBatch.class}
//	)
//	public static class RenderTextColorPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				int[] tmp = LineFinder.findInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(Hitbox.class, "clickStarted"));
//				return new int[]{tmp[0]};
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(EndTurnButton _inst) {
//			if (ReflectionHacks.getPrivate(_inst, EndTurnButton.class, "textColor")
//					.equals(Color.LIGHT_GRAY) && AliceSpireKit.isInBattle() &&
//					AbstractDungeon.player.hasPower(AliceExtraTurnPower.POWER_ID))
//				ReflectionHacks.setPrivate(
//						_inst,
//						EndTurnButton.class,
//						"textColor",
//						Color.GOLD.cpy()
//				);
//		}
//	}
	
	@SpirePatch(
			clz = EndTurnButton.class,
			method = "render",
			paramtypez = {SpriteBatch.class}
	)
	public static class RenderEndTurnButtonGlowPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(ImageMaster.class, "END_TURN_BUTTON_GLOW"));
				return new int[]{tmp[0] + 4};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class, localvars = "buttonImg")
		public static void Insert(EndTurnButton _inst, SpriteBatch sb, @ByRef Texture[] buttonImg) {
			if (buttonImg[0] == ImageMaster.END_TURN_BUTTON_GLOW &&
					AliceSpireKit.isInBattle() &&
					AbstractDungeon.player.hasPower(AliceExtraTurnPower.POWER_ID))
				buttonImg[0] = AliceImageMaster.GOLD_END_TURN_BUTTON_GLOW;
		}
	}
	
	@SpirePatch(
			clz = EndTurnGlowEffect.class,
			method = "render",
			paramtypez = {SpriteBatch.class, float.class, float.class}
	)
	public static class EndTurnGlowEffectPatch {
		public static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(SpriteBatch.class, "draw"));
				return new int[]{tmp[0]};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(EndTurnGlowEffect _inst, SpriteBatch sb, float x, float y) {
			if (AliceSpireKit.isInBattle() && AbstractDungeon.player.hasPower(AliceExtraTurnPower.POWER_ID)) {
				float scale = ReflectionHacks.getPrivate(_inst, EndTurnGlowEffect.class, "scale");
				sb.draw(
						AliceImageMaster.GOLD_END_TURN_BUTTON_GLOW,
						x - 128.0F,
						y - 128.0F,
						128.0F,
						128.0F,
						256.0F,
						256.0F,
						scale,
						scale,
						0.0F,
						0,
						0,
						256,
						256,
						false,
						false
				);
				sb.setBlendFunction(770, 771);
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
}
