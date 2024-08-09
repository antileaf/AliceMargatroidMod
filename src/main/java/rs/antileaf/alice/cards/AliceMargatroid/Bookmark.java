package rs.antileaf.alice.cards.AliceMargatroid;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.patches.enums.AbstractCardEnum;
import rs.antileaf.alice.strings.AliceCardNoteStrings;
import rs.antileaf.alice.strings.AliceLanguageStrings;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.ArrayList;

public class Bookmark extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Bookmark.class.getSimpleName();
//	public static final String ID = AliceSpireKit.makeID(SIMPLE_NAME);
	public static final String ID = SIMPLE_NAME;
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final AliceCardNoteStrings cardNoteStrings = AliceCardNoteStrings.get(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	
	static ArrayList<AbstractCard> cache = new ArrayList<>(), newCache = new ArrayList<>();
	
	// The logic of calling this is implemented in patches.
	public static void updateCache() {
		if (AliceSpireKit.isInBattle()) {
			cache = newCache.stream()
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			newCache = AbstractDungeon.player.hand.group.stream()
					.filter(c -> !(c instanceof Bookmark))
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			
			AliceSpireKit.log("Bookmark cache updated: " + cache.size() + " -> " + newCache.size());
		}
	}
	
	public static void clearCache() {
		cache = new ArrayList<>();
		newCache = new ArrayList<>();
	}
	
	public Bookmark() {
		super(
				ID,
				cardStrings.NAME,
				AliceSpireKit.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.RARE,
				CardTarget.NONE
		);
		
		this.exhaust = true;
	}
	
	@Override
	public TooltipInfo getNote() {
		if (!AliceSpireKit.isInBattle())
			return new TooltipInfo(AliceCardNoteStrings.DEFAULT_TITLE, cardNoteStrings.DESCRIPTION);
		
		if (cache.isEmpty())
			return new TooltipInfo(cardNoteStrings.TITLE, cardNoteStrings.EXTENDED_DESCRIPTION[0]);
		
		String desc = String.format(cardNoteStrings.EXTENDED_DESCRIPTION[1], cache.size());
		desc += " NL " + cache.stream()
				.map(c -> c.name)
				.reduce((a, b) -> a + AliceLanguageStrings.CAESURA_WITH_SPACE + b)
				.orElse("")
				+ AliceLanguageStrings.PERIOD;
		
		return new TooltipInfo(cardNoteStrings.TITLE, desc);
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> tmp = cache.stream()
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		
		if (!tmp.isEmpty())
			this.addToBot(new AnonymousAction(() -> {
				ArrayList<AbstractCard> cards = new ArrayList<>();
				
				for (AbstractCard card : tmp)
					if (AbstractDungeon.player.discardPile.contains(card) ||
						AbstractDungeon.player.drawPile.contains(card) ||
						AbstractDungeon.player.exhaustPile.contains(card))
						cards.add(card);
				
				int countToHand = cards.size();
				
				if (AbstractDungeon.player.hand.size() + cards.size() >= BaseMod.MAX_HAND_SIZE) {
					AbstractDungeon.player.createHandIsFullDialog();
					countToHand = Math.max(0, BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size());
				}
				
				for (int i = 0; i < countToHand; i++) {
					AbstractCard card = cards.get(i);
//					AliceSpireKit.log("Add to hand: " + card.name);
					
					if (AbstractDungeon.player.discardPile.contains(card))
						AbstractDungeon.player.discardPile.moveToHand(card);
					else if (AbstractDungeon.player.drawPile.contains(card))
						AbstractDungeon.player.drawPile.moveToHand(card);
					else if (AbstractDungeon.player.exhaustPile.contains(card))
						AbstractDungeon.player.exhaustPile.moveToHand(card);
				}
				
				for (int i = countToHand; i < cards.size(); i++) {
					AbstractCard card = cards.get(i);
//					AliceSpireKit.log("Add to discard: " + card.name);
					
					if (AbstractDungeon.player.drawPile.contains(card))
						AbstractDungeon.player.drawPile.moveToDiscardPile(card);
					else if (AbstractDungeon.player.exhaustPile.contains(card))
						AbstractDungeon.player.exhaustPile.moveToDiscardPile(card);
				}
				
				for (AbstractCard card : cards)
					card.unfadeOut();
				
				this.addToTop(new WaitAction(Settings.ACTION_DUR_XFAST));
			}));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Bookmark();
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
