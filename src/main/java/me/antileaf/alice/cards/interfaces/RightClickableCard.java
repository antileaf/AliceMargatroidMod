package me.antileaf.alice.cards.interfaces;

public interface RightClickableCard {
	void onRightClick();
	default void onDoubleRightClick() {}

	default Position clickablePositions() {
		return Position.HAND;
	}

	enum Position {
		HAND, ALL_PILES
	}
}
