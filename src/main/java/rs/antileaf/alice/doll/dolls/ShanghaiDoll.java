package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.DollActAction;
import rs.antileaf.alice.action.doll.DollDamageAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.relics.SuspiciousCard;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ShanghaiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = ShanghaiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 3;
//	public static final int PASSIVE_AMOUNT = 5;
	public static final int ACT_AMOUNT = 4;
	
	public ShanghaiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				-1,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("red"),
				RenderTextMode.ACT
		);
		
//		this.passiveAmountType = DollAmountType.DAMAGE;
		this.actAmountType = DollAmountType.DAMAGE;
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
				this.addToTop(new DollDamageAction(m,
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
			
			this.addToTop(new DamageAllEnemiesAction(
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
	
	public void postSpawn() {
		this.addToTop(new DollActAction(this));
//		AbstractMonster m = AbstractDungeon.getRandomMonster();
//
//		if (m != null)
//			this.addToTop(new DollDamageAction(m,
//					new DollDamageInfo(this.passiveAmount, this,
//							DollAmountType.DAMAGE, DollAmountTime.PASSIVE),
//							AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//
//		this.highlightPassiveValue();
	}
	
	// The logic of Strength is implemented in AbstractDoll.applyPowers().
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = String.format(dollStrings.DESCRIPTION[0], this.coloredPassiveAmount());
		
		this.actDescription = String.format(dollStrings.DESCRIPTION[1], this.coloredActAmount());
	}
	
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
	
	public static String getDescription() {
		return getHpDescription(MAX_HP) + " NL " + (new ShanghaiDoll()).desc();
	}
	
	public static String getFlavor() {
		return dollStrings.DESCRIPTION[dollStrings.DESCRIPTION.length - 1];
	}
}
