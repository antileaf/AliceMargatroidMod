package rs.antileaf.alice.patches.card.signature;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SuppressWarnings("unused")
public class SignatureInterfacePatch {
	@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<TextureAtlas.AtlasRegion> signaturePortrait = new SpireField<>(() -> null);
		public static SpireField<Boolean> hasSignature = new SpireField<>(() -> false);

		public static SpireField<Boolean> signatureHovered = new SpireField<>(() -> false);
		public static SpireField<Float> signatureHoverTimer = new SpireField<>(() -> 0.0F);
		public static SpireField<Float> signatureHoverDuration = new SpireField<>(() -> 0.0F);
	}
}
