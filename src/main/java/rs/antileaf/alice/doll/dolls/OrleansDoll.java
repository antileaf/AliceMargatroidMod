package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.stream.Stream;

public class OrleansDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = OrleansDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 1;
	
	public OrleansDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				2,
				1,
				AliceSpireKit.getOrbImgFilePath("black"),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.MAGIC;
	}
	
	@Override
	public String getID() {
		return ID;
	}
	
	@Override
	public void onAct() {
		this.addToBot(new DrawCardAction(this.actAmount));
	}
	
	@Override
	public void onRecycle() {
		this.addToTop(new GainEnergyAction(this.passiveAmount));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0],
				AliceMiscKit.join(Stream.generate(() -> "[E]")
						.limit(this.passiveAmount).toArray(String[]::new)));
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[1], this.coloredActAmount());
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new OrleansDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new OrleansDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
