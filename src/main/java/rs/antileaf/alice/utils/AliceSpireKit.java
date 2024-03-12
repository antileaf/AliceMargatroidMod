package rs.antileaf.alice.utils;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.jetbrains.annotations.NotNull;
import rs.antileaf.alice.AliceMagtroidMod;

import java.nio.charset.StandardCharsets;

public abstract class AliceSpireKit {
	public static String getModID() {
		return AliceMagtroidMod.SIMPLE_NAME;
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
		return AliceSpireKit.getImgFilePath("cards/AliceMagtroid", name);
	}
	
	public static String getOrbImgFilePath(String name) {
		return AliceSpireKit.getImgFilePath("orbs", name);
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
	
	public static void addEffect(AbstractGameEffect effect) {
		AbstractDungeon.effectList.add(effect);
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
	
	public static String coloredNumber(int amount, int baseAmount) {
		if (amount != baseAmount)
			return FontHelper.colorString("" + amount,
				amount > baseAmount ? "g" : "r");
		else
			return "" + amount;
	}
	
	public static void log(String what) {
		AliceMagtroidMod.logger.info(what);
	}
	
	public static void log(Object who, Object what) {
		AliceMagtroidMod.logger.info(who.getClass().getSimpleName() + " : " + what);
	}
}
