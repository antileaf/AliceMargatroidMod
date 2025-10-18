//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DollPoisonLoseHpAction extends AbstractGameAction {
	private static final Logger logger = LogManager.getLogger(DollPoisonLoseHpAction.class);
	
	private AbstractDoll doll;

    public DollPoisonLoseHpAction(AbstractDoll doll) {
        this.actionType = ActionType.DAMAGE;
		this.doll = doll;
        this.duration = this.startDuration = 0.2F;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
				if (DollManager.get().contains(this.doll))
					DollManager.get().poisonLoseHp(this.doll);
				else
					logger.warn("DollManager does not contain {}!", this.doll.name);
			}
        }
    }
}
