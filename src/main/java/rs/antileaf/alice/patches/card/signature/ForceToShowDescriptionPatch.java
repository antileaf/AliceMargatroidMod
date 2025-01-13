package rs.antileaf.alice.patches.card.signature;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rs.antileaf.alice.cards.AbstractAliceCard;

@SuppressWarnings("unused")
public class ForceToShowDescriptionPatch {
	private static final Logger logger = LogManager.getLogger(ForceToShowDescriptionPatch.class.getName());

	public static class GridSelectionScreenPatches {
//		@SpirePatch(clz = GridCardSelectScreen.class, method = SpirePatch.CLASS)
//		public static class Fields {
//			public static SpireField<ArrayList<AbstractAliceCard>> last = new SpireField<>(ArrayList::new);
//		}

		@SpirePatch(clz = GridCardSelectScreen.class, method = "update", paramtypez = {})
		public static class UpdatePatch {
			@SpirePostfixPatch
			public static void Postfix(GridCardSelectScreen _inst, AbstractCard ___hoveredCard) {
				if (_inst.confirmScreenUp) {
//					ArrayList<AbstractAliceCard> last = Fields.last.get(_inst);
//
//					if (!last.isEmpty()) {
//						last.forEach(c -> c.forcedToShowDescription = false);
//						last.clear();
//					}

					if (___hoveredCard instanceof AbstractAliceCard) {
						((AbstractAliceCard) ___hoveredCard).forceToShowDescription();
//						last.add((AbstractAliceCard) ___hoveredCard);
					}

					if (_inst.upgradePreviewCard instanceof AbstractAliceCard) {
						((AbstractAliceCard) _inst.upgradePreviewCard).forceToShowDescription();
//						last.add((AbstractAliceCard) _inst.upgradePreviewCard);
					}
				}
//				else {
//					ArrayList<AbstractAliceCard> last = Fields.last.get(_inst);
//
//					if (!last.isEmpty()) {
//						last.forEach(c -> c.forcedToShowDescription = false);
//						last.clear();
//					}
//				}
			}
		}
	}

	public static class HandSelectScreenPatches {
		@SpirePatch(clz = HandCardSelectScreen.class, method = SpirePatch.CLASS)
//		public static class Fields {
//			public static SpireField<ArrayList<AbstractAliceCard>> last = new SpireField<>(ArrayList::new);
//		}

		@SpirePatch(clz = HandCardSelectScreen.class, method = "update", paramtypez = {})
		public static class SelectHoveredCardPatch {
			@SpirePostfixPatch
			public static void Postfix(HandCardSelectScreen _inst) {
//				ArrayList<AbstractAliceCard> last = Fields.last.get(_inst);
//
//				if (!last.isEmpty()) {
//					last.forEach(c -> c.forcedToShowDescription = false);
//					last.clear();
//				}

				_inst.selectedCards.group.forEach(c -> {
					if (c instanceof AbstractAliceCard) {
						((AbstractAliceCard) c).forceToShowDescription();
//						last.add((AbstractAliceCard) c);
					}
				});

				if (_inst.upgradePreviewCard instanceof AbstractAliceCard) {
					((AbstractAliceCard) _inst.upgradePreviewCard).forceToShowDescription();
//					last.add((AbstractAliceCard) _inst.upgradePreviewCard);
				}
			}
		}
	}

	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class})
	public static class ShowCardBrieflyEffectPatch1 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard card) {
			if (card instanceof AbstractAliceCard)
				((AbstractAliceCard) card).forceToShowDescription();
		}
	}

	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, float.class, float.class})
	public static class ShowCardBrieflyEffectPatch2 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard card, float x, float y) {
			if (card instanceof AbstractAliceCard)
				((AbstractAliceCard) card).forceToShowDescription();
		}
	}

	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = "update", paramtypez = {})
	public static class ShowCardBrieflyEffectPatch3 {
		@SpirePrefixPatch
		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard ___card) {
			if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F && ___card instanceof AbstractAliceCard)
				((AbstractAliceCard) ___card).forceToShowDescription();
		}
	}

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
	public static class ShowCardAndObtainEffectPatch1 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndObtainEffect _inst, AbstractCard card,
								   float x, float y, boolean convergeCards) {
			if (card instanceof AbstractAliceCard) {
				((AbstractAliceCard) card).forceToShowDescription();

//				logger.info("ShowCardAndObtainEffectPatch1: {}", card.cardID);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update", paramtypez = {})
	public static class ShowCardAndObtainEffectPatch2 {
		@SpirePrefixPatch
		public static void Prefix(ShowCardAndObtainEffect _inst, AbstractCard ___card) {
			if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F &&
					___card instanceof AbstractAliceCard) {
				((AbstractAliceCard) ___card).forceToShowDescription();

//				logger.info("ShowCardAndObtainEffectPatch2: {}", ___card.cardID);
			}
		}
	}
}
