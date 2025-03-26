package rs.antileaf.alice.doll.interfaces;

import rs.antileaf.alice.doll.AbstractDoll;

// A hook interface for both powers and relics.
// Relic hooks will be called before power hooks.
public interface OnDollOperateHook {
	default void preSpawnDoll(AbstractDoll doll) {}
	
	default void postSpawnDoll(AbstractDoll doll) {}

	default void preDollAct(AbstractDoll doll) {}
	
	default void postDollAct(AbstractDoll doll) {}
	
	default void postDollGainedBlock(AbstractDoll doll, int block) {}
	
	default void postRecycleDoll(AbstractDoll doll) {}
	
	default void postDestroyDoll(AbstractDoll doll) {}
	
	default void postRecycleOrDestroyDoll(AbstractDoll doll) {}

	@Deprecated
	default boolean canWorkOnSpecialAct() {
		return true;
	}
}
