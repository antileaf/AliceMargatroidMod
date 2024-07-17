package rs.antileaf.alice.doll.dolls.deprecated;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

@Deprecated
public class LilyDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = LilyDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 1;
	public static final int PASSIVE_AMOUNT = 3;
	public static final int ACT_AMOUNT = 3;
	
	public LilyDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("LilyWhite"),
				RenderTextMode.PASSIVE
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.MAGIC;
	}
	
	@Override
	public void applyPower() {}
	
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
		this.addToBot(new ArmamentsAction(false)); // I am lazy, so I reused the Armaments action.
	}
	
	@Override
	public void postSpawn() {
		this.addToTop(new TalkAction(true, dollStrings.DESCRIPTION[4], 1.0F, 2.0F));
	}
	
	@Override
	public void postEnergyRecharge() {
		this.addToBot(new GainEnergyAction(1));
//		this.basePassiveAmount -= 1;
		this.passiveAmount -= 1;
		
		if (this.passiveAmount <= 0)
			this.addToBot(new RecycleDollAction(this));
		else
			this.updateDescription();
	}
	
	@Override
	public void onRecycle() {
		this.addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 3));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[0],
				this.coloredPassiveAmount(),
				dollStrings.DESCRIPTION[1],
				this.coloredActAmount(),
				dollStrings.DESCRIPTION[2]
		);
		
		this.actDescription = dollStrings.DESCRIPTION[3];
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new LilyDoll();
	}
	
	@Override
	public void playChannelSFX() {}
}
