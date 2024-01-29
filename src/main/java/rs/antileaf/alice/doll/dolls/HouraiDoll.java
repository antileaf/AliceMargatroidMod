package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.utils.AliceSpireKit;

public class HouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = HouraiDoll.class.getSimpleName();
	public static final String ID = AliceSpireKit.generateID(HouraiDoll.SIMPLE_NAME);
	public static final String NAME = ""; // TODO
	
	public static final int MAX_HP = 2;
	
	public HouraiDoll() {
		super(HouraiDoll.ID, HouraiDoll.NAME, HouraiDoll.MAX_HP, -1, -1,
				"", "", "");
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new HouraiDoll();
	}
	
	@Override
	public void playChannelSFX() {
		// TODO
	}
	
	@Override
	public void onAct() {
		AliceSpireKit.log(this.getClass(), "HouraiDoll.onAct() should not be called!");
	}
	
	@Override
	public void applyPower() {
		this.actAmount = this.baseActAmount =
				DollManager.getInstance(AbstractDungeon.player).getHouraiDollCount();
		
		this.updateDescription();
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
}
