package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceHelper;

public class IllusoryMoon extends AbstractAliceCard {
	public static final String SIMPLE_NAME = IllusoryMoon.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int DAMAGE = 6;
	private static final int DAMAGE2 = 9;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_DAMAGE = 3;
	
	public IllusoryMoon() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.secondaryDamage = this.baseSecondaryDamage = DAMAGE2;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.isMultiDamage = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.isMultiDamage = false;
		this.calculateCardDamage(m);
		this.addToBot(new DamageAction(m,
				new DamageInfo(p, this.secondaryDamage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		
		this.addToBot(new WaitAction(0.15F));
		
		this.isMultiDamage = true;
		this.calculateCardDamage(null);
		for (int i = 0; i < this.magicNumber; i++)
			this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
					AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new IllusoryMoon();
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
