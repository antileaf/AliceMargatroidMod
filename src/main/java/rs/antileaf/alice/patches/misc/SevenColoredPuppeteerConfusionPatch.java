package rs.antileaf.alice.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.unique.RandomizeHandCostAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.cards.AliceMargatroid.SevenColoredPuppeteer;

public class SevenColoredPuppeteerConfusionPatch {
	@SpirePatch(
			clz = ConfusionPower.class,
			method = "onCardDraw",
			paramtypez = {AbstractCard.class}
	)
	public static class ConfusionPatch {
		public static SpireReturn<Void> Prefix(ConfusionPower _inst, AbstractCard c) {
			if (c instanceof SevenColoredPuppeteer) {
				SevenColoredPuppeteer card = (SevenColoredPuppeteer) c;
				
				int newCost = AbstractDungeon.cardRandomRng.random(3);
				if (card.cost != newCost)
					card.updateCost(newCost - card.cost);
				
				card.freeToPlayOnce = false;
				
				return SpireReturn.Return(null);
			}
			else
				return SpireReturn.Continue();
		}
	}
	
	@SpirePatch(
			clz = RandomizeHandCostAction.class,
			method = "update"
	)
	public static class SneckoOilPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "cardRandomRng"));
				return new int[]{tmp[0] + 1};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class, localvars = {"card", "newCost"})
		public static void Insert(RandomizeHandCostAction _inst, AbstractCard card, @ByRef int[] newCost) {
			if (card instanceof SevenColoredPuppeteer) {
				SevenColoredPuppeteer puppeteer = (SevenColoredPuppeteer) card;
				puppeteer.updateCost(newCost[0] - puppeteer.cost);
				newCost[0] = puppeteer.cost;
			}
		}
	}
}
