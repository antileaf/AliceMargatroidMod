package rs.antileaf.alice.patches.card.signature;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreviewRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.utils.SignatureHelper;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class SCVRenderCardPatch {
	private static void renderHelper(SingleCardViewPopup scv, SpriteBatch sb,
									 float x, float y, TextureAtlas.AtlasRegion img) {
		ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderHelper",
				SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class)
				.invoke(scv, sb, x, y, img);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<TextureAtlas.AtlasRegion> signature = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "loadPortraitImg", paramtypez = {})
	public static class LoadPortraitImgPatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, AbstractCard ___card) {
			if (___card instanceof AbstractAliceCard && ((AbstractAliceCard) ___card).hasSignature) {
				String sig = ((AbstractAliceCard) ___card).getSignaturePortraitImgPath();
				Fields.signature.set(_inst, SignatureHelper.load(sig));
			}
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "close", paramtypez = {})
	public static class ClosePatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst) {
			if (Fields.signature.get(_inst) != null) {
//				Fields.signature.get(_inst).getTexture().dispose();
				Fields.signature.set(_inst, null);
			}
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderPortrait", paramtypez = {SpriteBatch.class})
	public static class RenderPortraitPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (___card.isLocked)
				return SpireReturn.Continue();

			if (___card instanceof AbstractAliceCard && SignatureHelper.shouldUseSignature(___card.cardID)) {
				renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
						Fields.signature.get(_inst));

				if (!SCVPanelPatch.Fields.hideDesc.get(_inst))
					renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
							___card.description.size() >= 4 ?
									SignatureHelper.DESC_SHADOW_P : SignatureHelper.DESC_SHADOW_SMALL_P);

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame", paramtypez = {SpriteBatch.class})
	public static class RenderFramePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (___card instanceof AbstractAliceCard && SignatureHelper.shouldUseSignature(___card.cardID)) {
				TextureAtlas.AtlasRegion frame;

				if (___card.type == AbstractCard.CardType.ATTACK) {
					if (___card.rarity == AbstractCard.CardRarity.RARE)
						frame = SignatureHelper.CARD_TYPE_ATTACK_RARE_P;
					else if (___card.rarity == AbstractCard.CardRarity.UNCOMMON)
						frame = SignatureHelper.CARD_TYPE_ATTACK_UNCOMMON_P;
					else
						frame = SignatureHelper.CARD_TYPE_ATTACK_COMMON_P;
				}
				else if (___card.type == AbstractCard.CardType.POWER) {
					if (___card.rarity == AbstractCard.CardRarity.RARE)
						frame = SignatureHelper.CARD_TYPE_POWER_RARE_P;
					else if (___card.rarity == AbstractCard.CardRarity.UNCOMMON)
						frame = SignatureHelper.CARD_TYPE_POWER_UNCOMMON_P;
					else
						frame = SignatureHelper.CARD_TYPE_POWER_COMMON_P;
				}
				else {
					if (___card.rarity == AbstractCard.CardRarity.RARE)
						frame = SignatureHelper.CARD_TYPE_SKILL_RARE_P;
					else if (___card.rarity == AbstractCard.CardRarity.UNCOMMON)
						frame = SignatureHelper.CARD_TYPE_SKILL_UNCOMMON_P;
					else
						frame = SignatureHelper.CARD_TYPE_SKILL_COMMON_P;
				}

				renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, frame);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner", paramtypez = {SpriteBatch.class})
	public static class RenderCardBannerPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (___card instanceof AbstractAliceCard && SignatureHelper.shouldUseSignature(___card.cardID))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText", paramtypez = {SpriteBatch.class})
	public static class RenderCardTypeTextPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"label"})
		public static SpireReturn<Void> Insert(SingleCardViewPopup _inst, SpriteBatch sb,
											   AbstractCard ___card, String label) {
			if (___card instanceof AbstractAliceCard && SignatureHelper.shouldUseSignature(___card.cardID)) {
				Color cardTypeColor = ReflectionHacks.getPrivateStatic(SingleCardViewPopup.class,
						"CARD_TYPE_COLOR");

				FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, label,
						(float)Settings.WIDTH / 2.0F,
						(float)Settings.HEIGHT / 2.0F - 392.0F * Settings.scale,
						cardTypeColor);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription", paramtypez = {SpriteBatch.class})
	public static class RenderDescriptionPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (___card instanceof AbstractAliceCard &&
					SignatureHelper.shouldUseSignature(___card.cardID) &&
					SCVPanelPatch.Fields.hideDesc.get(_inst))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN", paramtypez = {SpriteBatch.class})
	public static class RenderDescriptionCNPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (___card instanceof AbstractAliceCard &&
					SignatureHelper.shouldUseSignature(___card.cardID) &&
					SCVPanelPatch.Fields.hideDesc.get(_inst))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = MultiCardPreviewRenderer.RenderMultiCardPreviewInSingleViewPatch.class,
			method = "Postfix", paramtypez = {SingleCardViewPopup.class, SpriteBatch.class})
	public static class AvoidOverlappingWithPanelPatch {
		@SpireInsertPatch(rloc = 25, localvars = {"horizontalOnly", "verticalNext", "position", "offset", "toPreview"})
		public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card,
								  boolean horizontalOnly, @ByRef boolean[] verticalNext,
								  Vector2 position, Vector2 offset, AbstractCard toPreview) {
			if (___card instanceof AbstractAliceCard && SignatureHelper.isUnlocked(___card.cardID) &&
					!((AbstractAliceCard) ___card).dontAvoidSCVPanel) {
				ArrayList<AbstractCard> previews = MultiCardPreview.multiCardPreview.get(___card);

				if (previews.size() > 1 && toPreview == previews.get(1)) {
					ReflectionHacks.privateMethod(MultiCardPreviewRenderer.class, "reposition",
							Vector2.class, Vector2.class, boolean.class)
							.invoke(null, position, offset, horizontalOnly || !verticalNext[0]);

					verticalNext[0] = !verticalNext[0];
				}
			}
		}
	}
}
