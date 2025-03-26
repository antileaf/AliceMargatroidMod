package rs.antileaf.alice.utils;

import basemod.AutoAdd;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.cards.alice.Thread;
import rs.antileaf.alice.patches.card.signature.UnlockConditionPatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class SignatureHelper {
	private static final Logger logger = LogManager.getLogger(SignatureHelper.class);

	public static TextureAtlas.AtlasRegion ATTACK_BG_P, SKILL_BG_P, POWER_BG_P;
	public static TextureAtlas.AtlasRegion CARD_TYPE_ATTACK_COMMON, CARD_TYPE_ATTACK_UNCOMMON, CARD_TYPE_ATTACK_RARE;
	public static TextureAtlas.AtlasRegion CARD_TYPE_SKILL_COMMON, CARD_TYPE_SKILL_UNCOMMON, CARD_TYPE_SKILL_RARE;
	public static TextureAtlas.AtlasRegion CARD_TYPE_POWER_COMMON, CARD_TYPE_POWER_UNCOMMON, CARD_TYPE_POWER_RARE;
	public static TextureAtlas.AtlasRegion CARD_TYPE_ATTACK_COMMON_P, CARD_TYPE_ATTACK_UNCOMMON_P, CARD_TYPE_ATTACK_RARE_P;
	public static TextureAtlas.AtlasRegion CARD_TYPE_SKILL_COMMON_P, CARD_TYPE_SKILL_UNCOMMON_P, CARD_TYPE_SKILL_RARE_P;
	public static TextureAtlas.AtlasRegion CARD_TYPE_POWER_COMMON_P, CARD_TYPE_POWER_UNCOMMON_P, CARD_TYPE_POWER_RARE_P;
	public static TextureAtlas.AtlasRegion DESC_SHADOW, DESC_SHADOW_P;
	public static TextureAtlas.AtlasRegion DESC_SHADOW_SMALL, DESC_SHADOW_SMALL_P;

	private static Map<String, Boolean> unlocked, enabled;
	private static final Map<String, Texture> cache = new HashMap<>();

	public static TextureAtlas.AtlasRegion load(String path) {
		Texture t;

		if (cache.containsKey(path))
			t = cache.get(path);
		else {
			if (Gdx.files.internal(path).exists())
				t = ImageMaster.loadImage(path);
			else
				t = null;

			cache.put(path, t);
		}

		return t == null ? null :
				new TextureAtlas.AtlasRegion(t, 0, 0, t.getWidth(), t.getHeight());
	}

	public static void initialize() {
		ATTACK_BG_P = load(AliceHelper.getImgFilePath("1024", "attack_s"));
		SKILL_BG_P = load(AliceHelper.getImgFilePath("1024", "skill_s"));
		POWER_BG_P = load(AliceHelper.getImgFilePath("1024", "power_s"));

		CARD_TYPE_ATTACK_COMMON = load(AliceHelper.getImgFilePath("512", "attack_common"));
		CARD_TYPE_ATTACK_UNCOMMON = load(AliceHelper.getImgFilePath("512", "attack_uncommon"));
		CARD_TYPE_ATTACK_RARE = load(AliceHelper.getImgFilePath("512", "attack_rare"));
		CARD_TYPE_SKILL_COMMON = load(AliceHelper.getImgFilePath("512", "skill_common"));
		CARD_TYPE_SKILL_UNCOMMON = load(AliceHelper.getImgFilePath("512", "skill_uncommon"));
		CARD_TYPE_SKILL_RARE = load(AliceHelper.getImgFilePath("512", "skill_rare"));
		CARD_TYPE_POWER_COMMON = load(AliceHelper.getImgFilePath("512", "power_common"));
		CARD_TYPE_POWER_UNCOMMON = load(AliceHelper.getImgFilePath("512", "power_uncommon"));
		CARD_TYPE_POWER_RARE = load(AliceHelper.getImgFilePath("512", "power_rare"));

		CARD_TYPE_ATTACK_COMMON_P = load(AliceHelper.getImgFilePath("1024", "attack_common"));
		CARD_TYPE_ATTACK_UNCOMMON_P = load(AliceHelper.getImgFilePath("1024", "attack_uncommon"));
		CARD_TYPE_ATTACK_RARE_P = load(AliceHelper.getImgFilePath("1024", "attack_rare"));
		CARD_TYPE_SKILL_COMMON_P = load(AliceHelper.getImgFilePath("1024", "skill_common"));
		CARD_TYPE_SKILL_UNCOMMON_P = load(AliceHelper.getImgFilePath("1024", "skill_uncommon"));
		CARD_TYPE_SKILL_RARE_P = load(AliceHelper.getImgFilePath("1024", "skill_rare"));
		CARD_TYPE_POWER_COMMON_P = load(AliceHelper.getImgFilePath("1024", "power_common"));
		CARD_TYPE_POWER_UNCOMMON_P = load(AliceHelper.getImgFilePath("1024", "power_uncommon"));
		CARD_TYPE_POWER_RARE_P = load(AliceHelper.getImgFilePath("1024", "power_rare"));

		DESC_SHADOW = load(AliceHelper.getImgFilePath("512", "desc_shadow"));
		DESC_SHADOW_P = load(AliceHelper.getImgFilePath("1024", "desc_shadow"));
		DESC_SHADOW_SMALL = load(AliceHelper.getImgFilePath("512", "desc_shadow_small"));
		DESC_SHADOW_SMALL_P = load(AliceHelper.getImgFilePath("1024", "desc_shadow_small"));

		unlocked = new HashMap<>();
		enabled = new HashMap<>();
	}

	public static boolean isUnlocked(String id) {
		if (!unlocked.containsKey(id))
			unlocked.put(id, AliceConfigHelper.isSignatureUnlocked(id));

		return unlocked.get(id);
	}

	public static void unlock(String id, boolean unlock) {
		AliceConfigHelper.setSignatureUnlocked(id, unlock);
		unlocked.put(id, unlock);
	}

	public static boolean isEnabled(String id) {
		if (!enabled.containsKey(id))
			enabled.put(id, AliceConfigHelper.isSignatureEnabled(id));

		return enabled.get(id);
	}

	public static void enable(String id, boolean enable) {
		AliceConfigHelper.setSignatureEnabled(id, enable);
		enabled.put(id, enable);
	}

	public static boolean shouldUseSignature(String id) {
		return isUnlocked(id) && isEnabled(id);
	}

	public static void unlockAll() {
		logger.info("Unlocking all signatures...");

		ArrayList<AbstractAliceCard> cards = new ArrayList<>(), cancel = new ArrayList<>();

		ArrayList<String> packages = new ArrayList<>();
		packages.add("alice");
		packages.add("colorless");
//		if (!AliceHelper.isMarisaModAvailable())
//			packages.add("marisa");

		for (String pkg : packages) {
			new AutoAdd(AliceHelper.getModID())
					.packageFilter("rs.antileaf.alice.cards." + pkg)
					.any(AbstractAliceCard.class, (info, card) -> {
						if (card.hasSignature)
							cards.add(card);
						else if (isUnlocked(card.cardID))
							cancel.add(card);
					});
		}

		for (AbstractAliceCard card : cancel) {
			unlock(card.cardID, false);

			logger.warn("{} does not have a signature. Cancelled.", card.cardID);
		}

		for (AbstractAliceCard card : cards) {
			unlock(card.cardID, true);

			logger.info("Unlocked {} ({}).", card.name, card.cardID);
		}

		unlock(Thread.ID, false); // TODO: This is just for testing
		enable(Thread.ID, false);
	}

	public static boolean hasSignature(AbstractCard card) {
		return card instanceof AbstractAliceCard && ((AbstractAliceCard) card).hasSignature;
	}

	public static String getUnlockCondition(String id) {
		return UnlockConditionPatch.Fields.unlockCondition.get(CardCrawlGame.languagePack.getCardStrings(id));
	}
}
