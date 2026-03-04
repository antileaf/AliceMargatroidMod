package me.antileaf.alice.action.medicine;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.monsters.medicine.MedicineCardItem;
import me.antileaf.alice.monsters.medicine.MedicineMelancholy;

public class MedicinePlayCardAction extends AbstractGameAction {
	private final MedicineCardItem item;
	private MedicineMelancholy medicine;
	
	public MedicinePlayCardAction(MedicineCardItem item, MedicineMelancholy medicine) {
		this.item = item;
		this.medicine = medicine;
		
		this.actionType = ActionType.USE;
		this.duration = this.startDuration = 0.2F;
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			this.medicine.cardList.play(this.item, this.medicine);
		}
		
		this.tickDuration();
	}
}
