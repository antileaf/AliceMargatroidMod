package rs.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.deprecated.ThePhantomOfTheGrandGuignolPower;
import rs.antileaf.alice.utils.AliceHelper;

@Deprecated
public class ThePhantomOfTheGrandGuignol extends AbstractAliceCard {
	public static final String SIMPLE_NAME = ThePhantomOfTheGrandGuignol.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 2;
	private static final int UPGRADED_COST = 1;
	private static final int MAGIC = 1;
	
	public ThePhantomOfTheGrandGuignol() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.POWER,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(
				p,
				p,
				new ThePhantomOfTheGrandGuignolPower(this.magicNumber),
				this.magicNumber
		));
		this.addToBot(new ApplyPowerAction(
				p,
				p,
				new EnergyDownPower(p, 1),
				1
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new ThePhantomOfTheGrandGuignol();
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
