package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.patches.enums.ActionTypeEnum;
import me.antileaf.alice.powers.unique.DollAmbushPower;
import me.antileaf.alice.utils.AliceHelper;

public class RecycleDollAction extends AbstractGameAction {
	private final AbstractDoll doll;
	private final AbstractDoll newDoll;
	private final boolean isSpecial;
	
	public RecycleDollAction(AbstractDoll doll, AbstractDoll newDoll, boolean isSpecial) {
		this.doll = doll;
		this.newDoll = newDoll;
		this.isSpecial = isSpecial;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
	}
	
	public RecycleDollAction(AbstractDoll doll, AbstractDoll newDoll) {
		this(doll, newDoll, false);
	}
	
	public RecycleDollAction(AbstractDoll doll) {
		this(doll, null, false);
	}
	
	@Override
	public void update() {
		if (!this.isDone) {
			if (!this.isSpecial && AbstractDungeon.player.hasPower(DollAmbushPower.POWER_ID)) {
				int count = AbstractDungeon.player.getPower(DollAmbushPower.POWER_ID).amount;
				for (int i = 0; i < count; i++)
					AliceHelper.addActionToBuffer(new DollActAction(doll));
			}
			
			AliceHelper.addActionToBuffer(new RecycleDollInternalAction(doll, newDoll));
			AliceHelper.commitBuffer();
			
			this.isDone = true;
		}
	}
}
