package me.antileaf.alice.effects.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import me.antileaf.alice.effects.utils.AlicePokerHelper;

import java.util.ArrayList;

@Deprecated
public class AlicePokerCardsDroppingEffect extends AbstractGameEffect {
	public static final int CARD_WIDTH = AlicePokerHelper.CARD_WIDTH;
	public static final int CARD_HEIGHT = AlicePokerHelper.CARD_HEIGHT;
	public static final float DEFAULT_GRAVITY = 500.0F;
	
	private static class Card {
		AlicePokerHelper.Suit suit;
		float x, y, angle;
		float vx, vy, angularV;
	}
	
	private final ArrayList<Card> cards = new ArrayList<>();
	private final float gravity;
//	private boolean screenEffectTriggered = false;
	
//	private float soundTimer = 0.0F;
	
	public AlicePokerCardsDroppingEffect(float scale, float duration, boolean renderBehind, float gravity) {
		this.scale = scale;
		this.gravity = gravity;
		this.duration = this.startingDuration = duration;
		this.renderBehind = renderBehind;
		
		this.color = Color.WHITE.cpy();
	}
	
	public AlicePokerCardsDroppingEffect(float scale, float duration, boolean renderBehind) {
		this(scale, duration, renderBehind, DEFAULT_GRAVITY);
	}
	
	public void add(AlicePokerHelper.Suit suit, float x, float y, float vx, float vy, float angle, float angularV) {
		Card card = new Card();
		card.suit = suit;
		card.x = x;
		card.y = y;
		card.vx = vx;
		card.vy = vy;
		card.angle = angle;
		card.angularV = angularV;
		
		cards.add(card);
	}
	
	@Override
	public void update() {
		float dt = Gdx.graphics.getDeltaTime();
		
		for (Card card : cards) {
			card.x += card.vx * dt;
			card.y += card.vy * dt;
			card.vy -= gravity * dt;
			card.angle += card.angularV * dt;
			
//			AbstractDungeon.effectList.add(new StunStarEffect(card.x, card.y));
		}
		
//		this.soundTimer -= dt;
//		if (this.soundTimer < 0.0F) {
//			if (this.duration > this.startingDuration - 0.35F)
//				CardCrawlGame.sound.play("STAB_BOOK_DEATH");
//			this.soundTimer = 0.1F;
//		}
		
		this.duration -= dt;
		if (this.duration < 0.0F)
			this.isDone = true;
	}
	
	@Override
	public void render(SpriteBatch sb) {
		this.color.a = this.duration / this.startingDuration;
		sb.setBlendFunction(770, 771);
		sb.setColor(this.color);
		
		for (Card o : cards)
			sb.draw(AlicePokerHelper.getTexture(o.suit),
					o.x - CARD_WIDTH / 2F, o.y - CARD_HEIGHT / 2F,
					CARD_WIDTH / 2F, CARD_HEIGHT / 2F,
					CARD_WIDTH, CARD_HEIGHT,
					this.scale, this.scale, o.angle,
					0, 0, CARD_WIDTH, CARD_HEIGHT,
					false, false);
	}
	
	@Override
	public void dispose() {
	
	}
}
