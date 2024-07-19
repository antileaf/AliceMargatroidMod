package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.MoveDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class KyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = KyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 5;
	public static final int PASSIVE_AMOUNT = 4;
	public static final int ACT_AMOUNT = 6;
	
	public KyotoDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("green"),
				RenderTextMode.BOTH
		);
		
		this.passiveAmountType = DollAmountType.BLOCK;
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
		AliceSpireKit.addToTop(new DollGainBlockAction(this, this.passiveAmount));
		this.highlightPassiveValue();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0],
					this.coloredPassiveAmount());
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[1],
					this.coloredActAmount());
	}
	
	@Override
	public void clearBlock(int preserved) {
		// Will not lose block.
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new KyotoDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new KyotoDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
