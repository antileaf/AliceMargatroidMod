package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.unique.SemiAutomatonPower;
import rs.antileaf.alice.utils.AliceHelper;

public class SemiAutomaton extends AbstractAliceCard {
	public static final String SIMPLE_NAME = SemiAutomaton.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	private static final int MAGIC = 6;
//	private static final int UPGRADE_PLUS_MAGIC = 3;

	public SemiAutomaton() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);

		this.magicNumber = this.baseMagicNumber = MAGIC;

		this.exhaust = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(p, p, new SemiAutomatonPower(this.magicNumber)));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new SemiAutomaton();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
//			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.upgradeBaseCost(UPGRADED_COST);
			this.initializeDescription();
		}
	}
}
