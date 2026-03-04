package me.antileaf.alice.action.medicine;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.monsters.medicine.MedicineCardItem;
import me.antileaf.alice.monsters.medicine.MedicineCardList;

public class MedicineCardFadeOutAction extends AbstractGameAction {
	private final MedicineCardList cardList;
	private final MedicineCardItem item;
	
	public MedicineCardFadeOutAction(MedicineCardList cardList, MedicineCardItem item) {
		this.cardList = cardList;
		this.item = item;
		
		this.actionType = ActionType.WAIT;
		this.duration = this.startDuration = 0.5F;
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			this.item.card.fadingOut = true;
		}
		
		this.tickDuration();
		
		if (this.isDone) {
			this.cardList.cardInUse = null;
			this.item.dispose();
		}
	}
}
