package me.antileaf.alice.patches.skin;

import basemod.CustomCharacterSelectScreen;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.controller.CInputAction;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.alice.utils.AliceControllerHelper;

@SuppressWarnings("unused")
public class CharSelectControllerPatch {
	@SpirePatch(clz = MainMenuScreen.class, method = "updateCharSelectController", paramtypez = {})
	public static class UpdateCharSelectControllerPatch {
		@SpireInsertPatch(rloc = 20, localvars = {"index"})
		public static SpireReturn<Void> Insert(MainMenuScreen _inst, int index) {
			if (CharacterSelectScreenSkinPatch.isAliceSelected() &&
					AliceControllerHelper.isRightTriggerPressed()) {
				if (_inst.charSelectScreen.options.get(index).locked)
					_inst.charSelectScreen.confirmButton.hide();
				else
					_inst.charSelectScreen.confirmButton.show();
				
				return SpireReturn.Return();
			}
			
			return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = CustomCharacterSelectScreen.class, method = "updateCharSelectController", paramtypez = {})
	public static class BaseModFixPatch {
		@SpirePrefixPatch
		public static SpireReturn<Boolean> Prefix(CustomCharacterSelectScreen _inst) {
			if (CharacterSelectScreenSkinPatch.isAliceSelected() &&
					AliceControllerHelper.isRightTriggerPressed()) {
				return SpireReturn.Return(false);
			}
			
			return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(clz = CharacterSelectScreen.class, method = "updateButtons", paramtypez = {})
	public static class UpdateButtonsPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(InputHelper.class, "pressedEscape"));
			}
		}
		
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(CharacterSelectScreen _inst) {
			if (CharacterSelectScreenSkinPatch.isAliceSelected() &&
					AliceControllerHelper.isRightTriggerPressed()) {
				_inst.cancelButton.hb.clicked = false;
				_inst.confirmButton.hb.clicked = false;
				InputHelper.pressedEscape = false;
			}
		}
	}
	
	@SpirePatch(clz = ConfirmButton.class, method = "update", paramtypez = {})
	public static class ConfirmButtonPatch {
		public static boolean check() {
			return CardCrawlGame.mode == CardCrawlGame.GameMode.CHAR_SELECT &&
					CardCrawlGame.mainMenuScreen.screen == MainMenuScreen.CurScreen.CHAR_SELECT &&
					!((SeedPanel) ReflectionHacks.getPrivate(CardCrawlGame.mainMenuScreen.charSelectScreen,
							CharacterSelectScreen.class, "seedPanel")).shown &&
					CharacterSelectScreenSkinPatch.isAliceSelected() &&
					AliceControllerHelper.isRightTriggerPressed();
		}
		
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals(CInputAction.class.getName()) &&
							m.getMethodName().equals("isJustPressed")) {
						m.replace("{ $_ = (!" + ConfirmButtonPatch.class.getName() +
								".check()) && $proceed($$); }");
					}
				}
			};
		}
	}
}
