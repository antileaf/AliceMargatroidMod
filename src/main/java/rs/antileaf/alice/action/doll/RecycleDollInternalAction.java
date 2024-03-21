package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class RecycleDollInternalAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final AbstractDoll newDoll;
	
	public RecycleDollInternalAction(AbstractDoll doll, AbstractDoll newDoll) {
		this.doll = doll;
		this.newDoll = newDoll;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == DURATION) {
			if (!DollManager.get().contains(this.doll)) {
				AliceSpireKit.log(this.getClass(),
						"RecycleDollAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
				
				this.addToTop(new SpawnDollAction(this.newDoll, -1));
				this.isDone = true;
				return;
			}
		}
		
		this.tickDuration();
			
		if (this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().recycleDoll(this.doll, this.newDoll);
			else
				AliceSpireKit.log("???");
		}
	}
}
