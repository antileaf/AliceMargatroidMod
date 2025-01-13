package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceHelper;

public class IncreaseDollMaxHealthAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int amount;

	public IncreaseDollMaxHealthAction(AbstractDoll doll, int amount) {
		this.doll = doll;
		this.amount = amount;
		
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == DURATION) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().increaseMaxHealth(this.doll, this.amount);
			else
				AliceHelper.logger.info("IncreaseDollMaxHealthAction.update(): DollManager does not contain {}!",
						this.doll.getClass().getSimpleName());
		}
		
		this.tickDuration();
	}
}
