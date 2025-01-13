package rs.antileaf.alice.effects.unique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class ResurgenceEffect extends AbstractGameEffect {
	public static Map<Integer, ResurgenceEffect> instances = new HashMap<>();
	
	public static final float FADE_DUR = 0.2F;
	public static final float DUR = 1.1F;
	public static final float velocity = 50.0F;
	
	public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			ResurgenceEffect.class.getSimpleName());
	
	Texture img;
	float x, y;
	
	public ResurgenceEffect() {
		this.duration = this.startingDuration = DUR + FADE_DUR * 2.0F;
		
		this.scale = 1.0F;
		this.color = Color.WHITE.cpy();
		this.color.a = 0.0F;
		
		this.img = ImageMaster.loadImage(AliceHelper.getImgFilePath("vfx", "resurgence"));
		this.x = Settings.WIDTH - this.img.getWidth() / 2.0F;
		this.y = Settings.HEIGHT * 0.45F;
	}
	
	@Override
	public void update() {
		float dt = Gdx.graphics.getDeltaTime();
		this.duration -= dt;
		
		if (this.duration > DUR + FADE_DUR)
			this.color.a = (1.0F - (this.duration - DUR - FADE_DUR) / FADE_DUR) * 0.6F;
		else if (this.duration > FADE_DUR)
			this.x -= velocity * dt;
		else if (this.duration > 0.0F)
			this.color.a = this.duration / FADE_DUR * 0.6F;
		else
			this.isDone = true;
	}
	
	@Override
	public void render(SpriteBatch sb) {
		sb.setBlendFunction(770, 771);
		sb.setColor(this.color);
		
		sb.draw(
				this.img,
				this.x - this.img.getWidth() / 2.0F,
				this.y - this.img.getHeight() / 2.0F,
				this.img.getWidth() / 2.0F,
				this.img.getHeight() / 2.0F,
				this.img.getWidth(),
				this.img.getHeight(),
				this.scale,
				this.scale,
				0.0F,
				0,
				0,
				this.img.getWidth(),
				this.img.getHeight(),
				false,
				false
		);
		
		FontHelper.renderFontCentered(
				sb,
				FontHelper.largeCardFont,
				uiStrings.TEXT[0],
				this.x,
				this.y,
				this.color,
				1.0F
		);
	}
	
	@Override
	public void dispose() {
		this.img.dispose();
	}
}
