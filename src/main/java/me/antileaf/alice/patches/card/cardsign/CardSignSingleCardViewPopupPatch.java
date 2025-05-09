package me.antileaf.alice.patches.card.cardsign;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import me.antileaf.alice.cards.AbstractAliceCard;

@SuppressWarnings("unused")
public class CardSignSingleCardViewPopupPatch {
	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTitle",
			paramtypez = {SpriteBatch.class})
	public static class SingleCardViewPatch {
		private static float drawScale = 2.0F;
		private static float yOffsetBase = 690.0F;
		
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card, float ___current_y) {
			if (___card instanceof AbstractAliceCard) {
				AbstractAliceCard card = (AbstractAliceCard) ___card;
				card.renderCardSign(
						sb,
						(float)Settings.WIDTH / 2.0F,
						___current_y,
						yOffsetBase,
						drawScale
				);
			}
		}
	}
}
