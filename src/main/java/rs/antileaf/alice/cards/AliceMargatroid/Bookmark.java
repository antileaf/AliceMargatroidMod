package rs.antileaf.alice.cards.AliceMargatroid;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;

import java.util.ArrayList;

public class Bookmark extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Bookmark.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 0;
	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public Bookmark() {
		super(
				ID,
				cardStrings.NAME,
				null, // AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.SELF
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new SelectCardsAction(
				p.discardPile.group,
				this.magicNumber,
				cardStrings.EXTENDED_DESCRIPTION[0] +
						this.magicNumber + cardStrings.EXTENDED_DESCRIPTION[1],
				true,
				(c) -> true,
				(cards) -> {
					ArrayList<AbstractCard> copy = new ArrayList<>(cards);
					this.addToTop(new AnonymousAction(() -> {
						for (int i = copy.size() - 1; i >= 0; i--) {
							AbstractCard card = copy.get(i);
							p.discardPile.removeCard(card);
							p.drawPile.addToTop(card);
						}
					}));
				}
		));
		this.addToBot(new DrawCardAction(1));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Bookmark();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
}
