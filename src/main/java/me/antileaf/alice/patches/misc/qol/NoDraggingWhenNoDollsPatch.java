package me.antileaf.alice.patches.misc.qol;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.CardTargetEnum;

@SuppressWarnings("unused")
public class NoDraggingWhenNoDollsPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards",
			paramtypez = {})
	public static class ClickAndDragCardsPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractCard.class, "hasEnoughEnergy"));
//				return new int[]{ tmp[tmp.length - 1] };
//			}
//		}

//		@SpireInsertPatch(locator = Locator.class)
		@SpireInsertPatch(rloc = 121)
		public static SpireReturn<Boolean> Insert(AbstractPlayer _inst) {
			AbstractCard card = _inst.hoveredCard;
			
			if (card.hasEnoughEnergy() && _inst.isHoveringDropZone &&
//					card instanceof AbstractAliceCard &&
					card.target == CardTargetEnum.DOLL && !DollManager.get().hasDoll()) {
				AbstractDungeon.effectList.add(new ThoughtBubble(
						_inst.dialogX, _inst.dialogY, 3.0F,
						card.cantUseMessage, true));
				_inst.releaseCard();
				CardCrawlGame.sound.play("CARD_REJECT");
				return SpireReturn.Return(true);
			}
			
			return SpireReturn.Continue();
		}
	}
}
