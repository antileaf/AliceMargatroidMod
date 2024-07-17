package rs.antileaf.alice.doll.dolls.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

@Deprecated
public class DEPRECATEDKyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = DEPRECATEDKyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 5;
	public static final int PASSIVE_AMOUNT = 5;
	public static final int ACT_AMOUNT = 1;
	
	public DEPRECATEDKyotoDoll() {
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
		ArrayList<AbstractGameAction> actions = new ArrayList<>();
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				actions.add(new DollGainBlockAction(doll, this.actAmount));
		actions.add(new GainBlockAction(AbstractDungeon.player, this.actAmount));
		
		AliceSpireKit.addActionsToTop(actions.toArray(new AbstractGameAction[0]));
		
		this.highlightActValue();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[0],
				this.coloredPassiveAmount(),
				dollStrings.DESCRIPTION[1]
		);
		
		this.actDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[2],
				this.coloredActAmount(),
				dollStrings.DESCRIPTION[3]
		);
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
		return new DEPRECATEDKyotoDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new DEPRECATEDKyotoDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
