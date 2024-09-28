package rs.antileaf.alice.cards.interfaces;

public interface RightClickableCard {
	void onRightClick();
	default void onDoubleRightClick() {}
}
