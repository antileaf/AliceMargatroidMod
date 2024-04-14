package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AliceExhaustSpecificCardAction;
import rs.antileaf.alice.action.common.AliceSelectCardsInHandAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;

public class Furnace extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Furnace.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 4;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	private static final int MAGIC2 = 4;
	
	public Furnace() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AliceSelectCardsInHandAction(
				this.magicNumber,
				cardStrings.EXTENDED_DESCRIPTION[0],
				true,
				true,
				(c) -> true,
				(cards) -> {
					for (AbstractCard card : cards)
						this.addToBot(new AliceExhaustSpecificCardAction(card, p.hand));
					
					int modifier = cards.size() * this.magicNumber;
					this.baseDamage += modifier;
					this.applyPowers();
					
					this.addToBot(new DamageAction(
							m,
							new DamageInfo(p, this.damage, this.damageTypeForTurn),
							AbstractGameAction.AttackEffect.FIRE));
					
					this.baseDamage -= modifier;
					this.applyPowers();
				},
				false
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Furnace();
	}
	
//	@Override
//	public AbstractCard makeStatEquivalentCopy() {
//		Furnace copy = (Furnace) super.makeStatEquivalentCopy();
//		copy.baseDamage = this.baseDamage;
//		copy.damage = this.damage;
//		copy.initializeDescription();
//		return copy;
//	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
