package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollDamageAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.relics.SuspiciousCard;
import rs.antileaf.alice.strings.AliceDollStrings;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ShanghaiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = ShanghaiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	private static final AliceDollStrings dollStrings = AliceDollStrings.get(ID);
	
	public static final int MAX_HP = 3;
	public static final int PASSIVE_AMOUNT = 2;
	public static final int ACT_AMOUNT = 4;
	
	public ShanghaiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath(SIMPLE_NAME),
				RenderTextMode.ACT
		);
		
		this.passiveAmountType = DollAmountType.MAGIC;
		this.actAmountType = DollAmountType.DAMAGE;
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
		this.highlightActValue();
		
		if (!AbstractDungeon.player.hasRelic(SuspiciousCard.ID)) {
			AbstractMonster m = AliceSpireKit.getMonsterWithLeastHP();
			
			if (m != null) {
				AliceSpireKit.addActionToBuffer(new DollDamageAction(m,
						new DollDamageInfo(this.actAmount, this,
								DollAmountType.DAMAGE, DollAmountTime.ACT),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
			}
		}
		else {
			if (AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> !m.isDeadOrEscaped())
					.count() > 1)
				AbstractDungeon.player.getRelic(SuspiciousCard.ID).flash();
			
			AliceSpireKit.addActionToBuffer(new DamageAllEnemiesAction(
					AbstractDungeon.player,
					DollDamageInfo.createDamageMatrix(
							this.actAmount,
							this,
							this.actAmountType,
							DollAmountTime.ACT),
					DamageInfo.DamageType.THORNS,
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
					true
			));
		}
	}
	
	@Override
	public void onStartOfTurn() {
		this.baseActAmount += this.passiveAmount;
		this.applyPower();
//		this.highlightActValue();
	}
	
	// The logic of Strength is implemented in AbstractDoll.applyPowers().
	
	@Override
	public void triggerActAnimation() {
		// TODO
	}
	
	@Override
	public AbstractDoll makeCopy() {
		return new ShanghaiDoll();
	}
	
	@Override
	public void playChannelSFX() {}
	
	@Override
	protected float getRenderXOffset() {
		return NUM_X_OFFSET + 6.0F * Settings.scale;
	}
	
	@Override
	protected float getRenderYOffset() {
		return this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 26.0F * Settings.scale;
	}
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new ShanghaiDoll()).desc();
	}
}
