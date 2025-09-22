package me.antileaf.alice.patches.misc.compatibility;

import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

public class CustomCardColorTabNameFixPatch {
	@SpirePatch(clz = ColorTabBarFix.Render.class, method = "Insert",
			paramtypez = {ColorTabBar.class, SpriteBatch.class, float.class, ColorTabBar.CurrentTab.class},
			optional = true)
	public static class ColorTabBarFixRenderPatch {
		private static UIStrings uiStrings = null;
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class, localvars = {"tabName"})
		public static void Insert(ColorTabBar _inst, SpriteBatch sb, float y, ColorTabBar.CurrentTab tab,
										   @ByRef String[] tabName) {
			if (tabName[0].equalsIgnoreCase(AbstractCardEnum.ALICE_MARISA_COLOR.name())) {
				if (uiStrings == null)
					uiStrings = CardCrawlGame.languagePack.getUIString(AliceHelper.makeID(
							AbstractCardEnum.ALICE_MARISA_COLOR.name()));
				
				tabName[0] = uiStrings.TEXT[0];
			}
		}
	}
}
