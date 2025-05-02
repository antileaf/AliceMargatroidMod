package me.antileaf.alice.save;

public class AliceSaveData {
	private boolean hasTriggeredShanghaiDollEvent = false;
	private boolean returnedShanghaiDoll = false;

	public boolean getHasTriggeredShanghaiDollEvent() {
		return hasTriggeredShanghaiDollEvent;
	}

	public void setHasTriggeredShanghaiDollEvent(boolean value) {
		hasTriggeredShanghaiDollEvent = value;
	}

	public boolean getReturnedShanghaiDoll() {
		return returnedShanghaiDoll;
	}

	public void setReturnedShanghaiDoll(boolean value) {
		returnedShanghaiDoll = value;
	}
}
