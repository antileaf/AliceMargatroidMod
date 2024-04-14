package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class FranceDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = FranceDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 5;
	public static final int ACT_AMOUNT = 3;
	
	public FranceDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("blue"),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.BLOCK;
	}
	
	@Override
	public void clearBlock(int preserve) {}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void onAct() {
		AliceSpireKit.addToTop(new DollGainBlockAction(this, this.actAmount));
		
		this.highlightActValue();
	}
	
//	@Override
//	public void postOtherDollDestroyed(AbstractDoll doll) {
//		if (doll instanceof EmptyDollSlot)
//			AliceSpireKit.log("FranceDoll", "EmptyDollSlot destroyed");
//		else
//			AliceSpireKit.addToBot(new DollActAction(this));
//	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.DESCRIPTION[0];
		
		this.actDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[1],
				this.coloredActAmount(),
				dollStrings.DESCRIPTION[2]
		);
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
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new FranceDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
