package rs.antileaf.alice.cards.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.utils.AliceHelper;

public class Binah extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Binah.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;

	private static final int DAMAGE = 3;
	private static final int MAGIC = 3;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Binah() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				CardColor.COLORLESS,
				CardRarity.SPECIAL,
				CardTarget.ALL_ENEMY
		);

		this.damage = this.baseDamage = DAMAGE;
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new Binah();
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
