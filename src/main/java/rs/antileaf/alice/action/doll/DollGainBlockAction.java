package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollGainBlockAction extends AbstractGameAction {
	private final AbstractDoll doll;
	private final int block;
	
	public DollGainBlockAction(AbstractDoll doll, int block) {
		this.doll = doll;
		this.block = block;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().addBlock(this.doll, this.block);
			else
				AliceSpireKit.log(this.getClass(),
						"DollGainBlockAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
			
			this.isDone = true;
		}
	}
}
