package rs.antileaf.alice.powers.unique;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import rs.antileaf.alice.doll.interfaces.OnDollOperateHook;
import rs.antileaf.alice.utils.AliceHelper;

public class TauntedPower extends TwoAmountPower implements OnDollOperateHook {
	public static final String SIMPLE_NAME = TauntedPower.class.getSimpleName();
	public static final String POWER_ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public TauntedPower(AbstractMonster owner, int amount, int amount2) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID + "_" + amount2;
		this.owner = owner;
		this.amount = amount;
		this.amount2 = amount2;
		
		this.type = PowerType.DEBUFF;
		this.updateDescription();
		this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(
				AliceHelper.getPowerImgFilePath(SIMPLE_NAME + "84")), 0, 0, 84, 84);
		this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(
				AliceHelper.getPowerImgFilePath(SIMPLE_NAME + "32")), 0, 0, 32, 32);
	}

	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.amount += stackAmount;
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount, this.amount2);
	}

	@Override
	public void onInitialApplication() {
		for (AbstractPower other : this.owner.powers)
			if (other instanceof TauntedPower && !other.ID.equals(this.ID))
				this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, other));
	}

	@Override
	public void atEndOfRound() {
		this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
	}
}
