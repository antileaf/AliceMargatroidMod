package me.antileaf.alice.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameTips;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class GameTipsPatch {
	@SpirePatch(clz = GameTips.class, method = "initialize", paramtypez = {})
	public static class InitializePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Collections.class, "shuffle"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(GameTips _inst, ArrayList<String> ___tips) {
//			___tips.clear();

			Collections.addAll(___tips, CardCrawlGame.languagePack.getTutorialString(
					AliceHelper.makeID("Tips")).TEXT);
		}
	}
}
