package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.Settings;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.MoveDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceSpireKit;

public class FranceDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = FranceDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 5;
	public static final int ACT_AMOUNT = 5;
	
	public FranceDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
		);
		
//		this.passiveAmountType = DollAmountType.BLOCK;
		this.actAmountType = DollAmountType.BLOCK;
	}
	
	@Override
	public AliceDollStrings getDollStrings() {
		return dollStrings;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		return MAX_HP;
	}
	
	@Override
	public void onAct() {
		int dest = -1;
		if (this.getOverflowedDamage() <= 0) {
			for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
				AbstractDoll doll = DollManager.get().getDolls().get(i);
				if (doll != this && doll.getOverflowedDamage() > 0) {
					dest = i;
					break;
				}
			}
		}
		
		if (dest != -1) {
			if (DollManager.get().getDolls().get(dest) != this)
				AliceSpireKit.addActionToBuffer(new MoveDollAction(this, dest));
			else
				AliceSpireKit.logger.info("FranceDoll.onAct: dest == this");
		}
		else {
			AliceSpireKit.addActionToBuffer(new DollGainBlockAction(this, this.actAmount));
			this.highlightActValue();
		}
	}
	
	@Override
	public void onStartOfTurn() {
		this.highlightPassiveValue();
	}
	
	public void triggerPassiveEffect(int amount) {
		this.addToBot(new DollGainBlockAction(this, amount));
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new FranceDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 14.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 30.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new FranceDoll()).desc();
	}
}
