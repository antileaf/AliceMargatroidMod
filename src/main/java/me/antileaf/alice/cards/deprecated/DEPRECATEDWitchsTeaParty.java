package me.antileaf.alice.cards.deprecated;

import ThMod.characters.Marisa;
import basemod.helpers.TooltipInfo;
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
import me.antileaf.alice.strings.AliceCardNoteStrings;
import me.antileaf.alice.utils.AliceConfigHelper;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

@Deprecated
public class DEPRECATEDWitchsTeaParty extends AbstractAliceCard {
	public static final String SIMPLE_NAME = DEPRECATEDWitchsTeaParty.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final AliceCardNoteStrings cardNoteStrings = AliceCardNoteStrings.get(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 3;
	
	public DEPRECATEDWitchsTeaParty() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
		this.exhaust = true;
	}
	
	@Override
	public TooltipInfo getNote() {
		String desc = String.format(cardNoteStrings.DESCRIPTION,
				AliceConfigHelper.enableWitchsTeaPartyFeature() ?
						cardNoteStrings.EXTENDED_DESCRIPTION[0] :
						cardNoteStrings.EXTENDED_DESCRIPTION[1]);
		return new TooltipInfo(cardNoteStrings.TITLE, desc);
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
			if (AliceHelper.isMarisaModAvailable() && !(p instanceof Marisa))
				colors.add(ThMod.patches.AbstractCardEnum.MARISA_COLOR);
//			if (AliceHelper.isPatchouliModAvailable() && !(p instanceof TheLibrarian))
//				colors.add(TheLibrarian.Enums.PATCHOULI_LAVENDER);
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
						AliceHelper.addCardToHand(card);
					}));
				},
				cardStrings.EXTENDED_DESCRIPTION[0],
				true
		));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new DEPRECATEDWitchsTeaParty();
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
		AbstractCard card = CardLibrary.cards.get(DEPRECATEDWitchsTeaParty.ID);
//		if (card instanceof WitchsTeaParty)
//			((WitchsTeaParty) card).updateNote();
	}
}
