package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.unique.ForbiddenMagicPower;
import rs.antileaf.alice.utils.AliceHelper;

public class ForbiddenMagic extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ForbiddenMagic.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 7;

	public ForbiddenMagic() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int amount = this.upgraded ? this.magicNumber : (int) DollManager.get().getDolls().stream()
				.filter(d -> d instanceof EmptyDollSlot)
				.count();

		if (amount > 0)
			this.addToBot((new ApplyPowerAction(p, p, new RetainCardPower(p, amount))));

		this.addToBot(new ApplyPowerAction(p, p, new ForbiddenMagicPower(1)));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ForbiddenMagic();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
