package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AliceFatalAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.common.AliceExtraTurnPower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class ButterflyFlurry extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ButterflyFlurry.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int DAMAGE = 16;
	private static final int UPGRADED_COST = 1;
	
	public ButterflyFlurry() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AliceFatalAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.FIRE,
				(fatal) -> {
					if (fatal)
						AliceSpireKit.addToBot(new ApplyPowerAction(p, p, new AliceExtraTurnPower(1), 1));
				}
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ButterflyFlurry();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
