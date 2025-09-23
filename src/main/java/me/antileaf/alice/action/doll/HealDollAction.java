package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HealDollAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(HealDollAction.class.getName());
	
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int heal;
	
	public HealDollAction(AbstractDoll doll, int heal) {
		this.doll = doll;
		this.heal = heal;
		
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = this.startDuration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().heal(this.doll, this.heal);
			else
				logger.warn("HealDollAction.update(): DollManager does not contain {}!",
						this.doll.getClass().getSimpleName());
		}
		
		this.tickDuration();
	}
}
