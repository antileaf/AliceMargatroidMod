package rs.antileaf.alice.doll.dolls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.utils.AliceSpireKit;

public class EmptyDollSlot extends AbstractDoll {
	public static final String SIMPLE_NAME = EmptyDollSlot.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(EmptyDollSlot.SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public EmptyDollSlot() {
		super(
				ID,
				dollStrings.NAME,
				-1,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.NONE
		);
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
	public boolean takeDamage(int amount) {
		if (amount > 0)
			AliceSpireKit.log(EmptyDollSlot.class, "EmptyDollSlot.takeDamage() called!");
		
		return false;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void onAct() {
		AliceSpireKit.log(EmptyDollSlot.class, "EmptyDollSlot.onAct() called!");
	}
	
	@Override
	public void applyPower() {}
	
	@Override
	public void updateDescription() {
		this.description = dollStrings.DESCRIPTION[0];
	}
	
	@Override
	public void updateDescriptionImpl() {}
	
	@Override
	public void renderHealth(SpriteBatch sb) {
		// Do nothing.
	}
	
	@Override
	public void triggerActAnimation() {
		AliceSpireKit.log(EmptyDollSlot.class, "EmptyDollSlot.triggerActAnimation() called!");
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new EmptyDollSlot();
	}
	
	@Override
	public void playChannelSFX() {}
	
//	public static String getFlavor() {
//		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
//	}
}
