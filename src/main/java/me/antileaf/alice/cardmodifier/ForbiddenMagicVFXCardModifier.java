package me.antileaf.alice.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.powers.unique.ForbiddenMagicPower;
import me.antileaf.alice.strings.AliceCardModifierStrings;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;
import java.util.List;

// No actual functionality. Just for visual effects.
public class ForbiddenMagicVFXCardModifier extends AbstractCardModifier {
	private static final String SIMPLE_NAME = ForbiddenMagicVFXCardModifier.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final AliceCardModifierStrings cardModifierStrings = AliceCardModifierStrings.get(ID);

	public ForbiddenMagicVFXCardModifier() {
	}

	private int getActCount() {
		return AliceHelper.isInBattle() && AbstractDungeon.player.hasPower(ForbiddenMagicPower.POWER_ID) ?
				AbstractDungeon.player.getPower(ForbiddenMagicPower.POWER_ID).amount : -1;
	}
	
	@Override
	public List<TooltipInfo> additionalTooltips(AbstractCard card) {
		ArrayList<TooltipInfo> tips = new ArrayList<>();
		tips.add(new TooltipInfo(cardModifierStrings.NAME,
				String.format(cardModifierStrings.DESCRIPTION, this.getActCount())));
		return tips;
	}
	
	@Override
	public void onInitialApplication(AbstractCard card) {
//		PurgeField.purge.set(card, true);
	}
	
	@Override
	public AbstractCardModifier makeCopy() {
		return new ForbiddenMagicVFXCardModifier();
	}

	@Override
	public void onRender(AbstractCard card, SpriteBatch sb) {
		if (card instanceof AbstractAliceCard && AliceHelper.isInBattle())
			((AbstractAliceCard) card).drawOnCard(sb,
					ForbiddenMagicPower.ICON,
					card.current_x, card.current_y,
					new Vector2(134.0F, 200.0F),
					128.0F, 128.0F,
					1.0F, card.drawScale, 0.6F);
	}
	
//	@Override
//	public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
//		card.stopGlowing();
//	}

	// The logic of removal is implemented in patches.unique.ForbiddenMagicVFXRemovalPatch
//	@Override
//	public boolean removeAtEndOfTurn(AbstractCard card) {
//		return true;
//	}

	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}
	
	@Override
	public boolean shouldApply(AbstractCard card) {
		return !CardModifierManager.hasModifier(card, ID);
	}
}
