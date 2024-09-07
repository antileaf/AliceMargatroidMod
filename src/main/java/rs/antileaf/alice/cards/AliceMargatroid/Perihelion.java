package rs.antileaf.alice.cards.AliceMargatroid;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Perihelion extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Perihelion.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int DAMAGE = 12;
	private static final int UPGRADE_PLUS_DAMAGE = 4;
	private static final int MAGIC = 3;
	
	private boolean hasBeenInitialized = false;
	private int initialCost;
	private int initialBaseDamage;
	private int initialBaseMagicNumber;
	private boolean initialUpgraded;
	
	public Perihelion() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.isMultiDamage = true;
		
		this.setInitialState(null);
	}
	
	public void setInitialState(Perihelion other) {
		if (other == null) {
			this.initialCost = this.cost;
			this.initialBaseDamage = this.baseDamage;
			this.initialBaseMagicNumber = this.baseMagicNumber;
			this.initialUpgraded = this.upgraded;
		}
		else {
			this.initialCost = other.cost;
			this.initialBaseDamage = other.baseDamage;
			this.initialBaseMagicNumber = other.baseMagicNumber;
			this.initialUpgraded = other.upgraded;
		}
		
		this.hasBeenInitialized = true;
		AliceSpireKit.log("Perihelion has been initialized!");
	}
	
	private boolean shouldTriggerEffect() {
		if (!this.hasBeenInitialized) {
			AliceMargatroidMod.logger.warn("Perihelion has not been initialized!");
			return false;
		}
		
		if (this.costForTurn != this.initialCost ||
				this.baseDamage != this.initialBaseDamage ||
				this.baseMagicNumber != this.initialBaseMagicNumber ||
				this.upgraded != this.initialUpgraded)
			return true;
		
		if (!CardModifierManager.modifiers(this).isEmpty())
			return true;
		
		return false;
	}
	
	@Override
	public void applyPowers() {
		if (this.shouldTriggerEffect()) {
			int originalBaseDamage = this.baseDamage;
			this.baseDamage *= this.magicNumber;
			super.applyPowers();
			this.baseDamage = originalBaseDamage;
			this.isDamageModified = this.damage != this.baseDamage;
		}
		else
			super.applyPowers();
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		if (this.shouldTriggerEffect()) {
			int originalBaseDamage = this.baseDamage;
			this.baseDamage *= this.magicNumber;
			super.calculateCardDamage(mo);
			this.baseDamage = originalBaseDamage;
			this.isDamageModified = this.damage != this.baseDamage;
		}
		else
			super.calculateCardDamage(mo);
	}
	
	@Override
	public void triggerOnGlowCheck() {
		if (this.shouldTriggerEffect())
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.FIRE));
	}
	
	@Override
	public AbstractCard makeCopy() {
		Perihelion card = new Perihelion();
		card.setInitialState(this);
		return card;
	}
	
	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard c = super.makeStatEquivalentCopy();
		
		if (c instanceof Perihelion)
			((Perihelion) c).setInitialState(this);
		else {
			AliceSpireKit.log("makeStatEquivalentCopy returned a card that is not an instance of Perihelion!");
			AliceSpireKit.log("Original card: " + this);
			AliceSpireKit.log("Copy: " + c);
		}
		
		return c;
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
