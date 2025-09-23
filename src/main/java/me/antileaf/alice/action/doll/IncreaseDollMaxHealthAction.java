package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IncreaseDollMaxHealthAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(IncreaseDollMaxHealthAction.class.getName());
	
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int amount;
	private final boolean isEmpty;

	public IncreaseDollMaxHealthAction(AbstractDoll doll, int amount, boolean isEmpty) {
		this.doll = doll;
		this.amount = amount;
		this.isEmpty = isEmpty;

		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = this.startDuration = DURATION;
	}

	public IncreaseDollMaxHealthAction(AbstractDoll doll, int amount) {
		this(doll, amount, false);
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().increaseMaxHealth(this.doll, this.amount, this.isEmpty);
			else
				logger.warn("IncreaseDollMaxHealthAction.update(): DollManager does not contain {}!",
						this.doll.getClass().getSimpleName());
		}
		
		this.tickDuration();
	}
}
