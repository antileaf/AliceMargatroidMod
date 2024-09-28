package rs.antileaf.alice.cards.alice;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AliceExhaustSpecificCardAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class BlackTea extends AbstractAliceCard {
	public static final String SIMPLE_NAME = BlackTea.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = -2;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	private int count = 0;
	
	public BlackTea() {
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
		this.retain = this.selfRetain = true;
		this.cantBePlayed = true;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}
	
	@Override
	public void applyPowers() {
		this.magicNumber = this.baseMagicNumber - this.count;
		this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;
	}
	
	@Override
	public void triggerWhenDrawn() {
		this.aliceTriggerAtStartOfTurn();
	}
	
	@Override
	public void aliceTriggerAtStartOfTurn() {
		this.flash();
		
		this.addToBot(new GainEnergyAction(1));
		
//		this.applyPowers();
		this.count++;
		this.applyPowers();
		if (this.magicNumber <= 0) {
			this.addToBot(new AliceExhaustSpecificCardAction(
					this, AbstractDungeon.player.hand));
		}
		
		this.initializeDescription();
	}
	
	@Override
	public void triggerOnExhaust() {
		this.count = 0;
		this.applyPowers();
		this.initializeDescription();
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new BlackTea();
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		BlackTea card = (BlackTea) super.makeStatEquivalentCopy();
		card.count = this.count;
		card.applyPowers();
		return card;
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.applyPowers();
			this.initializeDescription();
		}
	}
}
