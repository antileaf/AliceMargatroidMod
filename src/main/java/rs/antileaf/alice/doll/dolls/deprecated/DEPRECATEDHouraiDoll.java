package rs.antileaf.alice.doll.dolls.deprecated;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

@Deprecated
public class DEPRECATEDHouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = DEPRECATEDHouraiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 2;
	
	public DEPRECATEDHouraiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath("white"),
				RenderTextMode.PASSIVE
		);
	}
	
	@Override
	public AbstractOrb makeCopy() {
		return new DEPRECATEDHouraiDoll();
	}
	
	@Override
	public void playChannelSFX() {
		// TODO
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
		AliceSpireKit.log(this.getClass(), "HouraiDoll.onAct() should not be called!");
	}
	
	@Override
	public void applyPower() {
		this.passiveAmount = this.basePassiveAmount =
				DollManager.getInstance(AbstractDungeon.player).getTotalHouraiPassiveAmount();
		
		this.updateDescription();
	}
	
	@Override
	public void updateDescriptionImpl() {
		if (this.dontShowHPDescription)
			this.passiveDescription = dollStrings.DESCRIPTION[0];
		else {
			this.passiveDescription = AliceMiscKit.join(
					dollStrings.DESCRIPTION[1],
					this.coloredPassiveAmount(),
					dollStrings.DESCRIPTION[2]
			);
		}
		
		this.actDescription = dollStrings.DESCRIPTION[3];
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new DEPRECATEDHouraiDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
