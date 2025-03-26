package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.action.doll.DollGainBlockAction;
import rs.antileaf.alice.action.doll.HealDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceHelper;

import java.util.Arrays;

public class KyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = KyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 8;
//	public static final int ACT_AMOUNT = 3;
	
	public KyotoDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceHelper.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.NONE
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.MAGIC;
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
		int[] matrix = DollDamageInfo.createDamageMatrix(
				this.HP / 2,
				this,
				DollAmountType.DAMAGE,
				DollAmountTime.PASSIVE);

		this.specialBuffer = Arrays.stream(matrix).sum();

		AliceHelper.addActionToBuffer(new DamageAllEnemiesAction(
				AbstractDungeon.player,
				matrix,
				DamageInfo.DamageType.THORNS,
				AbstractGameAction.AttackEffect.FIRE,
				true
		));
		
//		this.highlightActValue();
	}

	@Override
	public void onSpecialAct() {
		this.onAct();

		AliceHelper.addActionToBuffer(new DollGainBlockAction(this, this.specialBuffer));
		this.specialBuffer = 0;
	}
	
	@Override
	public void onEndOfTurn() {
		int amount = (this.maxHP - this.HP) / 2;
		
		if (amount > 0)
			AliceHelper.addActionToBuffer(new HealDollAction(this, amount));
	}
	
//	@Override
//	public void postOtherDollDestroyed(AbstractDoll doll) {
//		if (doll instanceof EmptyDollSlot)
//			AliceSpireKit.log("FranceDoll", "EmptyDollSlot destroyed");
//		else
//			AliceSpireKit.addToBot(new DollActAction(this));
//	}
	
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
		return new KyotoDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 8.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 28.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new KyotoDoll()).desc();
	}
}
