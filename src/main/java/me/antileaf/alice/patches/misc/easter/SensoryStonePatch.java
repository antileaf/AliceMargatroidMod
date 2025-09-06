package me.antileaf.alice.patches.misc.easter;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.alice.characters.AliceMargatroid;
import me.antileaf.alice.utils.AliceHelper;

public class SensoryStonePatch {
//	private static float alternateAnimateTimer = 0.0F;
	
	@SpirePatch(clz = SensoryStone.class, method = "reward", paramtypez = {int.class})
	public static class ReplaceImagePatch {
		@SpirePrefixPatch
		public static void Prefix(SensoryStone _inst, int num) {
			if (num != 0 && AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.player instanceof AliceMargatroid) {
				_inst.imageEventText.loadImage(AliceHelper.getEventImgFilePath("AliceSensoryStone"));
				AliceHelper.log("SensoryStonePatch",
						"Loading image: " + AliceHelper.getEventImgFilePath("AliceSensoryStone"));
				Color new_color = Color.WHITE.cpy();
				new_color.a = 0.0F;
				ReflectionHacks.setPrivate(_inst.imageEventText, GenericEventDialog.class,
						"imgColor", new_color);
			}
		}
	}
	
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
