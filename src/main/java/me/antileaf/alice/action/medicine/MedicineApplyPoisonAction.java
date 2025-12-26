package me.antileaf.alice.action.medicine;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.alice.action.doll.DollApplyPoisonAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.medicine.MedicinePoisonPower;

public class MedicineApplyPoisonAction extends AbstractGameAction {
	private final int targetIndex;
	
	public MedicineApplyPoisonAction(int targetIndex, AbstractCreature source, int amount) {
		this.setValues(AbstractDungeon.player, source, amount);
		this.targetIndex = targetIndex;
		this.actionType = ActionType.DEBUFF;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
//			boolean applied = false;
			
			AbstractDoll doll = DollManager.get().getDolls().get(this.targetIndex);
			if (doll != null && !(doll instanceof EmptyDollSlot)) {
				this.addToTop(new DollApplyPoisonAction(doll, this.amount));
			}
			else {
				this.addToTop(new ApplyPowerAction(this.target, this.source,
						new MedicinePoisonPower(this.target, this.source, this.amount)));
			}
			
			this.isDone = true;
		}
	}
}
