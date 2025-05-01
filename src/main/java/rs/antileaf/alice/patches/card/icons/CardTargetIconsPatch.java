package rs.antileaf.alice.patches.card.icons;

import basemod.ReflectionHacks;
import basemod.patches.whatmod.CardView;
import basemod.patches.whatmod.WhatMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.signature.patches.scv.SCVPanelPatch;
import me.antileaf.signature.utils.SignatureHelper;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.targeting.AliceTargetIcon;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceImageMaster;
import rs.antileaf.alice.utils.AliceTargetIconTipHelper;

import static rs.antileaf.alice.AliceMargatroidMod.ALICE_PUPPETEER_FLAVOR;

@SuppressWarnings("unused")
public class CardTargetIconsPatch {
	private static final float BODY_TEXT_WIDTH = 280.0F * Settings.scale;
	private static final float TIP_DESC_LINE_SPACING = 26.0F * Settings.scale;
	private static final float SHADOW_DIST_X = 9.0F * Settings.scale;
	private static final float SHADOW_DIST_Y = 14.0F * Settings.scale;
	private static final float BOX_W = 320.0F * Settings.scale;
	private static final float BOX_EDGE_H = 32.0F * Settings.scale;
	private static final float BOX_BODY_H = 64.0F * Settings.scale;
	private static final float TEXT_OFFSET_X = 22.0F * Settings.scale;

	private static final BitmapFont BODY_FONT = FlavorText.prepFont(22.0F, false);

	private static float fixSmartHeight(String text, AbstractAliceCard card) {
		float base = FontHelper.getSmartHeight(BODY_FONT, text,
				BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING);

		int count = 2 ; // Math.min(card.targetIcons.size(), 2);

		if (Settings.lineBreakViaCharacter)
			base += BODY_FONT.getLineHeight() * count;
		else
			base += TIP_DESC_LINE_SPACING * count + BODY_FONT.getCapHeight();

		return base;
	}

	private static float getAliceSmartHeight(AbstractAliceCard card) {
		String tip = AliceTargetIconTipHelper.generateTip(card.targetIcons);
		return fixSmartHeight(tip, card) - BOX_EDGE_H * 3.15F;
	}

	private static void renderTargetTipBox(float x, float y, SpriteBatch sb, String text, AbstractAliceCard card) {
		float h = -fixSmartHeight(text, card);

		Color backup = sb.getColor();

		sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
		sb.draw(ImageMaster.KEYWORD_TOP, x + SHADOW_DIST_X, y - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);
		sb.draw(ImageMaster.KEYWORD_BODY, x + SHADOW_DIST_X, y - h - BOX_EDGE_H - SHADOW_DIST_Y, BOX_W, h + BOX_EDGE_H);
		sb.draw(ImageMaster.KEYWORD_BOT, x + SHADOW_DIST_X, y - h - BOX_BODY_H - SHADOW_DIST_Y, BOX_W, BOX_EDGE_H);

		sb.setColor(ALICE_PUPPETEER_FLAVOR);
		sb.draw(AliceImageMaster.TIP_TOP, x, y, BOX_W, BOX_EDGE_H);
		sb.draw(AliceImageMaster.TIP_MID, x, y - h - BOX_EDGE_H, BOX_W, h + BOX_EDGE_H);
		sb.draw(AliceImageMaster.TIP_BOT, x, y - h - BOX_BODY_H, BOX_W, BOX_EDGE_H);

		FontHelper.renderSmartText(sb, BODY_FONT, text,
				x + TEXT_OFFSET_X, y + 13.0F * Settings.scale,
				BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING, Color.BLACK);
	}

	public static class CardPatches {
		private static final float CARD_TIP_PAD = 12.0F * Settings.scale;
		private static final float BOX_W = 320.0F * Settings.scale;
		private static final float BOX_EDGE_H = 32.0F * Settings.scale;

		@SpirePatch(clz = AbstractCard.class, method = "renderInLibrary", paramtypez = {SpriteBatch.class})
		public static class RenderInLibraryPatch {
			private static class Locator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
					int[] tmp = LineFinder.findAllInOrder(ctBehavior,
							new Matcher.FieldAccessMatcher(Settings.class, "lineBreakViaCharacter"));
					return new int[]{tmp[0]};
				}
			}

			@SpireInsertPatch(locator = Locator.class)
			public static void Insert(AbstractCard _inst, SpriteBatch sb) {
				if (_inst instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons()) {
					AbstractAliceCard card = (AbstractAliceCard) _inst;

					card.renderTargetIcons(sb);
//				AliceTargetIconTipHelper.render(sb, card);
				}
			}
		}

		@SpirePatch(clz = AbstractCard.class, method = "renderCard",
				paramtypez = {SpriteBatch.class, boolean.class, boolean.class})
		public static class RenderCardPatch {
			private static class Locator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
					int[] tmp = LineFinder.findAllInOrder(ctBehavior,
							new Matcher.FieldAccessMatcher(Settings.class, "lineBreakViaCharacter"));
					return new int[]{tmp[0]};
				}
			}

			@SpireInsertPatch(locator = Locator.class)
			public static void Insert(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
				if (_inst instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons()) {
					AbstractAliceCard card = (AbstractAliceCard) _inst;

					card.renderTargetIcons(sb);
//				AliceTargetIconTipHelper.render(sb, card);
				}
			}
		}

		@SpirePatch(clz = TipHelper.class, method = "render", paramtypez = {SpriteBatch.class})
		public static class RenderTipPatch {
			private static class FirstLocator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
					int[] tmp = LineFinder.findAllInOrder(ctBehavior,
							new Matcher.MethodCallMatcher(TipHelper.class, "renderKeywords"));
					return new int[]{tmp[0]};
				}
			}

			@SpireInsertPatch(locator = FirstLocator.class)
			public static void FirstInsert(SpriteBatch sb) {
				AbstractCard card = ReflectionHacks.getPrivateStatic(TipHelper.class, "card");
				if (card instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons()) {
					AbstractAliceCard aliceCard = (AbstractAliceCard) card;
					if (aliceCard.targetIcons.isEmpty())
						return;

					String tip = AliceTargetIconTipHelper.generateTip(aliceCard.targetIcons);
					renderTargetTipBox(
							card.current_x - AbstractCard.IMG_WIDTH / 2.0F - CARD_TIP_PAD - BOX_W - 324.0F * Settings.scale,
							card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H,
							sb,
							tip,
							aliceCard
					);
				}
			}

			private static class SecondLocator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
					int[] tmp = LineFinder.findAllInOrder(ctBehavior,
							new Matcher.MethodCallMatcher(TipHelper.class, "renderKeywords"));
					return new int[]{tmp[tmp.length - 1]};
				}
			}

			@SpireInsertPatch(locator = SecondLocator.class)
			public static void SecondInsert(SpriteBatch sb) {
				AbstractCard card = ReflectionHacks.getPrivateStatic(TipHelper.class, "card");
				if (card instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons()) {
					AbstractAliceCard aliceCard = (AbstractAliceCard) card;
					if (aliceCard.targetIcons.isEmpty())
						return;

					String tip = AliceTargetIconTipHelper.generateTip(aliceCard.targetIcons);

					if (card.current_x > (float) Settings.WIDTH * 0.25F)
						renderTargetTipBox(
								card.current_x - AbstractCard.IMG_WIDTH / 2.0F - CARD_TIP_PAD - BOX_W,
								card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H,
								sb,
								tip,
								aliceCard
						);
					else
						renderTargetTipBox(
								card.current_x + AbstractCard.IMG_WIDTH / 2.0F + CARD_TIP_PAD + 324.0F * Settings.scale,
								card.current_y + AbstractCard.IMG_HEIGHT / 2.0F - BOX_EDGE_H,
								sb,
								tip,
								aliceCard
						);
				}
			}
		}
	}

	public static class SCVPatches {
		public static final float DRAW_SCALE = 1.35F;

		@SpirePatch(clz = SingleCardViewPopup.class, method = "render", paramtypez = {SpriteBatch.class})
		public static class SCVRenderIconPatch {

			private static class Locator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
					int[] tmp = LineFinder.findAllInOrder(ctBehavior,
							new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "renderTitle"));
					return new int[]{tmp[0]};
				}
			}

			@SpireInsertPatch(locator = Locator.class)
			public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
				if (___card instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons() &&
						(!SignatureHelper.shouldUseSignature(___card) ||
						!SCVPanelPatch.Fields.hideDesc.get(_inst)))
					((AbstractAliceCard) ___card).renderTargetIcons(
							sb,
							Settings.WIDTH / 2.0F,
							Settings.HEIGHT / 2.0F,
							200.0F,
							140.0F,
							AliceTargetIcon.WIDTH * DRAW_SCALE,
							AliceTargetIcon.WIDTH * DRAW_SCALE,
							1.0F,
							DRAW_SCALE
					);
			}
		}

		private static float offsetHeight = 0.0F;

		@SpirePatch(clz = CardView.class, method = "Postfix",
				paramtypez = {SingleCardViewPopup.class, SpriteBatch.class})
		public static class BeforeWhatModPatch {
			@SpirePrefixPatch
			public static void Prefix(SingleCardViewPopup scv, SpriteBatch sb) {
				AbstractCard card = ReflectionHacks.getPrivate(scv, SingleCardViewPopup.class, "card");
				if (card instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons()) {
					AbstractAliceCard aliceCard = (AbstractAliceCard) card;
					if (!aliceCard.targetIcons.isEmpty())
						offsetHeight = getAliceSmartHeight(aliceCard);
				}
			}
		}
		@SpirePatch(clz = WhatMod.class, method = "renderModTooltipBottomLeft",
				paramtypez = {SpriteBatch.class, float.class, float.class, Class[].class})
		public static class AdjustWhatModPositionPatch {
			private static class Locator extends SpireInsertLocator {
				@Override
				public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
					int[] tmp = LineFinder.findAllInOrder(ctBehavior,
							new Matcher.MethodCallMatcher(WhatMod.class, "renderModTooltip"));
					return new int[]{tmp[0]};
				}
			}

			@SpireInsertPatch(locator = Locator.class, localvars = {"height"})
			public static void Insert(SpriteBatch sb, float x, float y, Class<?>[] modClasses, @ByRef float[] height) {
				if (offsetHeight != 0.0F) {
					height[0] -= offsetHeight;
					offsetHeight = 0.0F;
				}
			}
		}

		private static void renderTip(SpriteBatch sb, AbstractCard card) {
			if (card.isLocked || !card.isSeen)
				return;

			if (card instanceof AbstractAliceCard && AliceConfigHelper.enableCardTargetIcons()) {
				AbstractAliceCard aliceCard = (AbstractAliceCard) card;

				if (aliceCard.targetIcons.isEmpty())
					return;

				String tip = AliceTargetIconTipHelper.generateTip(aliceCard.targetIcons);

				float x = (float) Settings.WIDTH / 2.0F + 340.0F * Settings.scale;
				float y = (float) Settings.HEIGHT / 2.0F + 160.0F * Settings.yScale;

				float height = fixSmartHeight(tip, aliceCard);
				y -= height - 7.0F * Settings.scale;

				renderTargetTipBox(x, y, sb, tip, aliceCard);
			}
		}

		@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips", paramtypez = {SpriteBatch.class})
		public static class SCVRenderTipPatch {
			@SpirePostfixPatch
			public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
				renderTip(sb, ___card);
			}
		}
	}
}
