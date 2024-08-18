package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.MoveDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class FranceDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = FranceDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 5;
//	public static final int PASSIVE_AMOUNT = 4;
	public static final int ACT_AMOUNT = 6;
	
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
	public String getID() {
		return ID;
	}
	
	@Override
	public int getBaseHP() {
		return MAX_HP;
	}
	
	@Override
	public void onAct() {
		int pos = -1, val = 0;
		for (int i = 0; i < DollManager.get().getDolls().size(); i++) {
			AbstractDoll doll = DollManager.get().getDolls().get(i);
			
			int tmp = doll.calcTotalDamageAboutToTake();
			if (tmp > 0) {
				if (pos == -1 || tmp > val) {
					pos = i;
					val = tmp;
				}
			}
		}
		
		if (pos != -1 && DollManager.get().getDolls().get(pos) != this) {
			this.addToTop(new MoveDollAction(this, pos));
		}
		else {
			this.addToTop(new DollGainBlockAction(this, this.actAmount));
			this.highlightActValue();
		}
	}
	
	@Override
	public void onStartOfTurn() {
//		AliceSpireKit.addToTop(new DollGainBlockAction(this, this.passiveAmount));
		this.highlightPassiveValue();
	}
	
	public void passiveEffect(int amount) {
		this.addToBot(new DollGainBlockAction(this, amount));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0],
					this.coloredPassiveAmount());
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[1],
					this.coloredActAmount());
	}
	
//	@Override
//	public void clearBlock(int preserved) {
//
//	}
	
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
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 24.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new FranceDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
