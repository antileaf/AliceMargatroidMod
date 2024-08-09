package rs.antileaf.alice.effects.unique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import rs.antileaf.alice.effects.common.AlicePokerCardsDroppingEffect;
import rs.antileaf.alice.effects.utils.AlicePokerHelper;

public class PokerTrickEffect extends AbstractGameEffect {
	public static final int CARD_WIDTH = AlicePokerHelper.CARD_WIDTH;
	public static final int CARD_HEIGHT = AlicePokerHelper.CARD_HEIGHT;
	public static final float velocity = 1000.0F;
	
	AlicePokerHelper.Suit suit;
	float startX, startY, destX, destY;
	float x, y;
	
	public PokerTrickEffect(AlicePokerHelper.Suit suit, float startX, float startY, float destX, float destY, float scale) {
		this.suit = suit;
		this.startX = startX;
		this.startY = startY;
		this.destX = destX;
		this.destY = destY;
		this.scale = scale;
		
		this.x = startX;
		this.y = startY;
		
		float distance = (float)Math.sqrt((destX - startX) * (destX - startX) + (destY - startY) * (destY - startY));
		this.duration = this.startingDuration = distance / velocity;
		
		if (distance > 0.0F)
			this.rotation = MathUtils.atan2(destY - startY, destX - startX) * MathUtils.radiansToDegrees;
	}
	
	@Override
	public void update() {
		float dt = Gdx.graphics.getDeltaTime();
		this.duration -= dt;
		
		if (this.duration < 0.0F) {
			AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(destX, destY,
					AbstractGameAction.AttackEffect.BLUNT_LIGHT));
			
			AlicePokerCardsDroppingEffect effect = new AlicePokerCardsDroppingEffect(this.scale,
					2F, false);
			
			for (int i = 0; i < 8; i++)
				effect.add(suit,
						destX, destY,
						MathUtils.random(-300.0F, 300.0F),
						MathUtils.random(-50.0F, 300.0F),
						MathUtils.random(0.0F, 360.0F),
						MathUtils.random(-360.0F, 360.0F));
			
			AbstractDungeon.effectsQueue.add(effect);
			
			this.isDone = true;
			return;
		}
		
		float t = 1.0F - this.duration / this.startingDuration;
		this.x = MathUtils.lerp(startX, destX, t);
		this.y = MathUtils.lerp(startY, destY, t);
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.setBlendFunction(770, 771);
		sb.draw(AlicePokerHelper.getTexture(suit),
				x - CARD_WIDTH / 2.0F,
				y - CARD_HEIGHT / 2.0F,
				CARD_WIDTH / 2.0F,
				CARD_HEIGHT / 2.0F,
				CARD_WIDTH, CARD_HEIGHT,
				this.scale, this.scale,
				this.rotation,
				0, 0, CARD_WIDTH, CARD_HEIGHT,
				false, false);
	}
	
	@Override
	public void dispose() {
	
	}
}
