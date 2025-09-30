package me.antileaf.alice.cards.alice;

import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cardmodifier.BookmarkTrickModifier;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.strings.AliceCardNoteStrings;
import me.antileaf.alice.strings.AliceLanguageStrings;
import me.antileaf.alice.utils.AliceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Bookmark extends AbstractAliceCard {
	private static final Logger logger = LogManager.getLogger(Bookmark.class);
	
	public static final String SIMPLE_NAME = Bookmark.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final AliceCardNoteStrings cardNoteStrings = AliceCardNoteStrings.get(ID);
	
	private static final int COST = 1;
	private static final int UPGRADED_COST = 0;
	
	private static ArrayList<AbstractCard> cache = new ArrayList<>(), newCache = new ArrayList<>();
	private static ArrayList<UUID> uuids = new ArrayList<>();
	
	// The logic of calling this is implemented in patches.
	public static void updateCache() {
		if (AliceHelper.isInBattle()) {
			if (!uuids.isEmpty()) { // First turn
				newCache = new ArrayList<>();
				
				for (UUID uuid : uuids)
					for (AbstractCard card : GetAllInBattleInstances.get(uuid))
						if (!(card instanceof Bookmark)) {
							if (!newCache.contains(card))
								newCache.add(card);
							else
								logger.warn("Bookmark cache duplicate: {}", card.name);
						}
				
				uuids.clear();
				
				logger.info("Bookmark cache initialized: {}", newCache.toString());
			}
			
			cache = newCache.stream()
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			newCache = AbstractDungeon.player.hand.group.stream()
					.filter(c -> !(c instanceof Bookmark))
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			
			logger.info("Bookmark cache updated: {} -> {}", cache.size(), newCache.size());
		}
	}
	
	private static ArrayList<AbstractCard> filterCardsInMasterDeck(boolean remove) {
		ArrayList<AbstractCard> cards = new ArrayList<>();
		Map<AbstractCard, Integer> order = new HashMap<>();
		
		for (AbstractCard card : AbstractDungeon.player.masterDeck.group)
			if (CardModifierManager.hasModifier(card, BookmarkTrickModifier.ID)) {
				cards.add(card);
				order.put(card, ((BookmarkTrickModifier) CardModifierManager.getModifiers(card,
						BookmarkTrickModifier.ID).get(0)).order);
				
				if (remove)
					CardModifierManager.removeModifiersById(card, BookmarkTrickModifier.ID, true);
			}
		
		cards.sort(Comparator.comparingInt(order::get));
		
		return cards;
	}
	
	public static void onBattleStart() {
		cache = new ArrayList<>();
		newCache = new ArrayList<>();
		
		uuids = filterCardsInMasterDeck(true).stream()
				.map(c -> c.uuid)
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	public static void postBattle() {
		logger.info("Bookmark post battle");
		
		int order = 0;
		for (AbstractCard c : newCache) {
			if (c instanceof Bookmark)
				continue;
			
			Optional<AbstractCard> card = AbstractDungeon.player.masterDeck.group.stream()
					.filter(mc -> mc.uuid.equals(c.uuid))
					.findFirst();
			
			if (card.isPresent())
				CardModifierManager.addModifier(card.get(), new BookmarkTrickModifier(++order));
		}
		
		cache = new ArrayList<>();
		newCache = new ArrayList<>();
		
		logger.info("Bookmark post battle marked cards: {}",
				AbstractDungeon.player.masterDeck.group.stream()
						.filter(c -> CardModifierManager.hasModifier(c, BookmarkTrickModifier.ID))
						.map(c -> c.name + "(" +
								((BookmarkTrickModifier) CardModifierManager.getModifiers(c, BookmarkTrickModifier.ID)
										.get(0)).order + ")")
						.reduce((a, b) -> a + AliceLanguageStrings.CAESURA_WITH_SPACE + b)
						.orElse("None"));
	}
	
	public Bookmark() {
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
	
	@Override
	public TooltipInfo getNote() {
		if (!AliceHelper.isInBattle()) {
			String desc = cardNoteStrings.DESCRIPTION;
			
			if (AbstractDungeon.isPlayerInDungeon() &&
					AbstractDungeon.player != null &&
					AbstractDungeon.player.masterDeck != null) {
				ArrayList<AbstractCard> cards = filterCardsInMasterDeck(false);
				
				if (!cards.isEmpty()) {
					desc += " NL " + String.format(cardNoteStrings.EXTENDED_DESCRIPTION[2], cards.size());
					desc += " NL " + cards.stream()
							.map(c -> c.name)
							.reduce((a, b) -> a + AliceLanguageStrings.CAESURA_WITH_SPACE + b)
							.orElse("")
							+ AliceLanguageStrings.PERIOD;
				}
			}
			
			return new TooltipInfo(AliceCardNoteStrings.DEFAULT_TITLE, desc);
		}
		
		if (cache.isEmpty())
			return new TooltipInfo(cardNoteStrings.TITLE, cardNoteStrings.EXTENDED_DESCRIPTION[0]);
		
		ArrayList<AbstractCard> filtered = cache.stream()
				.filter(c -> AbstractDungeon.player.hand.contains(c) ||
						AbstractDungeon.player.drawPile.contains(c) ||
						AbstractDungeon.player.discardPile.contains(c) ||
						AbstractDungeon.player.exhaustPile.contains(c))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		
		String desc = String.format(cardNoteStrings.EXTENDED_DESCRIPTION[1], filtered.size());
		desc += " NL " + filtered.stream()
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
