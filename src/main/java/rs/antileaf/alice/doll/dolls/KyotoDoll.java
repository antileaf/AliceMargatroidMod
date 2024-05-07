package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class KyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = KyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 5;
	public static final int PASSIVE_AMOUNT = 5;
	public static final int ACT_AMOUNT = 1;
	
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
	public void onAct() {
		this.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.block));
	}
	
	@Override
	public void onStartOfTurn() {
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				AliceSpireKit.addActionToBuffer(new DollGainBlockAction(doll, this.actAmount));
		
		AliceSpireKit.commitBuffer();
		
		this.highlightActValue();
	}
	
	@Override
	public void updateDescriptionImpl() {
		if (this.dontShowHPDescription)
			this.passiveDescription = String.format(dollStrings.DESCRIPTION[0], this.coloredPassiveAmount());
		else
			this.passiveDescription = String.format(dollStrings.DESCRIPTION[1],
					this.coloredPassiveAmount(), DollManager.get().getPreservedBlock());
		
		this.passiveDescription += " NL " + String.format(dollStrings.DESCRIPTION[2], this.coloredActAmount());
		
		this.actDescription = dollStrings.DESCRIPTION[3];
	}
	
	@Override
	public void clearBlock(int preserve) {
		super.clearBlock(preserve);
		this.highlightPassiveValue();
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
