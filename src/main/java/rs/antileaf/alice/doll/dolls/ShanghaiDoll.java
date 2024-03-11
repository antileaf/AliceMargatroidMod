package rs.antileaf.alice.doll.dolls;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollDamageInfo;
import rs.antileaf.alice.doll.enums.DollAmountTime;
import rs.antileaf.alice.doll.enums.DollAmountType;
import rs.antileaf.alice.utils.AliceMiscKit;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ShanghaiDoll extends AbstractDoll {
	public static final String SIMPLE_NAME = ShanghaiDoll.class.getSimpleName();
	public static final String ID = SIMPLE_NAME;
	public static final OrbStrings dollStrings = CardCrawlGame.languagePack.getOrbString(ID);
	
	public static final int MAX_HP = 3;
	public static final int PASSIVE_AMOUNT = 5;
	public static final int ACT_AMOUNT = 4;
	
	public ShanghaiDoll() {
		super(
				ID,
				dollStrings.NAME,
				MAX_HP,
				PASSIVE_AMOUNT,
				ACT_AMOUNT,
				AliceSpireKit.getOrbImgFilePath("dark"),
				RenderTextMode.BOTH
		);
		
		this.passiveAmountType = DollAmountType.DAMAGE;
		this.actAmountType = DollAmountType.DAMAGE;
	}
	
	@Override
	public void onAct() {
		AbstractMonster m = AliceSpireKit.getMonsterWithLeastHP();
		
		if (m != null)
			this.addToTop(new DamageAction(m,
					new DollDamageInfo(this.actAmount, ShanghaiDoll.class,
							DollAmountType.DAMAGE, DollAmountTime.ACT)));
	}
	
	public void postSpawn() {
		AbstractMonster m = AbstractDungeon.getRandomMonster();
		
		if (m != null)
			this.addToTop(new DamageAction(m,
					new DollDamageInfo(this.passiveAmount, ShanghaiDoll.class,
							DollAmountType.DAMAGE, DollAmountTime.PASSIVE)));
	}
	
	// The logic of Strength is implemented in AbstractDoll.applyPowers().
	
	@Override
	public void updateDescriptionImpl() {
		this.passiveDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[0],
				this.coloredPassiveAmount(),
				dollStrings.DESCRIPTION[1]
		);
		
		this.actDescription = AliceMiscKit.join(
				dollStrings.DESCRIPTION[2],
				this.coloredActAmount(),
				dollStrings.DESCRIPTION[3]
		);
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
}
