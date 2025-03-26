package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.doll.RecycleDollAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.targeting.AliceHoveredTargets;
import rs.antileaf.alice.utils.AliceHelper;

public class ReturnInanimateness extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ReturnInanimateness.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 3;
	private static final int UPGRADED_COST = 2;
	private static final int DAMAGE = 2;
	
	public ReturnInanimateness() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
	}
	
	@Override
	public AliceHoveredTargets getHoveredTargets(AbstractMonster mon, AbstractDoll slot) {
		return AliceHoveredTargets.fromDolls(DollManager.get().getDolls().stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.toArray(AbstractDoll[]::new));
	}
	
	public int getMultiplier() {
		return 1 << DollManager.get().getDollTypeCount();
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster m) {
		int tmpBaseDamage = this.baseDamage;
		this.baseDamage *= this.getMultiplier();
		super.calculateCardDamage(m);
		this.baseDamage = tmpBaseDamage;
		this.isDamageModified = this.damage != this.baseDamage;
	}
	
	@Override
	public void applyPowers() {
		int tmpBaseDamage = this.baseDamage;
		this.baseDamage *= this.getMultiplier();
		super.applyPowers();
		this.baseDamage = tmpBaseDamage;
		this.isDamageModified = this.damage != this.baseDamage;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractDoll doll : DollManager.get().getDolls())
			if (!(doll instanceof EmptyDollSlot))
				this.addToBot(new RecycleDollAction(doll));
		
		this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
				AbstractGameAction.AttackEffect.FIRE));
		
//		int tmpBaseDamage = this.baseDamage;
//		this.baseDamage *= this.getMultiplier();
//		this.calculateCardDamage(null);
//
//		int[] tmpMultiDamage = this.multiDamage.clone();
//		this.addToBot(new DamageAllEnemiesAction(p, tmpMultiDamage, this.damageTypeForTurn,
//				AbstractGameAction.AttackEffect.FIRE));
//
//		this.baseDamage = tmpBaseDamage;
//		this.calculateCardDamage(null);
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ReturnInanimateness();
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
