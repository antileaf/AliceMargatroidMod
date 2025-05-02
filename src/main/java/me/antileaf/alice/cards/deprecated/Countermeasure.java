package me.antileaf.alice.cards.deprecated;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.common.AliceDiscoverAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

@AutoAdd.Ignore
@Deprecated
public class Countermeasure extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Countermeasure.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 8;
	private static final int UPGRADE_PLUS_MAGIC = 3;
	private static final int DAMAGE = 10;
	private static final int UPGRADE_PLUS_DMG = 4;
	private static final int DAMAGE2 = 3;
	private static final int MAGIC2 = 3;
	private static final int UPGRADE_PLUS_MAGIC2 = 1;

	Chesed chesed = new Chesed();
	Goz goz = new Goz();
	Binah binah = new Binah();

	public Countermeasure() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.ATTACK,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.ALL_ENEMY
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.damage = this.baseDamage = DAMAGE;
		this.secondaryDamage = this.baseSecondaryDamage = DAMAGE2;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);

		this.chesed.baseDamage = this.baseMagicNumber;
		this.chesed.calculateCardDamage(mo);
		this.baseMagicNumber = this.chesed.baseDamage;
		this.magicNumber = this.chesed.damage;
		this.isMagicNumberModified = this.chesed.isDamageModified;

		this.goz.baseDamage = this.baseDamage;
		this.goz.calculateCardDamage(mo);
		this.baseDamage = this.goz.baseDamage;
		this.damage = this.goz.damage;
		this.isDamageModified = this.goz.isDamageModified;

		this.binah.baseDamage = this.baseSecondaryDamage;
		this.binah.calculateCardDamage(mo);
		this.baseSecondaryDamage = this.binah.baseDamage;
		this.secondaryDamage = this.binah.damage;
		this.isSecondaryDamageModified = this.binah.isDamageModified;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		this.chesed.baseDamage = this.baseMagicNumber;
		this.chesed.applyPowers();
		this.baseMagicNumber = this.chesed.baseDamage;
		this.magicNumber = this.chesed.damage;
		this.isMagicNumberModified = this.chesed.isDamageModified;

		this.goz.baseDamage = this.baseDamage;
		this.goz.applyPowers();
		this.baseDamage = this.goz.baseDamage;
		this.damage = this.goz.damage;
		this.isDamageModified = this.goz.isDamageModified;

		this.binah.baseDamage = this.baseSecondaryDamage;
		this.binah.applyPowers();
		this.baseSecondaryDamage = this.binah.baseDamage;
		this.secondaryDamage = this.binah.damage;
		this.isSecondaryDamageModified = this.binah.isDamageModified;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> choices = new ArrayList<>();
		choices.add(this.chesed.makeStatEquivalentCopy());
		choices.add(this.goz.makeStatEquivalentCopy());
		choices.add(this.binah.makeStatEquivalentCopy());

		choices.forEach(AbstractCard::applyPowers);

		this.addToBot(new AliceDiscoverAction(
				choices,
				(chosen) -> {
					if (chosen instanceof Chesed) {
						this.chesed.calculateCardDamage(m);

						this.addToBot(new DamageAllEnemiesAction(p,
								this.chesed.multiDamage,
								this.chesed.damageTypeForTurn,
								AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
					}
					else if (chosen instanceof Goz) {
						AbstractMonster target = null;
						for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
							if (!mo.isDeadOrEscaped() && (target == null || mo.currentHealth < target.currentHealth))
								target = mo;
						}

						this.goz.calculateCardDamage(target);

						this.addToBot(new DamageAction(target,
								new DamageInfo(p, this.goz.damage, this.damageTypeForTurn),
								AbstractGameAction.AttackEffect.BLUNT_HEAVY));
					}
					else if (chosen instanceof Binah) {
						this.binah.calculateCardDamage(m);

						for (int i = 0; i < this.secondaryMagicNumber; i++)
							this.addToBot(new AttackDamageRandomEnemyAction(this.binah,
									AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
					}
				},
				cardStrings.EXTENDED_DESCRIPTION[0],
				false
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Countermeasure();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeDamage(UPGRADE_PLUS_DMG);
			this.upgradeSecondaryMagicNumber(UPGRADE_PLUS_MAGIC2);

			this.chesed.upgrade();
			this.goz.upgrade();
			this.binah.upgrade();

			this.initializeDescription();
		}
	}
}
