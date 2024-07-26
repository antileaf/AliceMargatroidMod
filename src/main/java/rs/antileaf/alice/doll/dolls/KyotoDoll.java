package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceSpireKit;

public class KyotoDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = KyotoDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 6;
//	public static final int ACT_AMOUNT = 3;
	
	public KyotoDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				MAX_HP,
				-1,
				AliceSpireKit.getOrbImgFilePath("green"),
				RenderTextMode.PASSIVE
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
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
//		AliceSpireKit.addToTop(new DollGainBlockAction(this, this.actAmount));
		AliceSpireKit.addToTop(new DamageAllEnemiesAction(
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
		if (this.HP < this.passiveAmount) {
			int diff = this.passiveAmount - this.HP;
			if (diff > 0) {
				AliceSpireKit.addToTop(new AnonymousAction(() -> {
					this.HP = Math.min(this.maxHP, this.HP + diff);
					AbstractDungeon.effectsQueue.add(new HealEffect(this.hb.cX - this.animX, this.hb.cY, diff));
					this.updateDescription();
				}));
			}
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
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0], this.coloredPassiveAmount());
		
		this.actDescription = dollStrings.DESCRIPTION[1];
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
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new KyotoDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
