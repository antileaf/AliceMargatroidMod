package me.antileaf.alice.doll.dolls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.strings.AliceDollStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmptyDollSlot extends AbstractDoll {
	private static final Logger logger = LogManager.getLogger(EmptyDollSlot.class.getName());

	public static final String SIMPLE_NAME = EmptyDollSlot.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public EmptyDollSlot() {
		super(
				ID,
				dollStrings.NAME,
				-1,
				-1,
				-1,
				null,
				RenderTextMode.NONE
		);
		
		this.angle = MathUtils.random(360.0F);
	}
	
	@Override
	public AliceDollStrings getDollStrings() {
		return dollStrings;
	}
	
	@Override
	public void updateAnimation() {
		super.updateAnimation();
		
		this.angle += Gdx.graphics.getDeltaTime() * 10.0F;
	}
	
	@Override
	public void renderImage(SpriteBatch sb) {
		sb.draw(
				ImageMaster.ORB_SLOT_2,
				this.cX - 48.0F - this.bobEffect.y / 8.0F,
				this.cY - 48.0F + this.bobEffect.y / 8.0F,
				48.0F,
				48.0F,
				96.0F,
				96.0F,
				this.scale,
				this.scale,
				0.0F,
				0,
				0,
				96,
				96,
				false,
				false
		);
		sb.draw(
				ImageMaster.ORB_SLOT_1,
				this.cX - 48.0F - this.bobEffect.y / 8.0F,
				this.cY - 48.0F + this.bobEffect.y / 8.0F,
				48.0F,
				48.0F,
				96.0F,
				96.0F,
				this.scale,
				this.scale,
				this.angle,
				0,
				0,
				96,
				96,
				false,
				false
		);
//		super.renderImage(sb);
	}
	
	@Override
	public int onPlayerDamaged(int amount) {
		return amount;
	}
	
	@Override
	public void addBlock(int blockAmount) {}
	
	@Override
	public void loseBlock(int amount, boolean shouldPlaySound) {}
	
	@Override
	public void loseBlock(int amount) {}
	
	@Override
	public void heal(int amount) {}
	
	@Override
	public boolean takeDamage(int amount) {
		if (amount > 0)
			logger.warn("EmptyDollSlot.takeDamage() called!");
		
		return false;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		logger.warn("EmptyDollSlot.getBaseHP() called.");
		return 0;
	}
	
	@Override
	public void onAct(DollActModifier modifier) {
		logger.warn("EmptyDollSlot.onAct() called!");
	}
	
	@Override
	public void applyPower() {}
	
	@Override
	public void updateDescription() {
		this.description = dollStrings.PASSIVE_DESCRIPTION;
	}
	
	@Override
	public void updateDescriptionImpl() {}
	
	@Override
	public void renderHealth(SpriteBatch sb) {
		// Do nothing.
	}
	
	@Override
	public void triggerActAnimation() {
		logger.warn("EmptyDollSlot.triggerActAnimation() called!");
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new EmptyDollSlot();
	}
	
	@Override
	public void playChannelSFX() {}
}
