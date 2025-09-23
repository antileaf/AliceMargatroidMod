package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DollGainBlockAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(DollGainBlockAction.class.getName());
	
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int block;
	
	public DollGainBlockAction(AbstractDoll doll, int block) {
		this.doll = doll;
		this.block = block;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = this.startDuration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().addBlock(this.doll, this.block);
			else
				logger.warn("update(): DollManager does not contain {}!",
						this.doll.getClass().getSimpleName());
		}
		
		this.tickDuration();
	}
}
