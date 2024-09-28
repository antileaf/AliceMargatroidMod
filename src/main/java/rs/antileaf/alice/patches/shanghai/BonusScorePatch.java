package rs.antileaf.alice.patches.shanghai;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.characters.AliceMargatroid;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class BonusScorePatch {
	public static boolean BONUS = false;
	public static boolean IS_ALICE = false;
	
	@SpirePatch(clz = GameOverScreen.class, method = "resetScoreChecks")
	public static class GameOverScreenResetPatch {
		@SpirePostfixPatch
		public static void Postfix() {
			BONUS = false;
			IS_ALICE = false;
		}
	}
	
	@SpirePatch(clz = GameOverScreen.class, method = "checkScoreBonus", paramtypez = {boolean.class})
	public static class GameOverScreenCheckBonusPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic"));
				return new int[]{tmp[0]};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class, localvars = {"points"})
		public static void Insert(boolean victory, @ByRef int[] points) {
			IS_ALICE = (AbstractDungeon.player instanceof AliceMargatroid);

			if (AliceSpireKit.getSaveData().getReturnedShanghaiDoll()) {
				BONUS = true;
				points[0] += 1;
			}
			else
				BONUS = false;
		}
	}

	private static ScoreBonusStrings getBonusStrings() {
		return CardCrawlGame.languagePack.getScoreString("AliceReturnedShanghaiDoll");
	}

	private static GameOverStat getStat() {
		ScoreBonusStrings bonusStrings = getBonusStrings();
		return new GameOverStat(bonusStrings.NAME,
				bonusStrings.DESCRIPTIONS[IS_ALICE ? 1 : 0],
				Integer.toString(1));
	}
	
	@SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
	public static class VictoryScreenPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
//				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "bossCount"));
//				return new int[]{tmp[0] + 2}; // 代码是分两行写的，跟 IDEA 反编译的结果不一样
//			}
//		}

		@SpireInsertPatch(rloc = 65) // 矢野你有毛病啊，一行代码分五行写
		public static void Insert(VictoryScreen _inst, ArrayList<GameOverStat> ___stats) {
			if (BONUS)
				___stats.add(getStat());
		}
	}
	
	@SpirePatch(clz = DeathScreen.class, method = "createGameOverStats")
	public static class DeathScreenPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
//				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "bossCount"));
//				return new int[]{tmp[0] + 2};
//			}
//		}

		@SpireInsertPatch(rloc = 65) // 矢野你有毛病啊，一行代码分五行写
		public static void Insert(DeathScreen _inst, ArrayList<GameOverStat> ___stats) {
			if (BONUS)
				___stats.add(getStat());
		}
	}
}
