package rs.antileaf.alice.powers.interfaces;

import rs.antileaf.alice.doll.AbstractDoll;

public interface OnDollOperatePower {
	// Called after the doll is spawned.
	default void postSpawnDoll(AbstractDoll doll) {}
	
	default void postDollAct(AbstractDoll doll) {}
	
	default void postDollGainedBlock(AbstractDoll doll, int block) {}
	
	default void postRecycleDoll(AbstractDoll doll) {}
	
	default void postDestroyDoll(AbstractDoll doll) {}
	
	default void postRecycleOrDestroyDoll(AbstractDoll doll) {}
}
