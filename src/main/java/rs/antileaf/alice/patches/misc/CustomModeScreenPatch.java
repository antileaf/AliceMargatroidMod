package rs.antileaf.alice.patches.misc;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.custom.CustomModeCharacterButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import rs.antileaf.alice.characters.AliceMargatroid;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;

import java.util.List;

@SuppressWarnings("unused")
public class CustomModeScreenPatch {
	@SpirePatch(clz = CustomModeCharacterButton.class, method = "updateHitbox", paramtypez = {})
	public static class PlayButtonSoundPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractPlayer.class,
								"getCustomModeCharacterButtonSoundKey"));
				return new int[]{tmp[tmp.length - 1]};
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static SpireReturn<Void> Insert(CustomModeCharacterButton _inst) {
			if (_inst.c.chosenClass == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS) {
				CardCrawlGame.sound.play(_inst.c.getCustomModeCharacterButtonSoundKey());
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = BaseMod.class, method = "publishAddCustomModeMods", paramtypez = {List.class})
	public static class CustomModeModNamePatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals(AbstractPlayer.class.getName()) &&
							m.getMethodName().equals("getLocalizedCharacterName"))
						m.replace("{ if ($0 instanceof " + AliceMargatroid.class.getName() +
								") { $_ = ((" + AliceMargatroid.class.getName() +
								") $0).getCustomModeModCharacterName(); } else { $_ = $proceed($$); } }");
				}
			};
		}
	}
}
