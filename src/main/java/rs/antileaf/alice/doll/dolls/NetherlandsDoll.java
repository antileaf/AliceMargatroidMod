package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceHelper;

public class NetherlandsDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = NetherlandsDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 3;
	
	public boolean shouldAddDex = false;
	
	public NetherlandsDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				0,
				0,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.BOTH
		);
		
		this.passiveAmountType = DollAmountType.OTHERS;
		this.actAmountType = DollAmountType.OTHERS;
	}
	
	@Override
	public AliceDollStrings getDollStrings() {
		return dollStrings;
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
		if (!this.shouldAddDex) {
			AliceHelper.addActionToBuffer(new ApplyPowerAction(
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
			AliceHelper.addActionToBuffer(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, 1),
					1
			));
			
			this.baseActAmount += 1;
			this.actAmount += 1;
			this.highlightActValue();
		}
		
//		AliceSpireKit.commitBuffer();
		this.shouldAddDex = !this.shouldAddDex;
	}
	
	@Override
	public void postSpawn() {
//		AliceHelper.addActionToBuffer(new DollActAction(this)); // 额外一次
		
//		this.highlightPassiveValue();
	}
	
	private void onDisappeared() {
		AliceHelper.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, -this.passiveAmount),
				-this.passiveAmount
		));
		AliceHelper.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, -this.actAmount),
				-this.actAmount
		));
		
		AliceHelper.commitBuffer();
	}
	
//	@Override
//	public boolean preOtherDollSpawn(AbstractDoll doll) {
//		if (doll instanceof NetherlandsDoll) {
//			for (int i = 0; i < 2; i++)
//				this.addToTop(new DollActAction(this));
//			return true;
//		}
//		else
//			return false;
//	}
	
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
		return (!this.shouldAddDex ? "+" : "") + this.passiveAmount;
	}
	
	@Override
	protected String getRenderActValue() {
		return (this.shouldAddDex ? "+" : "") + this.actAmount;
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
		this.passiveDescription = dollStrings.PASSIVE_DESCRIPTION;
		this.actDescription = dollStrings.ACT_DESCRIPTION;
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

	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 14.0F * Settings.scale;
	}

	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 28.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new NetherlandsDoll()).desc();
	}
}
