package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.utils.AliceSpireKit;

public class EmptyDollSlot extends AbstractDoll {
	public static final String SIMPLE_NAME = EmptyDollSlot.class.getSimpleName();
	public static final String ID = AliceSpireKit.generateID(EmptyDollSlot.SIMPLE_NAME);
	public static final String NAME = ""; // TODO
	
	public EmptyDollSlot() {
		super(EmptyDollSlot.ID, EmptyDollSlot.NAME, -1, -1, -1,
				"", "", "");
	}
	
	@Override
	public void onAct() {
		assert false: "EmptyDollSlot.onAct() called!";
	}
	
	@Override
	public void triggerActAnimation() {
		assert false: "EmptyDollSlot.triggerActAnimation() called!";
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new EmptyDollSlot();
	}
	
	@Override
	public void playChannelSFX() {}
}
