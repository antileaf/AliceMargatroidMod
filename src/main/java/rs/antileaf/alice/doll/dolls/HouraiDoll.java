package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class HouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = HouraiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 2;
	public static final int PASSIVE_AMOUNT = 1;
	public static final int ACT_AMOUNT = 1;
	
	private int additionalPassiveAmount = 0;
	
	public HouraiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("white"),
				RenderTextMode.PASSIVE
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.MAGIC;
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
	public void applyPower() {
		super.applyPower();
		
		this.passiveAmount += this.additionalPassiveAmount;
		this.updateDescription();
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void onAct() {
		this.additionalPassiveAmount += this.actAmount;
		this.applyPower();
	}
	
	public void resetPassiveAmount() {
		this.additionalPassiveAmount = 0;
		this.applyPower();
	}
	
//	@Override
//	public void applyPower() {
//		this.passiveAmount = this.basePassiveAmount =
//				DollManager.getInstance(AbstractDungeon.player).getTotalHouraiPassiveAmount();
//
//		this.updateDescription();
//	}
	
	@Override
	public void updateDescriptionImpl() {
		if (this.dontShowHPDescription)
			this.passiveDescription = String.format(dollStrings.DESCRIPTION[0], this.coloredPassiveAmount());
		else {
//			this.passiveDescription = AliceMiscKit.join(
//					dollStrings.DESCRIPTION[1],
//					this.coloredPassiveAmount(),
//					dollStrings.DESCRIPTION[2],
//					"" + DollManager.get().getTotalHouraiPassiveAmount(),
//					dollStrings.DESCRIPTION[3]
//			);
			this.passiveDescription = String.format(dollStrings.DESCRIPTION[1],
					this.coloredPassiveAmount(), DollManager.get().getTotalHouraiPassiveAmount());
		}
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[2], this.coloredActAmount());
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new HouraiDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
