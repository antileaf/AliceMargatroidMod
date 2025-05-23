package me.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.strings.AliceDollStrings;
import me.antileaf.alice.utils.AliceHelper;

public class Su_san extends AbstractDoll {
	public static final String SIMPLE_NAME = Su_san.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 4;
	public static final int PASSIVE_AMOUNT = 3;
	
	public Su_san() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				-1,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.PASSIVE
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.MAGIC;

		this.tipImg = ImageMaster.loadImage(AliceHelper.getImgFilePath("UI", "Icon_Poison"));
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
	
	public void triggerPassiveEffect() {
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped())
				AliceHelper.addActionToBuffer(new ApplyPowerAction(
						m,
						AbstractDungeon.player,
						new PoisonPower(m, AbstractDungeon.player, this.passiveAmount),
						this.passiveAmount,
						true
				));
	}
	
	@Override
	public void onEndOfTurn() {
		this.triggerPassiveEffect();
	}
	
	@Override
	public void onAct(DollActModifier modifier) {
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
			if (!m.isDeadOrEscaped() && m.hasPower(PoisonPower.POWER_ID))
				AliceHelper.addActionToBuffer(new PoisonLoseHpAction(
						m,
						m,
						m.getPower(PoisonPower.POWER_ID).amount,
						AbstractGameAction.AttackEffect.POISON)
				);
		
//		AliceSpireKit.commitBuffer();
	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.PASSIVE_DESCRIPTION, this.coloredPassiveAmount());
		this.actDescription = dollStrings.ACT_DESCRIPTION;
	}
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new Su_san();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 6.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 24.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new Su_san()).desc();
	}
}
