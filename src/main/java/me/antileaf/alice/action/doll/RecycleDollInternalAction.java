package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecycleDollInternalAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(RecycleDollInternalAction.class.getName());

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
				logger.warn("RecycleDollInternalAction.update(): DollManager does not contain {}!",
						this.doll.name);
				
//				if (this.newDoll != null)
//					this.addToTop(new SpawnDollAction(this.newDoll, -1));

				this.isDone = true;
				return;
			}
		}
		
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().recycleDoll(this.doll, this.newDoll);
			else
				logger.warn("???");
		}
	}
}
