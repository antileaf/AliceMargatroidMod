package rs.antileaf.alice.interfaces;

import rs.antileaf.alice.doll.AbstractDoll;

public interface OnDollOperateHook {
	// Called before the doll is placed into the doll queue.
	default void onSpawnDoll(AbstractDoll doll) {}
	
	default void onDollAct(AbstractDoll doll) {}
	
	default void onRecycleDoll(AbstractDoll doll) {}
	
	default void onDestroyDoll(AbstractDoll doll) {}
}
