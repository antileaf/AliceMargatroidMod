package rs.antileaf.alice.effects.unique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.utils.AliceHelper;
import rs.antileaf.alice.utils.AliceImageMaster;

import java.util.HashMap;
import java.util.Map;

// colorFunction takes [0, 1] and returns a color
public class SeaOfSubconsciousnessEffect extends AbstractGameEffect {
	public static Map<Integer, SeaOfSubconsciousnessEffect> instances = new HashMap<>();
	
	public static final float FADE_DUR = 0.25F;
	public static final float MAX_A = 1F;
	
	int slot;
	public boolean hovered;
	float duration;
//	float transparency;
	
	public SeaOfSubconsciousnessEffect(int slot) {
		this.duration = this.startingDuration = FADE_DUR;
		this.hovered = false;
		this.slot = slot;
		
//		this.rotation = 40 * (slot - 3); // 0 on the right
		
		this.scale = 0.5F / 1.28F * Settings.scale;
		this.color = Color.WHITE.cpy();
		this.color.a = 0.0F;
	}
	
	@Override
	public void update() {
		if (this.hovered)
			this.duration = FADE_DUR;
		
		float d = Gdx.graphics.getDeltaTime();
		
		if (this.hovered)
			this.color.a = Math.min(MAX_A, this.color.a + d / FADE_DUR);
		else
			this.color.a = Math.max(0.0F, this.color.a - d / FADE_DUR);
		
		this.duration -= d;
		
		if (this.duration < 0.0F) {
			this.isDone = true;
			AliceHelper.log("SeaOfSubconsciousnessEffect: done, slot = " + this.slot);
			instances.remove(this.slot);
		}
	}
	
	@Override
	public void render(SpriteBatch sb) {
		if (!DollManager.get().isShown())
			return;
		
		sb.setColor(this.color.cpy());
		sb.setBlendFunction(770, 771);
		
		AbstractDoll doll = DollManager.get().getDolls().get(this.slot);
		
		float startX = doll.getDrawCX() - 128.0F;
		float startY = doll.getDrawCY() + 20.0F + (doll.getImgSize() > 96.0F ? 10.0F : 0.0F);
		
		sb.draw(
				AliceImageMaster.ALICE_ARROW,
				startX, startY,
				128.0F, 128.0F,
				256.0F, 256.0F,
				this.scale, this.scale / 1.2F,
				0.0F,
				0, 0, 256, 256,
				false, false
		);
		
//		sb.setBlendFunction(770, 771);
	}
	
	@Override
	public void dispose() {
	}
}
