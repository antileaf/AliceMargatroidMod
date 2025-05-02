package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import me.antileaf.alice.utils.AliceHelper;

public class DollGainBlockAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int block;
	
	public DollGainBlockAction(AbstractDoll doll, int block) {
		this.doll = doll;
		this.block = block;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == DURATION) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().addBlock(this.doll, this.block);
			else
				AliceHelper.log(this.getClass(),
						"DollGainBlockAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
		}
		
		this.tickDuration();
	}
}
