package me.antileaf.alice.utils;

import com.badlogic.gdx.controllers.Controller;

public class AliceControllerHelper { // Implemented in ControllerListenerPatch
	private static float[] axisValues = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
	
	public static void receiveAxisMoved(Controller controller, int axisCode, float value) {
		if (axisCode >= 0 && axisCode < axisValues.length)
			axisValues[axisCode] = value;
	}
	
	public static float getAxisValue(int axisCode) {
		if (axisCode >= 0 && axisCode < axisValues.length)
			return axisValues[axisCode];
		
		return 0.0F;
	}
	
	public static float getLeftTriggerValue() {
		return Math.max(axisValues[4], 0.0F);
	}
	
	public static boolean isLeftTriggerPressed() {
		return getLeftTriggerValue() > 0.5F;
	}
	
	public static float getRightTriggerValue() {
		return -Math.min(axisValues[4], 0.0F);
	}
	
	public static boolean isRightTriggerPressed() {
		return getRightTriggerValue() > 0.5F;
	}
}
