package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class NetherlandsDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = NetherlandsDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 4;
	public static final int PASSIVE_AMOUNT = 1;
	public static final int ACT_AMOUNT = 1;
	
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
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new LoseStrengthPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new LoseDexterityPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		
		AliceSpireKit.commitBuffer();
		this.highlightActValue();
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
					new DexterityPower(AbstractDungeon.player, this.passiveAmount),
					this.passiveAmount
			)
		);
		
		this.highlightPassiveValue();
	}
	
	@Override
	public void onRemoved() {
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, -this.passiveAmount),
				-this.passiveAmount
		));
		
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, -this.passiveAmount),
				-this.passiveAmount
		));
	}
	
	@Override
	public void postOtherDollSpawn(AbstractDoll doll) {
		if (doll instanceof HouraiDoll) {
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, doll.passiveAmount),
					doll.passiveAmount
			));
			
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, doll.passiveAmount),
					doll.passiveAmount
			));
		}
	}
	
	@Override
	public void postOtherDollAct(AbstractDoll doll) {
		if (doll instanceof HouraiDoll) {
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, doll.actAmount),
					doll.actAmount
			));
			
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, doll.actAmount),
					doll.actAmount
			));
		}
	}
	
	@Override
	public void postOtherDollRemoved(AbstractDoll doll) {
		if (doll instanceof HouraiDoll) {
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, -doll.passiveAmount),
					-doll.passiveAmount
			));
			
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, -doll.passiveAmount),
					-doll.passiveAmount
			));
		}
	}
	
	public void onStartOfTurnUpdate(int diff) {
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, diff),
				diff
		));
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, diff),
				diff
		));
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0], this.coloredPassiveAmount());
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[1], this.coloredActAmount());
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
