package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import me.antileaf.alice.utils.AliceHelper;

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
			AliceHelper.log(this.getClass(),
					"DollActAction.update(): DollManager does not contain " +
							this.doll.getClass().getSimpleName() + "!");
			this.isDone = true;
			return;
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
				DollManager.get().dollAct(this.doll, this.isSpecial);
			else
				AliceHelper.log("DollActAction.update() : There may be a bug in DollManager.");
		}
	}
}
