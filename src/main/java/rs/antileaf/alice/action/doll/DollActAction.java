package rs.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.patches.enums.ActionTypeEnum;
import rs.antileaf.alice.powers.unique.MagicConduitPower;
import rs.antileaf.alice.utils.AliceSpireKit;

public class DollActAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final boolean isSpecial;
	
	public DollActAction(AbstractDoll doll, boolean isSpecial) {
		this.doll = doll;
		this.isSpecial = isSpecial;
		this.actionType = ActionTypeEnum.DOLL_OPERATE;
		this.duration = DURATION;
	}
	
	public DollActAction(AbstractDoll doll) {
		this(doll, false);
	}
	
	@Override
	public void update() {
		this.tickDuration();
		
		if (this.isDone) {
			if (DollManager.get().contains(this.doll)) {
				if (AbstractDungeon.player.hasPower(MagicConduitPower.POWER_ID) &&
						DollManager.get().getDolls().get(DollManager.MAX_DOLL_SLOTS - 1) == this.doll) {
					((MagicConduitPower)AbstractDungeon.player.getPower(MagicConduitPower.POWER_ID))
							.triggerOnDollAct();
				}
				else
					DollManager.get().dollAct(this.doll, this.isSpecial);
			}
			else
				AliceSpireKit.log(this.getClass(),
						"DollActAction.update(): DollManager does not contain " +
								this.doll.getClass().getSimpleName() + "!");
		}
	}
}
