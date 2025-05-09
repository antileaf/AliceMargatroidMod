package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import me.antileaf.alice.utils.AliceHelper;

public class DollLoseBlockAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private int block;
	
	public DollLoseBlockAction(AbstractDoll doll, int block) {
		this.doll = doll;
		this.block = block;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == DURATION) {
			if (!DollManager.get().contains(this.doll)) {
				AliceHelper.log(this.getClass(),
						"DollLoseBlockAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
				this.isDone = true;
				return;
			}
			
			if (this.doll.block == 0) {
				this.isDone = true;
				return;
			}
		}
		
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll)) {
				if (this.block == -1)
					this.block = this.doll.block;
				
				DollManager.get().loseBlock(this.doll, this.block);
			}
			else
				AliceHelper.log(this.getClass(),
						"DollGainBlockAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
		}
	}
}
