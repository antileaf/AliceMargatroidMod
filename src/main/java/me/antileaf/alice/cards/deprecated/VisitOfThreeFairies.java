package me.antileaf.alice.cards.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.common.AliceDiscoverAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class VisitOfThreeFairies extends AbstractAliceCard {
	public static final String SIMPLE_NAME = VisitOfThreeFairies.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	
	public VisitOfThreeFairies() {
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
		
		this.exhaust = true;
	}
	
	private AbstractCard getRandomCard(CardColor color, CardType type, CardRarity rarity) {
		ArrayList<AbstractCard> choices = new ArrayList<>();
		
		for (AbstractCard card : CardLibrary.cards.values())
			if (card.color == color && card.type == type && card.rarity == rarity && !card.hasTag(CardTags.HEALING))
				choices.add(card);
		
		return choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<CardColor> colors = CardCrawlGame.characterManager.getAllCharacters().stream()
				.map(AbstractPlayer::getCardColor)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		
		ArrayList<CardType> types = new ArrayList<>();
		types.add(CardType.ATTACK);
		types.add(CardType.SKILL);
		types.add(CardType.POWER);
		
		ArrayList<CardRarity> rarities = new ArrayList<>();
		rarities.add(CardRarity.COMMON);
		rarities.add(CardRarity.UNCOMMON);
		rarities.add(CardRarity.RARE);
		
		ArrayList<AbstractCard> choices = new ArrayList<>();
		
		for (CardRarity rarity : rarities) {
			CardColor color;
			CardType type;
			
			do {
				color = colors.get(AbstractDungeon.cardRandomRng.random(colors.size() - 1));
				type = types.get(AbstractDungeon.cardRandomRng.random(types.size() - 1));
			} while (rarity == CardRarity.COMMON && type == CardType.POWER);
			
			AbstractCard card = this.getRandomCard(color, type, rarity).makeCopy();
			card.setCostForTurn(0);
			choices.add(card);
			
			colors.remove(color);
			types.remove(type);
		}
		
		this.addToBot(new AliceDiscoverAction(
				choices,
				(card) -> {
					this.addToTop(new AnonymousAction(() -> {
						AliceHelper.addCardToHand(card);
					}));
				},
				cardStrings.EXTENDED_DESCRIPTION[0],
				true
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new VisitOfThreeFairies();
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
