package rs.antileaf.alice.patches.marisa;

import ThMod.cards.Marisa.DoubleSpark;
import ThMod.cards.derivations.Spark;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.action.utils.AnonymousAction;
import rs.antileaf.alice.cards.Marisa.AliceSpark;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;

public class MarisaAlternativeImagePatch {
	@SpirePatch(clz = DoubleSpark.class, method = "use", requiredModId = "TS05_Marisa",
			paramtypez = {AbstractPlayer.class, AbstractMonster.class})
	public static class AliceSparkAlternativeImagePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.ConstructorCallMatcher(MakeTempCardInHandAction.class));
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom"));
				return new int[]{tmp[tmp.length - 1]};
			}
		}
		
		@SpireInsertPatch(locator = Locator.class, localvars = {"c"})
		public static SpireReturn<Void> Insert(DoubleSpark _inst, AbstractPlayer p, AbstractMonster m, AbstractCard c) {
			if ((c instanceof Spark) && _inst.textureImg.contains(AliceMargatroidMod.SIMPLE_NAME)) {
				if (AliceConfigHelper.enableAlternativeMarisaCardImage()) {
					((CustomCard) c).textureImg = AliceSpark.IMG;
					((CustomCard) c).loadCardImage(((CustomCard) c).textureImg);
				}
				
				AliceSpireKit.addToBot(new AnonymousAction(() -> {
					AliceSpireKit.addCardToHand(c);
				}));
				
				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
}
