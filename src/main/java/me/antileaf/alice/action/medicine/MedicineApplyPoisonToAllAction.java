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
import me.antileaf.alice.utils.AliceHelper;

public class MedicineApplyPoisonToAllAction extends AbstractGameAction {
	public MedicineApplyPoisonToAllAction(AbstractCreature source, int amount) {
		this.setValues(AbstractDungeon.player, source, amount);
		this.actionType = ActionType.DEBUFF;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (doll != null && !(doll instanceof EmptyDollSlot))
					AliceHelper.addActionToBuffer(new DollApplyPoisonAction(doll, this.amount));
			
			AliceHelper.addActionToBuffer(new ApplyPowerAction(this.target, this.source,
					new MedicinePoisonPower(this.target, this.source, this.amount)));
			
			AliceHelper.commitBuffer();
			
			this.isDone = true;
		}
	}
}
