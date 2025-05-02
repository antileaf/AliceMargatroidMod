package me.antileaf.alice.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;
import me.antileaf.alice.utils.AliceHelper;

public class AliceAddCardToHandAction extends AbstractGameAction {
	private final AbstractCard card;

	public AliceAddCardToHandAction(AbstractCard card) {
		this.card = card;
		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public AliceAddCardToHandAction(AbstractCard card, boolean triggerMasterReality) {
		this(card);

		if (triggerMasterReality &&
				AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID) &&
				card.canUpgrade())
			card.upgrade();
	}

	public void update() {
		AliceHelper.addCardToHand(this.card);
		this.isDone = true;
	}
}
