package me.antileaf.alice.patches.misc.controller;

import com.badlogic.gdx.controllers.Controller;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.controller.CInputListener;
import me.antileaf.alice.utils.AliceControllerHelper;

@SuppressWarnings("unused")
public class ControllerListenerPatch {
	@SpirePatch(clz = CInputListener.class, method = "axisMoved",
			paramtypez = {Controller.class, int.class, float.class})
	public static class AxisMovedPatch {
		@SpireInsertPatch(rloc = 43)
		public static void Insert(CInputListener _inst, Controller controller, int axisCode, float value) {
			AliceControllerHelper.receiveAxisMoved(controller, axisCode, value);
		}
	}
}
