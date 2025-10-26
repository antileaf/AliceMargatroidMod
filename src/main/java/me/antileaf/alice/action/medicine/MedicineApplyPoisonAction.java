package me.antileaf.alice.action.medicine;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.doll.DollApplyPoisonAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.medicine.MedicinePoisonPower;

public class MedicineApplyPoisonAction extends AbstractGameAction {
	public MedicineApplyPoisonAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);
		this.actionType = ActionType.DEBUFF;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			boolean applied = false;
			
			if (this.target.isPlayer && this.source instanceof AbstractMonster) {
				int index = DollManager.get().damageTarget.getOrDefault((AbstractMonster) this.source, -1);
				if (index != -1) {
					AbstractDoll doll = DollManager.get().getDolls().get(index);
					if (doll != null && !(doll instanceof EmptyDollSlot)) {
						this.addToTop(new DollApplyPoisonAction(doll, this.amount));
						applied = true;
					}
				}
			}
			
			if (!applied)
				this.addToTop(new ApplyPowerAction(this.target, this.source,
						new MedicinePoisonPower(this.target, this.source, this.amount)));
			
			this.isDone = true;
		}
	}
}
