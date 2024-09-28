package rs.antileaf.alice.patches.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.util.HashMap;

public class LoopSCVPreviewCardsPatch {
	public static class Preview {
		public AbstractCard[] previews = null;
		public int currentPreview = 0;
		public float interval = 1.0F;
		public float timer = 1.0F;

		public AbstractCard get() {
			this.timer -= Gdx.graphics.getDeltaTime();
			if (this.timer <= 0.0F) {
				this.currentPreview = (this.currentPreview + 1) % this.previews.length;
				this.timer = this.interval;
			}

			return this.previews[this.currentPreview];
		}
	}

	private static final HashMap<Class<? extends AbstractCard>, Preview> table = new HashMap<>();

	public static void register(Class<? extends AbstractCard> clazz, AbstractCard[] previews, float interval) {
		if (table.containsKey(clazz))
			return;

		Preview preview = new Preview();
		preview.previews = previews;
		preview.interval = preview.timer = interval;
		table.put(clazz, preview);
	}

	public static void register(Class<? extends AbstractCard> clazz, AbstractCard[] previews) {
		register(clazz, previews, 1.0F);
	}

	public static boolean hasRegistered(Class<? extends AbstractCard> clazz) {
		return table.containsKey(clazz);
	}

	@SuppressWarnings("unused")
	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips", paramtypez = {SpriteBatch.class})
	public static class SCVPatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			Preview preview = table.get(___card.getClass());
			if (preview != null && preview.previews != null) {
				AbstractCard previewCard = preview.get();
//				previewCard.current_x = 1435.0F * Settings.scale;
//				previewCard.current_y = 795.0F * Settings.scale;
//				previewCard.drawScale = 0.8F;
//				previewCard.render(sb);

				AbstractCard original = ___card.cardsToPreview;
				___card.cardsToPreview = previewCard;
				___card.renderCardPreviewInSingleView(sb);
				___card.cardsToPreview = original;
			}
		}
	}
}
