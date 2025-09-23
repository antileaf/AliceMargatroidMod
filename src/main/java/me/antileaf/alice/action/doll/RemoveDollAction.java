package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemoveDollAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(RemoveDollAction.class.getName());
	
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	
	public RemoveDollAction(AbstractDoll doll) {
		this.doll = doll;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = this.startDuration = DURATION;
	}
	
	@Override
	public void update() {
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().removeDoll(this.doll);
			else
				logger.warn("update(): DollManager does not contain {}!",
						this.doll.getClass().getSimpleName());
		}
	}
}
