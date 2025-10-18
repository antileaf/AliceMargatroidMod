package me.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
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
	public static final int ACT_AMOUNT = 3;
	
	public Su_san() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				ACT_AMOUNT,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
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
	
	// Passive Effect is moved to patches.doll.SusanPassiveEffectPatch
	@Deprecated
	public void triggerPassiveEffect() {
//		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
//			if (!m.isDeadOrEscaped())
//				AliceHelper.addActionToBuffer(new ApplyPowerAction(
//						m,
//						AbstractDungeon.player,
//						new PoisonPower(m, AbstractDungeon.player, this.passiveAmount),
//						this.passiveAmount,
//						true
//				));
	}
	
	@Override
	public void onEndOfTurn() {
//		this.triggerPassiveEffect();
	}
	
	@Override
	public void onAct(DollActModifier modifier) {
//		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
//			if (!m.isDeadOrEscaped() && m.hasPower(PoisonPower.POWER_ID))
//				AliceHelper.addActionToBuffer(new PoisonLoseHpAction(
//						m,
//						m,
//						m.getPower(PoisonPower.POWER_ID).amount,
//						AbstractGameAction.AttackEffect.POISON)
//				);
		
		AliceHelper.addActionToBuffer(new ApplyPowerToRandomEnemyAction(AbstractDungeon.player,
				new PoisonPower(null, AbstractDungeon.player, this.actAmount),
				this.actAmount, true));
	}
	
//	@Override
//	public boolean skipActWaiting() {
//		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
//			if (!m.isDeadOrEscaped() && m.hasPower(PoisonPower.POWER_ID))
//				return false;
//
//		return true;
//	}
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = dollStrings.PASSIVE_DESCRIPTION;
		this.actDescription = String.format(dollStrings.ACT_DESCRIPTION, this.coloredActAmount());
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
