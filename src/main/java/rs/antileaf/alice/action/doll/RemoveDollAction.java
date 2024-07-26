package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class RemoveDollAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	
	public RemoveDollAction(AbstractDoll doll) {
		this.doll = doll;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	@Override
	public void update() {
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().removeDoll(this.doll);
			else
				AliceSpireKit.log(this.getClass(),
						"RemoveDollAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
		}
	}
}
