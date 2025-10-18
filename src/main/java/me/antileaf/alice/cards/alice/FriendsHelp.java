package me.antileaf.alice.cards.alice;

import ThMod.cards.Marisa.*;
import ThMod.cards.derivations.Spark;
import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.common.AliceAddCardToHandAction;
import me.antileaf.alice.action.common.AliceDiscoverAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.cards.marisa.*;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.patches.marisa.MarisaAlternativeImagePatch;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FriendsHelp extends AbstractAliceCard {
	private static final Logger logger = LogManager.getLogger(FriendsHelp.class);
	
	public static final String SIMPLE_NAME = FriendsHelp.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	
	public FriendsHelp() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.exhaust = true;
		
		if (AliceHelper.isMarisaModAvailable()) {
			MultiCardPreview.add(this, new Spark(), new _6A(), new DoubleSpark(), new AsteroidBelt());
			for (AbstractCard card : MultiCardPreview.multiCardPreview.get(this))
				if (card instanceof CustomCard)
					setAlternativeImg((CustomCard) card);
		}
		else
			MultiCardPreview.add(this, new AliceSpark(), new Alice6A(), new AliceDoubleSpark(), new AliceAsteroidBelt());

		this.dontAvoidSCVPanel = true;
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		return new ArrayList<TooltipInfo>() {{
			add(new TooltipInfo(cardStrings.EXTENDED_DESCRIPTION[1], cardStrings.EXTENDED_DESCRIPTION[2]));
		}};
	}
	
	public static void setAlternativeImg(CustomCard card) {
		if (AliceHelper.isMarisaModAvailable()) {
			if (card instanceof DoubleSpark)
				AbstractAliceMarisaCard.setImages(card, AliceDoubleSpark.SIMPLE_NAME);
			else if (card instanceof AsteroidBelt)
				AbstractAliceMarisaCard.setImages(card, AliceAsteroidBelt.SIMPLE_NAME);
			else if (card instanceof _6A)
				AbstractAliceMarisaCard.setImages(card, Alice6A.SIMPLE_NAME);
			else if (card instanceof Spark)
				AbstractAliceMarisaCard.setImages(card, AliceSpark.SIMPLE_NAME);
			else if (card instanceof DeepEcologicalBomb)
				AbstractAliceMarisaCard.setImages(card, AliceDeepEcologicalBomb.IMG);
			else if (card instanceof SprinkleStarSeal)
				AbstractAliceMarisaCard.setImages(card, AliceSprinkleStarSeal.IMG);
			else if (card instanceof OortCloud)
				AbstractAliceMarisaCard.setImages(card, AliceOortCloud.IMG);
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> choices = new ArrayList<>();
		
		if (!this.upgraded) {
			if (AliceHelper.isMarisaModAvailable()) {
				for (CustomCard card : new CustomCard[]{new DoubleSpark(), new AsteroidBelt(), new _6A()}) {
					setAlternativeImg(card);
					if (card instanceof DoubleSpark)
						MarisaAlternativeImagePatch.AliceMarisaCardFields.isAliceMarisaCard.set(card, true);
					choices.add(card);
				}
			} else {
				choices.add(new AliceDoubleSpark());
				choices.add(new AliceAsteroidBelt());
				choices.add(new Alice6A());
			}
		}
		else {
			if (AliceHelper.isMarisaModAvailable()) {
				for (CustomCard card : new CustomCard[]{new DeepEcologicalBomb(), new SprinkleStarSeal(), new OortCloud()}) {
					setAlternativeImg(card);
//					MarisaAlternativeImagePatch.AliceMarisaCardFields.isAliceMarisaCard.set(card, true);
					choices.add(card);
				}
			} else {
				choices.add(new AliceDeepEcologicalBomb());
				choices.add(new AliceSprinkleStarSeal());
				choices.add(new AliceOortCloud());
			}
		}
		
		for (AbstractCard card : choices)
			card.setCostForTurn(0);
		
		this.addToBot(new AliceDiscoverAction(
				choices,
				(card) -> {
					logger.info("Selected card: {}", card.cardID);
					if (card instanceof CustomCard) {
//						AliceHelper.log("It is an instance of CustomCard.");
						setAlternativeImg((CustomCard) card);
					}
//					else
//						AliceHelper.log("How could this be? Check carefully.");

					this.addToTop(new AliceAddCardToHandAction(card));
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
			
			MultiCardPreview.multiCardPreview.get(this).clear();
			if (AliceHelper.isMarisaModAvailable()) {
				MultiCardPreview.add(this, new DeepEcologicalBomb(), new SprinkleStarSeal(), new OortCloud());
				for (AbstractCard card : MultiCardPreview.multiCardPreview.get(this))
					if (card instanceof CustomCard)
						setAlternativeImg((CustomCard) card);
			}
			else
				MultiCardPreview.add(this, new AliceDeepEcologicalBomb(), new AliceSprinkleStarSeal(),
						new AliceOortCloud());
			
			this.dontAvoidSCVPanel = false;
			this.initializeDescription();
		}
	}
}
