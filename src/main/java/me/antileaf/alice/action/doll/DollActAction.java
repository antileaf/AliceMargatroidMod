package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DollActAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(DollActAction.class.getName());

	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final AbstractDoll.DollActModifier modifier;
	
	public DollActAction(AbstractDoll doll, AbstractDoll.DollActModifier modifier) {
		this.doll = doll;
		this.modifier = modifier;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = this.startDuration = DURATION;
	}
	
	public DollActAction(AbstractDoll doll) {
		this(doll, new AbstractDoll.DollActModifier());
	}
	
	@Override
	public void update() {
		if (!DollManager.get().contains(this.doll)) {
			logger.warn("DollActAction.update(): DollManager does not contain {}!",
					this.doll.getClass().getSimpleName());
			this.isDone = true;
			return;
		}
		
		if (this.duration == this.startDuration) {
			if (DollManager.get().contains(this.doll)) {
				if (this.doll.skipActWaiting())
					this.duration = 0.0F;
			}
			else
				logger.warn("DollActAction.update() (skipped): There may be a bug in DollManager.");
		}
		
//		if (this.duration == DURATION) {
//			if (this.doll instanceof ShanghaiDoll && ((ShanghaiDoll) this.doll).charge > 0) {
//				ShanghaiDoll shanghai = (ShanghaiDoll) this.doll;
//
//				for (int i = 0; i < shanghai.charge; i++)
//					this.addToTop(new DollActAction(this.doll, false));
//
//				shanghai.charge = 0;
//
//				AliceSpireKit.logger.info("DollActAction triggered ShanghaiDoll charge for {} times.",
//						shanghai.charge);
//			}
//		}
		
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().dollAct(this.doll, this.modifier);
			else
				logger.warn("DollActAction.update() : There may be a bug in DollManager.");
		}
	}
}
