package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.utils.AliceSpireKit;

public class HouraiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = HouraiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 2;
	
	public HouraiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath("dark"),
				RenderTextMode.PASSIVE
		);
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
		this.passiveAmount = this.basePassiveAmount =
				DollManager.getInstance(AbstractDungeon.player).getHouraiDollCount();
		
		this.updateDescription();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.DESCRIPTION[0];
		this.actDescription = dollStrings.DESCRIPTION[1];
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
}
