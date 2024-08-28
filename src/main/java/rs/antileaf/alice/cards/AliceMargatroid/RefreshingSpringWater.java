package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class RefreshingSpringWater extends AbstractAliceCard {
	public static final String SIMPLE_NAME = RefreshingSpringWater.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 1;
//	private static final int MAGIC2 = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public RefreshingSpringWater() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
//		this.secondaryMagicNumber = this.baseSecondaryMagicNumber = MAGIC2;
	}
	
	@Override
	public void triggerOnGlowCheck() {
		int dollCount = (int)DollManager.get().getDolls().stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.count();
		
		if (dollCount >= 5)
			this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		else
			this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int dollCount = (int)DollManager.get().getDolls().stream()
				.filter(doll -> !(doll instanceof EmptyDollSlot))
				.count();
		
		if (dollCount >= 5) {
			this.addToBot(new GainEnergyAction(this.magicNumber));
//			this.addToBot(new DrawCardAction(this.secondaryMagicNumber));
		}
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new RefreshingSpringWater();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
