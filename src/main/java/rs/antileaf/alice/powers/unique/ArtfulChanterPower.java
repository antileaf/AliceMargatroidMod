package rs.antileaf.alice.powers.unique;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.powers.AbstractAlicePower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ArtfulChanterPower extends AbstractAlicePower implements OnDollOperateHook {
	public static final String POWER_ID = ArtfulChanterPower.class.getSimpleName();
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	
	public ArtfulChanterPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.initializeImage(null);
	}
	
	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}
	
	@Override
	public void updateDescription() {
		this.description = String.format(
				powerStrings.DESCRIPTIONS[0],
				this.amount
		);
	}
	
	@Override
	public void postRecycleDoll(AbstractDoll doll) {
		this.flash();
		
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				this.owner,
				this.owner,
				new StrengthPower(this.owner, this.amount),
				this.amount
		));
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				this.owner,
				this.owner,
				new DexterityPower(this.owner, this.amount),
				this.amount
		));
		
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				this.owner,
				this.owner,
				new LoseStrengthPower(this.owner, this.amount),
				this.amount
		));
		AliceSpireKit.addActionToBuffer(new ApplyPowerAction(
				this.owner,
				this.owner,
				new LoseDexterityPower(this.owner, this.amount),
				this.amount
		));
		
		AliceSpireKit.commitBuffer();
	}
}
