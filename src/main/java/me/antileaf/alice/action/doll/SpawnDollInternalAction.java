package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;

public class SpawnDollInternalAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int index;
	
	public SpawnDollInternalAction(AbstractDoll doll, int index) {
		this.doll = doll;
		this.index = index;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = this.startDuration = DURATION;
	}
	
	@Override
	public void update() {
		this.tickDuration();
		
		if (this.isDone) {
			DollManager.get().spawnDollInternal(this.doll, this.index);
		}
	}
}
