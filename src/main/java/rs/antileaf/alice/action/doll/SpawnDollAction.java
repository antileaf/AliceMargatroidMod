package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;

public class SpawnDollAction extends AbstractGameAction {
	private final AbstractDoll doll;
	private final int index;
	
	public SpawnDollAction(AbstractDoll doll, int index) {
		this.doll = doll;
		this.index = index;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			DollManager dm = DollManager.getInstance(AbstractDungeon.player);
			dm.spawnDoll(this.doll, this.index);
			
			this.isDone = true;
		}
	}
}
