package rs.antileaf.alice.cards.AliceMargatroid;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class Dessert extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Dessert.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public Dessert() {
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
	}
	
	@Override
	public void triggerOnOtherCardPlayed(AbstractCard c) {
//		AliceSpireKit.log("Dessert.triggerOnOtherCardPlayed: Triggered.");
		
		if (!AbstractDungeon.player.hand.contains(this)) {
			AliceSpireKit.log("Dessert.triggerOnOtherCardPlayed: This card is not in player's hand.");
			return;
		}
		
		if (!AbstractDungeon.player.hand.contains(c)) {
			AliceSpireKit.log("Dessert.triggerOnOtherCardPlayed: The other card is not in player's hand.");
			return;
		}
		
		if (!AbstractDungeon.player.hand.isEmpty() &&
				AbstractDungeon.player.hand.getTopCard() == c) {
			this.use(AbstractDungeon.player, null);
			this.addToBot(new DiscardSpecificCardAction(this));
			this.flash();
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainEnergyAction(this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Dessert();
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
