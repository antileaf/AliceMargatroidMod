package me.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import me.antileaf.alice.action.doll.DollGainBlockAction;
import me.antileaf.alice.action.doll.HealDollAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollDamageInfo;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.enums.DollAmountTime;
import me.antileaf.alice.doll.enums.DollAmountType;
import me.antileaf.alice.strings.AliceDollStrings;
import me.antileaf.alice.utils.AliceHelper;

import java.util.Arrays;

public class KyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = KyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 8;
//	public static final int PASSIVE_AMOUNT = 4;
	
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

		this.tipImg = ImageMaster.loadImage(AliceHelper.getImgFilePath("UI", "Icon_Heal"));
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
	public void onAct(DollActModifier modifier) {
		AliceHelper.addActionToBuffer(new HealDollAction(this, Math.max(1, this.maxHP / 2)));

		if (modifier.theSetup) {
			int[] matrix = DollDamageInfo.createDamageMatrix(
					this.HP,
					this,
					DollAmountType.DAMAGE,
					DollAmountTime.PASSIVE);

			AliceHelper.addActionToBuffer(new DollGainBlockAction(this, Arrays.stream(matrix).sum()));
		}
	}

//	@Override
//	public void postOtherDollDestroyed(AbstractDoll doll) {
//		if (doll instanceof EmptyDollSlot)
//			AliceSpireKit.log("FranceDoll", "EmptyDollSlot destroyed");
//		else
//			AliceSpireKit.addToBot(new DollActAction(this));
//	}

	@Override
	public void onLoseHP(int amount) {
//		AliceHelper.addToTop(new IncreaseDollMaxHealthAction(this, amount, true));
	}

	@Override
	public void heal(int amount) {
		int overHeal = this.HP + amount - this.maxHP;

		super.heal(amount);

		if (overHeal > 0) {
			int[] matrix = DollDamageInfo.createDamageMatrix(
					overHeal,
					this,
					DollAmountType.DAMAGE,
					DollAmountTime.PASSIVE);

			AbstractGameAction action = new DamageAllEnemiesAction(
					AbstractDungeon.player,
					matrix,
					DamageInfo.DamageType.THORNS,
					AbstractGameAction.AttackEffect.FIRE,
					true
			);
			DollManager.get().addDollAction(action);
			this.addToTop(action);
		}
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
