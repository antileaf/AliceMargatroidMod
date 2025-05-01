package rs.antileaf.alice.patches.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardTargetEnum {
	@SpireEnum
	public static AbstractCard.CardTarget DOLL, DOLL_OR_EMPTY_SLOT, DOLL_OR_EMPTY_SLOT_OR_NONE, DOLL_OR_NONE;

	@SpireEnum
	@Deprecated
	public static AbstractCard.CardTarget DOLL_OR_ENEMY, DOLL_OR_EMPTY_SLOT_OR_ENEMY;
	
	public static boolean isDollTarget(AbstractCard.CardTarget target) {
		return target == DOLL || target == DOLL_OR_EMPTY_SLOT || target == DOLL_OR_EMPTY_SLOT_OR_NONE ||
				target == DOLL_OR_ENEMY || target == DOLL_OR_EMPTY_SLOT_OR_ENEMY || target == DOLL_OR_NONE;
	}
}
