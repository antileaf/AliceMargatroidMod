package rs.antileaf.alice.utils;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.jetbrains.annotations.NotNull;
import rs.antileaf.alice.AliceMargatroidMod;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
		return "img/" + type + "/" + name + ".png";
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
	
	public static String getOrbImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("orbs", name);
	}
	
	public static String getPowerImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("powers", name);
	}
	
	public static String getLocalizationFilePath(String name) {
		return "local/" + AliceSpireKit.getLangShort() + "/" + name + ".json";
	}
	
	public static void loadCustomStrings(Class<?> clz, String name) {
		String lang = AliceSpireKit.getLangShort();
		
		String path = AliceSpireKit.getLocalizationFilePath(name);
		String buf = Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(clz, buf);
	}
	
	public static UIStrings getUIString(@NotNull String id) {
		return CardCrawlGame.languagePack.getUIString(id);
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
	
	public static AbstractMonster getMonsterByIndex(int index) { // 0-based index
		int cnt = 0;
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
	
	public static String coloredNumber(int amount, int baseAmount) {
		if (amount != baseAmount)
			return FontHelper.colorString("" + amount,
				amount > baseAmount ? "g" : "r");
		else
			return "" + amount;
	}
	
	public static void log(String what) {
		AliceMargatroidMod.logger.info(what);
	}
	
	public static void log(Object who, Object what) {
		AliceMargatroidMod.logger.info(who.getClass().getSimpleName() + " : " + what);
	}
}
