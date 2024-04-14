package rs.antileaf.alice.action.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class SunlightRayPostAction extends AbstractGameAction {
	public int amount = 0;
	
	public SunlightRayPostAction() {}
	
	public void update() {
		if (!this.isDone) {
			if (this.amount > 0)
				this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
						new VigorPower(AbstractDungeon.player, this.amount)));
			
			this.isDone = true;
		}
	}
}
