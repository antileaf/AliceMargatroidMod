package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.unique.SunlightRayAction;
import rs.antileaf.alice.action.unique.SunlightRayPostAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.patches.enums.CardTagEnum;
import rs.antileaf.alice.powers.unique.FrostRayPower;

public class SunlightRay extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SunlightRay.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int DAMAGE = 1;
	private static final int MAGIC = 5;
	private static final int UPGRADE_PLUS_MAGIC = 2;
	
	public SunlightRay() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ALL_ENEMY
		);
		
		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.tags.add(CardTagEnum.ALICE_RAY);
	}
	
	@Override
	public void applyPowers() {
		super.applyPowers();
		this.damage = this.baseDamage = DAMAGE;
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		this.damage = this.baseDamage = DAMAGE;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		SunlightRayPostAction postAction = new SunlightRayPostAction();
		this.addToBot(new SunlightRayAction(postAction, this.damage, this.magicNumber));
		this.addToBot(postAction);
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SunlightRay();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
