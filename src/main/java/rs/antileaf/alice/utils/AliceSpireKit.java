package rs.antileaf.alice.utils;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.cardmodifier.PhantomCardModifier;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.strings.AliceLanguageStrings;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class AliceSpireKit {
	public static String getModID() {
		return AliceMargatroidMod.SIMPLE_NAME;
	}
	
	public static String getModPrefix() {
		return AliceSpireKit.getModID() + ":";
	}
	
	public static String makeID(String name) {
		return AliceSpireKit.getModPrefix() + name;
	}
	
	public static String getLangShort() {
		return "zhs";
		
//		if (Settings.language == Settings.GameLanguage.ZHS ||
//				Settings.language == Settings.GameLanguage.ZHT) {
//			return "zhs";
//		} else {
//			return "eng";
//		}
	}
	
	public static String getImgFilePath(String type, String name) {
		return "AliceMargatroidMod/img/" + type + "/" + name + ".png";
	}
	
	public static String getCardImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("cards", name);
	}
	
	public static String getRelicImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("relics", name);
	}
	
	public static String getRelicOutlineImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("relics/outline", name);
	}
	
	public static String getRelicLargeImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("relics/large", name);
	}
	
	public static String getOrbImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("orbs", name);
	}
	
	public static String getPowerImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("powers", name);
	}
	
	public static String getEventImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("events", name);
	}
	
	public static String getLocalizationFilePath(String name) {
		return "AliceMargatroidMod/localization/" + AliceSpireKit.getLangShort() + "/" + name + ".json";
	}
	
	public static void loadCustomStrings(Class<?> clz, String name) {
		String lang = AliceSpireKit.getLangShort();
		
		String path = AliceSpireKit.getLocalizationFilePath(name);
		String buf = Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(clz, buf);
	}
	
//	public static UIStrings getUIString(@NotNull String id) {
//		return CardCrawlGame.languagePack.getUIString(id);
//	}
	
	public static void addCardToHand(AbstractCard card) {
		if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE)
			AliceSpireKit.addEffect(new ShowCardAndAddToHandEffect(
					card,
					Settings.WIDTH / 2.0F - (25.0F * Settings.scale + AbstractCard.IMG_WIDTH),
					Settings.HEIGHT / 2.0F));
		else
			AliceSpireKit.addEffect(new ShowCardAndAddToDiscardEffect(
					card,
					Settings.WIDTH / 2.0F + 25.0F * Settings.scale + AbstractCard.IMG_WIDTH,
					Settings.HEIGHT / 2.0F));
	}
	
	public static void addToBot(AbstractGameAction action) {
		AbstractDungeon.actionManager.addToBottom(action);
	}
	
	public static void addToTop(AbstractGameAction action) {
		AbstractDungeon.actionManager.addToTop(action);
	}
	
	public static void addActionsToTop(AbstractGameAction... actions) {
		for (AbstractGameAction action : AliceMiscKit.reversedArray(actions))
			AliceSpireKit.addToTop(action);
	}
	
	static ArrayList<AbstractGameAction> buffer = new ArrayList<>();
	
	public static void addActionToBuffer(AbstractGameAction action) {
		buffer.add(action);
	}
	
	public static void commitBuffer() {
		addActionsToTop(buffer.toArray(new AbstractGameAction[0]));
		buffer.clear();
	}
	
	public static void addEffect(AbstractGameEffect effect) {
		AbstractDungeon.effectList.add(effect);
	}
	
	public static boolean isInBattle() {
		return CardCrawlGame.dungeon != null &&
				AbstractDungeon.isPlayerInDungeon() &&
				AbstractDungeon.currMapNode != null &&
				AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
	}
	
	public static int getMonsterIndex(AbstractMonster m) { // 0-based index
		int index = 0;
		for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
			if (mon == m)
				break;
			
			if (!mon.isDeadOrEscaped())
				index++;
		}
		return index;
	}
	
	@Nullable
	public static AbstractMonster getMonsterByIndex(int index) { // 0-based index
		int cnt = 0;
		
		if (!isInBattle())
			return null;
		
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (m.isDeadOrEscaped())
				continue;
			
			if (cnt++ == index)
				return m;
		}
		return null;
	}
	
	public static AbstractMonster getMonsterWithLeastHP() {
		int minHP = Integer.MAX_VALUE;
		AbstractMonster res = null;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (m.isDeadOrEscaped())
				continue;
			
			if (m.currentHealth < minHP) {
				minHP = m.currentHealth;
				res = m;
			}
		}
		return res;
	}
	
	public static ArrayList<AbstractCard> cardsPlayedThisTurnWithTag(AbstractCard.CardTags tag) {
		ArrayList<AbstractCard> res = new ArrayList<>();
		for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
			if (c.hasTag(tag))
				res.add(c);
		}
		return res;
	}
	
	public static boolean hasPlayedCardThisTurnWithTag(AbstractCard.CardTags tag, AbstractCard except) {
		for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
			if (c != except && c.hasTag(tag))
				return true;
		}
		return false;
	}
	
//	public static HashMap<String, ArrayList<AbstractCard>> handClassifiedByCardID() {
//		HashMap<String, ArrayList<AbstractCard>> res = new HashMap<>();
//		for (AbstractCard c : AbstractDungeon.player.hand.group)
//			res.computeIfAbsent(c.cardID, k -> new ArrayList<>()).add(c);
//		return res;
//	}
//
	public static ArrayList<AbstractCard> duplicateCards(ArrayList<AbstractCard> cards, AbstractCard except) {
		HashSet<AbstractCard> res = new HashSet<>();
		HashSet<String> set = new HashSet<>();
		for (AbstractCard c : cards) {
			if (c == except || PhantomCardModifier.check(c))
				continue;
			
			if (!set.add(c.cardID))
				res.add(c);
		}
		return new ArrayList<>(res);
	}
	
	public static String getDuplicateCardsMessage(ArrayList<AbstractCard> group, AbstractCard except) {
		UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AliceDuplicateCards");
		
		ArrayList<AbstractCard> duplicates = AliceSpireKit.duplicateCards(group, except);
		StringBuilder sb = new StringBuilder(uiStrings.TEXT[0]);
		for (AbstractCard c : duplicates) {
			if (c != duplicates.get(0))
				sb.append(AliceLanguageStrings.CAESURA);
			sb.append(c.name);
		}
		sb.append(AliceLanguageStrings.PERIOD);
		return sb.toString();
	}
	
	public static boolean hasDuplicateCards(ArrayList<AbstractCard> cards, AbstractCard except) {
		HashSet<String> set = new HashSet<>();
		for (AbstractCard c : cards) {
			if (c == except || PhantomCardModifier.check(c))
				continue;
			
			if (!set.add(c.cardID))
				return true;
		}
		return false;
	}
	
	public static void upgradeCardDamage(AbstractCard card, int amount) {
		ReflectionHacks.privateMethod(AbstractCard.class, "upgradeDamage", int.class)
				.invoke(card, amount);
		
		if (card instanceof AbstractAliceCard)
			((AbstractAliceCard)card).upgradeSecondaryDamage(amount);
		
		card.applyPowers();
		// TODO: May need to add some checks here to work with other mods
	}
	
	public static String coloredNumber(int amount, int baseAmount) {
		if (amount != baseAmount)
			return FontHelper.colorString("" + amount,
				amount > baseAmount ? "g" : "r");
		else
			return FontHelper.colorString("" + amount, "b");
	}
	
	static String MARISA_MOD_ID = "TS05_Marisa";
	static String PATCHOULI_MOD_ID = "PatchouliMod";
	
	static HashMap<String, Boolean> modAvailable = new HashMap<>();
	
	public static boolean isModAvailable(String modID) {
		if (!modAvailable.containsKey(modID))
			modAvailable.put(modID, Loader.isModLoaded(modID));
		
		return modAvailable.get(modID);
	}
	
	public static boolean isMarisaModAvailable() {
		return AliceSpireKit.isModAvailable(MARISA_MOD_ID);
	}
	
	public static boolean isPatchouliModAvailable() {
		return AliceSpireKit.isModAvailable(PATCHOULI_MOD_ID);
	}
	
	public static void log(String what) {
		AliceMargatroidMod.logger.info(what);
	}
	
	public static void log(Object who, Object what) {
		AliceMargatroidMod.logger.info("{} : {}", who.getClass().getSimpleName(), what);
	}
	
	public static final Logger logger = AliceMargatroidMod.logger;
}
