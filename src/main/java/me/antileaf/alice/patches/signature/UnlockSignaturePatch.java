package me.antileaf.alice.patches.signature;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomUnlock;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.characters.AliceMargatroid;
import me.antileaf.alice.patches.enums.LibraryTypeEnum;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class UnlockSignaturePatch {
	private static final Logger logger = LogManager.getLogger(UnlockSignaturePatch.class.getName());

	private static ArrayList<AbstractAliceCard> getCardsToUnlock() {
		ArrayList<AbstractAliceCard> locked = CardLibrary.getCardList(LibraryTypeEnum.ALICE_MARGATROID_COLOR).stream()
				.filter(c -> c instanceof AbstractAliceCard)
				.map(c -> (AbstractAliceCard) c)
				.filter(c -> c.hasSignature)
				.filter(c -> !SignatureHelper.isUnlocked(c.cardID))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		if (locked.isEmpty() || AbstractDungeon.bossCount == 0)
			return null;

		int count = AbstractDungeon.ascensionLevel < 20 ?
				Math.min(3, AbstractDungeon.bossCount) :
				AbstractDungeon.bossCount >= 4 ? 3 : Math.min(2, AbstractDungeon.bossCount);

		ArrayList<AbstractAliceCard> inDeck = AbstractDungeon.player.masterDeck.group.stream()
				.filter(c -> c instanceof AbstractAliceCard)
				.map(c -> (AbstractAliceCard) c)
				.filter(c -> locked.stream()
						.anyMatch(cc -> cc.cardID.equals(c.cardID)))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

		ArrayList<AbstractAliceCard> toRemove = locked.stream()
				.filter(c -> inDeck.stream()
						.anyMatch(cc -> cc.cardID.equals(c.cardID)))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		locked.removeAll(toRemove);

		logger.info("inDeck: {}", inDeck);
		logger.info("locked: {}", locked);

		ArrayList<AbstractAliceCard> res = new ArrayList<>();
		while (res.size() < count && !inDeck.isEmpty())
			res.add(inDeck.remove(AbstractDungeon.eventRng.random(0, inDeck.size() - 1)));

		while (res.size() < count && !locked.isEmpty())
			res.add(locked.remove(AbstractDungeon.eventRng.random(0, locked.size() - 1)));

		return res;
	}

	private static ArrayList<?> patched = null;

	@SpirePatch(clz = GameOverScreen.class, method = "calculateUnlockProgress", paramtypez = {})
	public static class CalculateUnlockProgressPatch {
		@SpirePostfixPatch
		public static void Postfix(GameOverScreen _inst) {
			if (AbstractDungeon.player instanceof AliceMargatroid &&
					ReflectionHacks.getPrivate(_inst, GameOverScreen.class, "unlockBundle") == null) {
				ArrayList<AbstractAliceCard> toUnlock = getCardsToUnlock();

				if (toUnlock == null)
					return;

				ArrayList<AbstractUnlock> unlocks = new ArrayList<>();

				for (AbstractAliceCard card : toUnlock) {
					SignatureHelper.unlock(card.cardID, true);
					SignatureHelper.unlock(card.cardID, true);

					AbstractUnlock unlock = new CustomUnlock(card.cardID);
					unlock.card = unlock.card.makeCopy();

					unlocks.add(unlock);
				}

				ReflectionHacks.setPrivate(_inst, GameOverScreen.class, "unlockBundle",
						unlocks);

				patched = unlocks;
			}
		}
	}

	private static UIStrings uiStrings = null;

	private static UIStrings getUIStrings() {
		if (uiStrings == null)
			uiStrings = CardCrawlGame.languagePack.getUIString(AliceHelper.makeID("UnlockSignature"));
		return uiStrings;
	}

	public static boolean handle(NeowUnlockScreen screen) {
		if (patched == screen.unlockBundle) {
			AbstractDungeon.dynamicBanner.appear(getUIStrings().TEXT[0]);
			return true;
		}

		return false;
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "open")
	public static class UnlockScreenOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("appearInstantly"))
						m.replace(" { if (" + UnlockSignaturePatch.class.getName() +
								".handle(this)) { } else { $_ = $proceed($$); } }");
				}
			};
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "reOpen")
	public static class UnlockScreenReOpenPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return UnlockScreenOpenPatch.Instrument();
		}
	}

	@SpirePatch(clz = NeowUnlockScreen.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class UnlockScreenRenderPatch {
		@SpireInsertPatch(rloc = 36)
		public static SpireReturn<Void> Insert(NeowUnlockScreen _inst, SpriteBatch sb) {
			if (patched == _inst.unlockBundle) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont,
						getUIStrings().TEXT[1],
						(float) Settings.WIDTH / 2.0F,
						(float)Settings.HEIGHT / 2.0F - 330.0F * Settings.scale,
						Settings.CREAM_COLOR);

				_inst.button.render(sb);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}
}
