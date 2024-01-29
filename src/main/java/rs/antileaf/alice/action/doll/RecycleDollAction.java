package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.utils.AliceSpireKit;

public class RecycleDollAction extends AbstractGameAction {
	private final AbstractDoll doll;
	
	public RecycleDollAction(AbstractDoll doll) {
		this.doll = doll;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			DollManager dm = DollManager.getInstance(AbstractDungeon.player);
			if (dm.contains(this.doll))
				dm.recycleDoll(this.doll);
			else
				AliceSpireKit.log(this.getClass(),
						"RecycleDollAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
			
			this.isDone = true;
		}
	}
}
