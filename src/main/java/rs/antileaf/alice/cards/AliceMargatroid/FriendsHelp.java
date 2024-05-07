package rs.antileaf.alice.cards.AliceMargatroid;

import ThMod.cards.Marisa.AsteroidBelt;
import ThMod.cards.Marisa.DoubleSpark;
import ThMod.cards.Marisa._6A;
import basemod.abstracts.CustomCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AliceDiscoverAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.Marisa.Alice6A;
import rs.antileaf.alice.cards.Marisa.AliceAsteroidBelt;
import rs.antileaf.alice.cards.Marisa.AliceDoubleSpark;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class FriendsHelp extends AbstractAliceCard {
	public static final String SIMPLE_NAME = FriendsHelp.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	
	public FriendsHelp() {
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
		
		this.exhaust = true;
		
		if (AliceSpireKit.isMarisaModAvailable()) {
			MultiCardPreview.add(this, new DoubleSpark(), new AsteroidBelt(), new _6A());
			for (AbstractCard card : MultiCardPreview.multiCardPreview.get(this))
				if (card instanceof CustomCard)
					setAlternativeImg((CustomCard) card);
		}
		else
			MultiCardPreview.add(this, new AliceDoubleSpark(), new AliceAsteroidBelt(), new Alice6A());
	}
	
	public static void setAlternativeImg(CustomCard card) {
		if (AliceSpireKit.isMarisaModAvailable()) {
			if (card instanceof DoubleSpark)
				card.textureImg = AliceSpireKit.getImgFilePath("Marisa/cards", AliceDoubleSpark.class.getSimpleName());
			else if (card instanceof AsteroidBelt)
				card.textureImg = AliceSpireKit.getImgFilePath("Marisa/cards", AliceAsteroidBelt.class.getSimpleName());
			else if (card instanceof _6A)
				card.textureImg = AliceSpireKit.getImgFilePath("Marisa/cards", Alice6A.class.getSimpleName());
			else
				return;
			
			card.loadCardImage(card.textureImg);
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> choices = new ArrayList<>();
		
		if (AliceSpireKit.isMarisaModAvailable()) {
			for (CustomCard card : new CustomCard[]{new DoubleSpark(), new AsteroidBelt(), new _6A()}) {
				if (AliceConfigHelper.enableAlternativeMarisaCardImage())
					setAlternativeImg(card);
				choices.add(card);
			}
		}
		else {
			choices.add(new AliceDoubleSpark());
			choices.add(new AliceAsteroidBelt());
			choices.add(new Alice6A());
		}
		
		if (this.upgraded)
			for (AbstractCard card : choices)
				card.upgrade();
		
		for (AbstractCard card : choices)
			card.setCostForTurn(0);
		
		this.addToBot(new AliceDiscoverAction(
				choices,
				(card) -> {
					AliceSpireKit.log("Selected card: " + card.cardID);
					if (card instanceof CustomCard) {
						AliceSpireKit.log("It is an instance of CustomCard.");
						setAlternativeImg((CustomCard) card);
					}
					else
						AliceSpireKit.log("How could this be? Check carefully.");
					
					this.addToTop(new AnonymousAction(() -> {
						AliceSpireKit.addCardToHand(card);
					}));
				},
				cardStrings.EXTENDED_DESCRIPTION[0],
				true
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new FriendsHelp();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			for (AbstractCard card : MultiCardPreview.multiCardPreview.get(this))
				card.upgrade();
			this.initializeDescription();
		}
	}
}
