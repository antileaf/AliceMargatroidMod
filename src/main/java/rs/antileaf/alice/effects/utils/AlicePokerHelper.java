package rs.antileaf.alice.effects.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import rs.antileaf.alice.utils.AliceImageMaster;

public class AlicePokerHelper {
	public static final int CARD_WIDTH = 100;
	public static final int CARD_HEIGHT = 140;
	
	public enum Suit {
		SPADE, HEART, DIAMOND, CLUB,
		JOKER_SMALL, JOKER_BIG
	}
	
	public static Texture getTexture(Suit suit) {
		return AliceImageMaster.POKERS[suit.ordinal()];
	}
	
	public static Suit getRandomSuit(boolean includeJokers) {
		if (includeJokers && MathUtils.random(0, 50) == 0)
			return MathUtils.randomBoolean() ? Suit.JOKER_BIG : Suit.JOKER_SMALL;
		else
			return Suit.values()[MathUtils.random(0, 3)];
	}
	
	public static Suit getRandomSuit() {
		return getRandomSuit(false);
	}
}
