package rs.antileaf.alice.cards.AliceMargatroid;

import ThMod.characters.Marisa;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.common.AliceDiscoverAction;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;
import theLibrarian.characters.TheLibrarian;

import java.util.ArrayList;

public class WitchsTeaParty extends AbstractAliceCard {
	public static final String SIMPLE_NAME = WitchsTeaParty.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 3;
	
	public WitchsTeaParty() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		
//		if (AliceMargatroidMod.postInitialize)
		this.updateFlavor();
	}
	
	public void updateFlavor() {
		FlavorText.AbstractCardFlavorFields.flavor.set(this,
				String.format(FlavorText.CardStringsFlavorField.flavor.get(cardStrings),
						cardStrings.EXTENDED_DESCRIPTION[AliceConfigHelper.enableWitchsTeaPartyFeature() ? 1 : 2]));
		
		this.initializeDescription();
	}
	
	private AbstractCard getRandomSkill(CardColor color) {
		ArrayList<AbstractCard> choices = new ArrayList<>();
		
		for (AbstractCard card : CardLibrary.cards.values())
			if (card.color == color && card.type == CardType.SKILL && !card.hasTag(CardTags.HEALING))
				choices.add(card);
		
		return choices.get(AbstractDungeon.cardRandomRng.random(choices.size() - 1));
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<CardColor> colors = new ArrayList<>();
		
		if (AliceConfigHelper.enableWitchsTeaPartyFeature()) {
			if (AliceSpireKit.isMarisaModAvailable() && !(p instanceof Marisa))
				colors.add(ThMod.patches.AbstractCardEnum.MARISA_COLOR);
			if (AliceSpireKit.isPatchouliModAvailable() && !(p instanceof TheLibrarian))
				colors.add(TheLibrarian.Enums.PATCHOULI_LAVENDER);
		}
		
//		if (!(p instanceof AliceMargatroid))
//			colors.add(AbstractCardEnum.ALICE_MARGATROID_COLOR);
		
		while (colors.size() < this.magicNumber) {
			CardColor color = CardCrawlGame.characterManager.getAllCharacters()
					.get(AbstractDungeon.cardRandomRng.random(CardCrawlGame.characterManager.getAllCharacters().size() - 1))
					.getCardColor();
			
			if (color != p.getCardColor() && !colors.contains(color))
				colors.add(color);
		}
		
		ArrayList<AbstractCard> choices = colors.stream()
				.map(this::getRandomSkill)
				.map(AbstractCard::makeCopy)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		
		if (this.upgraded)
			for (AbstractCard c : choices)
				c.setCostForTurn(0);
		
		this.addToBot(new AliceDiscoverAction(
				choices,
				(card) -> {
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
		return new WitchsTeaParty();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
	
	public static void updateAll() {
		AbstractCard card = CardLibrary.cards.get(WitchsTeaParty.ID);
		if (card instanceof WitchsTeaParty)
			((WitchsTeaParty) card).updateFlavor();
	}
}
