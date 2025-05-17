package me.antileaf.alice.cards.interfaces;

public interface ConditionalExhaustCard {
	boolean shouldExhaust();

	default boolean ignoreSpoon() {
		return false;
	}
}
