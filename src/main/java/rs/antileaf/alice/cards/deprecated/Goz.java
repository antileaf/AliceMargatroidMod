package rs.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.utils.AliceHelper;

@Deprecated
public class Goz extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Goz.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = -2;

	private static final int DAMAGE = 10;
	private static final int UPGRADE_PLUS_DAMAGE = 4;

	public Goz() {
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
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		if (mo == null && AliceHelper.isInBattle()) {
			AbstractMonster target = null;

			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (!m.isDeadOrEscaped()) {
					if (target == null || m.currentHealth < target.maxHealth)
						target = m;
				}
			}

			super.calculateCardDamage(target);
		}
		else
			super.calculateCardDamage(mo);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}

	@Override
	public AbstractCard makeCopy() {
		return new Goz();
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
