package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DollApplyPoisonAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(DollApplyPoisonAction.class.getName());

	private static final float DURATION = 0.1F;
	private final AbstractDoll doll;
	private final int amount;
	
	public DollApplyPoisonAction(AbstractDoll doll, int amount) {
		this.doll = doll;
		this.amount = amount;
		this.actionType = ActionType.POWER;
		this.duration = this.startDuration = DURATION;
		
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.duration = this.startDuration = 0.0F;
			this.isDone = true;
		}
	}
	
	@Override
	public void update() {
		if (this.doll != null) {
			if (!DollManager.get().contains(this.doll)) {
				logger.warn("DollManager does not contain {}!", this.doll.name);
				this.isDone = true;
				return;
			}
			
			this.tickDuration();
			
			if (this.isDone)
				DollManager.get().applyPoison(this.doll, this.amount);
		}
		else
			this.isDone = true;
	}
}
