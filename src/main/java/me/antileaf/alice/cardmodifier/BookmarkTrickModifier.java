package me.antileaf.alice.cardmodifier;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.alice.strings.AliceCardModifierStrings;
import me.antileaf.alice.utils.AliceHelper;

// No actual influence on cards. Just for marking purposes.
public class BookmarkTrickModifier extends AbstractCardModifier {
	private static final String SIMPLE_NAME = BookmarkTrickModifier.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final AliceCardModifierStrings cardModifierStrings = AliceCardModifierStrings.get(ID);
	
	public final int order;
	
	public BookmarkTrickModifier(int order) {
		this.order = order;
	}
	
	@Override
	public boolean isInherent(AbstractCard card) {
		return true;
	}
	
	@Override
	public AbstractCardModifier makeCopy() {
		return new BookmarkTrickModifier(33550336);
	}
	
	@Override
	public String identifier(AbstractCard card) {
		return ID;
	}
	
	@Override
	public boolean shouldApply(AbstractCard card) {
		return !CardModifierManager.hasModifier(card, ID);
	}
}
