package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.unique.MaliceSparkPower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class MaliceSpark extends AbstractAliceCard {
	public static final String SIMPLE_NAME = MaliceSpark.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 12;
	private static final int UPGRADE_PLUS_DAMAGE = 4;
	private static final int MAGIC = 7;
	private static final int MAGIC2 = 3;
	
	public MaliceSpark() {
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
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}
	
//	@Override
//	public void triggerOnGlowCheck() {
//		this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//
//		if (AliceSpireKit.isInBattle()) {
//			if (AbstractDungeon.player.hand.size() <= 1) {
//				if (AbstractDungeon.player.hand.isEmpty() || AbstractDungeon.player.hand.getTopCard() == this)
//					this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
//				else
//					this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
//			}
//		}
//	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(
				m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		
		this.addToBot(new ApplyPowerAction(
				p,
				p,
				new MaliceSparkPower(this.secondaryMagicNumber),
				this.secondaryMagicNumber
		));
		this.addToBot(new DrawCardAction(this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new MaliceSpark();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
			this.initializeDescription();
		}
	}
}
