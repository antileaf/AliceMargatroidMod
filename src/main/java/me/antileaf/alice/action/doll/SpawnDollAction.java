package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;

public class SpawnDollAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int index;
	private final AbstractGameAction followUpAction;
	
	public SpawnDollAction(AbstractDoll doll, int index, AbstractGameAction followUpAction) {
		this.doll = doll;
		this.index = index;
		this.followUpAction = followUpAction;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}

	public SpawnDollAction(AbstractDoll doll, int index) {
		this(doll, index, null);
	}
	
	@Override
	public void update() {
//		if (this.duration == DURATION) {
//			if (this.index != -1) {
//				AbstractDoll doll = DollManager.get().getDolls().get(index);
//				if (!(doll instanceof EmptyDollSlot)) {
//					AliceSpireKit.addActionsToTop(
//							new RecycleDollAction(doll),
//							new SpawnDollAction(this.doll, this.index)
//					);
//					this.isDone = true;
//					return;
//				}
//			}
//		}
		
		this.tickDuration();
		
		if (this.isDone) {
//			AliceSpireKit.log(this.getClass(), "this.isDone!");
			DollManager.get().spawnDoll(this.doll, this.index, this.followUpAction);
		}
	}
}
