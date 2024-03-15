package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
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
	public void onAct() {
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new LoseStrengthPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new LoseDexterityPower(AbstractDungeon.player, this.actAmount),
				this.actAmount
		));
	}
	
	@Override
	public void postSpawn() {
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new StrengthPower(AbstractDungeon.player, this.passiveAmount),
				this.passiveAmount
		));
		
		this.addToBot(new ApplyPowerAction(
				AbstractDungeon.player,
				AbstractDungeon.player,
				new DexterityPower(AbstractDungeon.player, this.passiveAmount),
				this.passiveAmount
		));
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
					new StrengthPower(AbstractDungeon.player, 1),
					1
			));
			
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, 1),
					1
			));
		}
	}
	
	@Override
	public void postOtherDollRemoved(AbstractDoll doll) {
		if (doll instanceof HouraiDoll) {
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new StrengthPower(AbstractDungeon.player, -1),
					-1
			));
			
			this.addToBot(new ApplyPowerAction(
					AbstractDungeon.player,
					AbstractDungeon.player,
					new DexterityPower(AbstractDungeon.player, -1),
					-1
			));
		}
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
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new NetherlandsDoll();
	}
	
	@Override
	public void playChannelSFX() {}
}
