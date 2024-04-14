package rs.antileaf.alice.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.characters.AliceMargatroid;

public class SensoryStonePatch {
	@SpirePatch(clz = SensoryStone.class, method = "getRandomMemory")
	public static class GetRandomMemoryPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(GenericEventDialog.class, "updateBodyText"));
				return new int[]{tmp[tmp.length - 1]};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(SensoryStone _inst) {
			if (AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player instanceof AliceMargatroid) {
				String text = ((AliceMargatroid) AbstractDungeon.player).getSensoryStoneText();
				_inst.imageEventText.updateBodyText(text);
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
}
