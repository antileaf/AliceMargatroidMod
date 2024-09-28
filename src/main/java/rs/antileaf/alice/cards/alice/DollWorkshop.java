package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.loli.*;
import rs.antileaf.alice.patches.card.LoopSCVPreviewCardsPatch;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.powers.unique.DollWorkshopPower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollWorkshop extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DollWorkshop.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

	private static final int COST = 1;
	private static final int MAGIC = 1;

	public DollWorkshop() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
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
		this.addToBot(new ApplyPowerAction(p, p,
				new DollWorkshopPower(this.magicNumber), this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DollWorkshop();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.isInnate = true;
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}

	public static void registerLoopPreview() {
		LoopSCVPreviewCardsPatch.register(DollWorkshop.class,
				new AbstractCard[] {
						new VivaciousShanghaiDoll_Loli(),
						new QuietHouraiDoll_Loli(),
						new SpringKyotoDoll_Loli(),
						new RedHairedNetherlandsDoll_Loli(),
						new CharitableFranceDoll_Loli(),
						new MistyLondonDoll_Loli(),
						new CharismaticOrleansDoll_Loli()
				}, 2.0F);
	}
}
