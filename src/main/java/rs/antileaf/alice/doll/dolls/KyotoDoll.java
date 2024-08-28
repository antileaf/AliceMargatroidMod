package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceSpireKit;

public class KyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = KyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 6;
//	public static final int ACT_AMOUNT = 3;
	
	public KyotoDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				-1,
				AliceSpireKit.getOrbImgFilePath(SIMPLE_NAME),
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
		AliceSpireKit.addActionToBuffer(new DamageAllEnemiesAction(
				AbstractDungeon.player,
				DollDamageInfo.createDamageMatrix(
						this.HP / 2,
						this,
						DollAmountType.DAMAGE,
						DollAmountTime.PASSIVE),
				DamageInfo.DamageType.THORNS,
				AbstractGameAction.AttackEffect.FIRE,
				true
		));
		
//		this.highlightActValue();
	}
	
	@Override
	public void onEndOfTurn() {
		int amount = (this.maxHP - this.HP) / 2;
		
		if (amount > 0) {
			AliceSpireKit.addActionToBuffer(new AnonymousAction(() -> {
				this.HP = Math.min(this.maxHP, this.HP + amount);
				AbstractDungeon.effectsQueue.add(new HealEffect(this.hb.cX - this.animX, this.hb.cY, amount));
				this.updateDescription();
			}));
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
