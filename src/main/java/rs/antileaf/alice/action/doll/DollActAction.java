package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollActAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final boolean isSpecial;
	
	public DollActAction(AbstractDoll doll, boolean isSpecial) {
		this.doll = doll;
		this.isSpecial = isSpecial;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	public DollActAction(AbstractDoll doll) {
		this(doll, false);
	}
	
	@Override
	public void update() {
		if (!DollManager.get().contains(this.doll)) {
			AliceSpireKit.log(this.getClass(),
					"DollActAction.update(): DollManager does not contain " +
							this.doll.getClass().getSimpleName() + "!");
			this.isDone = true;
			return;
		}
		
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().dollAct(this.doll, this.isSpecial);
			else
				AliceSpireKit.log("DollActAction.update() : There may be a bug in DollManager.");
		}
	}
}
