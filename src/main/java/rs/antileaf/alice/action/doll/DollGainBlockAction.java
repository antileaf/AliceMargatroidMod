package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollActAction extends AbstractGameAction {
	private final AbstractDoll doll;
	
	public DollActAction(AbstractDoll doll) {
		this.doll = doll;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			if (DollManager.get().contains(this.doll))
				DollManager.get().dollAct(this.doll);
			else
				AliceSpireKit.log(this.getClass(),
						"DollActAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
			
			this.isDone = true;
		}
	}
}
