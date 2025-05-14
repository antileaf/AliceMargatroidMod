package me.antileaf.alice.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.characters.AliceMargatroid;
import me.antileaf.alice.patches.enums.LibraryTypeEnum;
import me.antileaf.signature.interfaces.EasyUnlockSubscriber;
import me.antileaf.signature.utils.EasyUnlock;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class AliceEasyUnlock implements EasyUnlockSubscriber {
	private static final Logger logger = LogManager.getLogger(AliceEasyUnlock.class.getName());

	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			AliceHelper.makeID("UnlockSignature"));

	private static ArrayList<String> getCardsToUnlock() {
		if (AbstractDungeon.bossCount == 0)
			return null;

		ArrayList<String> locked = CardLibrary.getCardList(LibraryTypeEnum.ALICE_MARGATROID_COLOR).stream()
				.filter(c -> c instanceof AbstractAliceCard)
				.filter(c -> ((AbstractAliceCard) c).hasSignature)
				.map(c -> c.cardID)
				.filter(id -> !SignatureHelper.isUnlocked(id))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		if (locked.isEmpty())
			return null;

		int count = AbstractDungeon.ascensionLevel < 20 ?
				Math.min(3, AbstractDungeon.bossCount) :
				AbstractDungeon.bossCount >= 4 ? 3 : Math.min(2, AbstractDungeon.bossCount);

		ArrayList<String> inDeck = new ArrayList<>();

		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c instanceof AbstractAliceCard) {
				if (((AbstractAliceCard) c).hasSignature && !inDeck.contains(c.cardID) &&
						locked.contains(c.cardID)) {
					inDeck.add(c.cardID);
				}
			}
		}

		locked.removeAll(inDeck);

		logger.info("inDeck: {}", inDeck);
		logger.info("locked: {}", locked);

		ArrayList<String> res = new ArrayList<>();
		while (res.size() < count && !inDeck.isEmpty())
			res.add(inDeck.remove(AbstractDungeon.eventRng.random(0, inDeck.size() - 1)));

		while (res.size() < count && !locked.isEmpty())
			res.add(locked.remove(AbstractDungeon.eventRng.random(0, locked.size() - 1)));

		return res;
	}

	@Override
	public EasyUnlock receiveOnGameOver(GameOverScreen screen) {
		if (AbstractDungeon.player instanceof AliceMargatroid) {
			return new EasyUnlock()
					.IDs(getCardsToUnlock())
					.tip(uiStrings.TEXT[1]);
		}

		return null;
	}
}
