package rs.antileaf.alice.effects.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.function.Function;

// colorFunction takes [0, 1] and returns a color
public class AliceDrawLineEffect extends AbstractGameEffect {
	Function<Float, Color> colorFunction;
	Function<Float, Float> transparencyFunction = null;
	float duration;
	float thickness;
	float startX, startY, destX, destY;
	
	public AliceDrawLineEffect(Function<Float, Color> colorFunction, float duration, float thickness,
	                           float startX, float startY, float destX, float destY,
	                           boolean renderBehind) {
		this.colorFunction = colorFunction;
		this.duration = this.startingDuration = duration;
		this.thickness = thickness;
		this.startX = startX;
		this.startY = startY;
		this.destX = destX;
		this.destY = destY;
		this.renderBehind = renderBehind;
		
		this.rotation = MathUtils.atan2(destY - startY, destX - startX);
		
		float distance = (float)Math.sqrt((destX - startX) * (destX - startX) + (destY - startY) * (destY - startY));
		this.scale = distance / 256.0F;
	}
	
	public AliceDrawLineEffect(Color color, Function<Float, Float> transparencyFunction, float duration, float thickness,
	                           float startX, float startY, float destX, float destY,
	                           boolean renderBehind) {
		this((t) -> color, duration, thickness, startX, startY, destX, destY, renderBehind);
		this.transparencyFunction = transparencyFunction;
	}
	
	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();
		
		float t = 1.0F - this.duration / this.startingDuration;
		this.color = this.colorFunction.apply(t);
		if (this.transparencyFunction != null)
			this.color.a = this.transparencyFunction.apply(t);
		
		if (this.duration < 0.0F)
			this.isDone = true;
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(this.color.cpy());
		sb.setBlendFunction(770, 1);
		
		sb.draw(
				ImageMaster.HORIZONTAL_LINE,
				startX, startY,
				0.0F, 0.0F,
				256.0F, 256.0F,
				this.scale, this.thickness * this.scale,
				this.rotation * MathUtils.radiansToDegrees,
				0, 0, 256, 256,
				false, false
		);
		
		sb.setBlendFunction(770, 771);
	}
	
	@Override
	public void dispose() {
	
	}
}
