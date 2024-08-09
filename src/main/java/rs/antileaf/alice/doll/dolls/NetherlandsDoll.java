package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class NetherlandsDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = NetherlandsDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 3;
	public static final int PASSIVE_AMOUNT = 1;
	public static final int ACT_AMOUNT = 1;
	
	public boolean dex = false;
	
	public NetherlandsDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("yellow"),
				RenderTextMode.BOTH
		);
		
		this.passiveAmountType = DollAmountType.OTHERS;
		this.actAmountType = DollAmountType.OTHERS;
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
		if (!this.dex) {
			AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, 1),
					1
			));
			
			this.basePassiveAmount += 1;
			this.passiveAmount += 1;
			this.highlightPassiveValue();
		}
		else {
			AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, 1),
					1
			));
			
			this.baseActAmount += 1;
			this.actAmount += 1;
			this.highlightActValue();
		}
		
		AliceSpireKit.commitBuffer();
		this.dex = !this.dex;
	}
	
	@Override
	public void postSpawn() {
		this.addActionsToTop(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, this.passiveAmount),
					this.passiveAmount
			),
				new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, this.actAmount),
					this.actAmount
			)
		);
		
//		this.highlightPassiveValue();
	}
	
	private void onDisappeared() {
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, -this.passiveAmount),
				-this.passiveAmount
		));
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, -this.actAmount),
				-this.actAmount
		));
		
		AliceSpireKit.commitBuffer();
	}
	
	@Override
	public void onRecycle() {
		this.onDisappeared();
	}
	
	@Override
	public void onDestroyed() {
		this.onDisappeared();
	}
	
	@Override
	public void onRemoved() {
		this.onDisappeared();
	}
	
	@Override
	protected String getRenderPassiveValue() {
		return (!this.dex ? "+" : "") + this.passiveAmount;
	}
	
	@Override
	protected String getRenderActValue() {
		return (this.dex ? "+" : "") + this.actAmount;
	}
	
	@Override
	protected String coloredPassiveAmount() {
		return (this.passiveAmount > 1 ? "#g" : "") + this.passiveAmount;
	}
	
	@Override
	protected String coloredActAmount() {
		return (this.actAmount > 1 ? "#g" : "") + this.actAmount;
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0],
				this.coloredPassiveAmount(), this.coloredActAmount());
		
		this.actDescription = dollStrings.DESCRIPTION[1];
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new NetherlandsDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new NetherlandsDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
