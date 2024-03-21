package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

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
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll)) {
				if (this.block == -1)
					this.block = this.doll.block;
				
				DollManager.get().loseBlock(this.doll, this.block);
			}
			else
				AliceSpireKit.log(this.getClass(),
						"DollGainBlockAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
		}
	}
}
