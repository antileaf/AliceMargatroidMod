package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;

public class MoveDollAction extends AbstractGameAction {
	private final AbstractDoll doll;
	private final int index;
	
	public MoveDollAction(AbstractDoll doll, int index) {
		this.doll = doll;
		this.index = index;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().moveDoll(this.doll, this.index);
			else
				rs.antileaf.alice.utils.AliceSpireKit.log(this.getClass(),
						"MoveDollAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
			
			this.isDone = true;
		}
	}
}
