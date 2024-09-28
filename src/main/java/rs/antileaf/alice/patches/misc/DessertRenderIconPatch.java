package rs.antileaf.alice.patches.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import rs.antileaf.alice.cards.alice.Dessert;

public class DessertRenderIconPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "renderHand", paramtypez = {SpriteBatch.class})
	public static class RenderIconPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractPlayer _inst, SpriteBatch sb) {
			for (AbstractCard c : _inst.hand.group)
				if (c instanceof Dessert)
					((Dessert) c).renderDessertIcon(sb);
		}
	}
}
