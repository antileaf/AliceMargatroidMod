package rs.antileaf.alice.patches.skin;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.characters.AliceMargatroid;
import rs.antileaf.alice.patches.enums.AbstractPlayerEnum;
import rs.antileaf.alice.ui.SkinSelectScreen;

@SuppressWarnings("unused")
public class CharacterSelectScreenSkinPatch {
//	public static boolean shouldUpdateBackground = false;
	
	public static boolean isAliceSelected() {
		return CardCrawlGame.chosenCharacter == AbstractPlayerEnum.ALICE_MARGATROID_PLAYER_CLASS &&
				(Boolean) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen,
						CharacterSelectScreen.class, "anySelected");
	}
	
	@SpirePatch(
			clz = CharacterSelectScreen.class,
			method = "render"
	)
	public static class RenderPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] cancelButton = LineFinder.findInOrder(ctBehavior,
					new Matcher.FieldAccessMatcher(CharacterSelectScreen.class, "cancelButton"));
				int[] buttonRender = LineFinder.findInOrder(ctBehavior,
					new Matcher.MethodCallMatcher(MenuCancelButton.class, "render"));
				
				if (cancelButton[0] != buttonRender[0])
					throw new PatchingException("Failed to find cancel button render call");
				
				return new int[]{buttonRender[0]};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(CharacterSelectScreen _inst, SpriteBatch sb) {
			if (isAliceSelected())
				SkinSelectScreen.inst.render(sb);
		}
	}
	
	@SpirePatch(
			clz = CharacterSelectScreen.class,
			method = "update"
	)
	public static class UpdateBackgroundPatch {
		@SpirePostfixPatch
		public static void Postfix(CharacterSelectScreen _inst) {
			if (isAliceSelected())
				SkinSelectScreen.inst.update();
			
			if (isAliceSelected() && SkinSelectScreen.shouldUpdateBackground) {
				_inst.bgCharImg = ImageMaster.loadImage(SkinSelectScreen.inst.getPortrait());
				SkinSelectScreen.shouldUpdateBackground = false;
			}
		}
	}
	
	@SpirePatch(
			clz = CharacterOption.class,
			method = "updateHitbox"
	)
	public static class ShouldUpdateBackgroundPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(
								AbstractPlayer.class, "doCharSelectScreenSelectEffect"));
				
				return new int[]{tmp[tmp.length - 1] - 2};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(CharacterOption _inst) {
			if (_inst.c instanceof AliceMargatroid) {
				SkinSelectScreen.shouldUpdateBackground = true;
			}
		}
	}
}
