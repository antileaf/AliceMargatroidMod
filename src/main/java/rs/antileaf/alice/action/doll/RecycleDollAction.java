package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.powers.unique.DollAmbushPower;
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
			if (AbstractDungeon.player.hasPower(DollAmbushPower.POWER_ID)) {
				int count = AbstractDungeon.player.getPower(DollAmbushPower.POWER_ID).amount;
				for (int i = 0; i < count; i++)
					AliceSpireKit.addActionToBuffer(new DollActAction(doll));
			}
			
			AliceSpireKit.addActionToBuffer(new RecycleDollInternalAction(doll));
			AliceSpireKit.commitBuffer();
			
			this.isDone = true;
		}
	}
}
