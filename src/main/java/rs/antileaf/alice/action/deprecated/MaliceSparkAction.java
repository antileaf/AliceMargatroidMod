package rs.antileaf.alice.action.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.powers.unique.MaliceSparkPower;

@Deprecated
public class MaliceSparkAction extends AbstractGameAction {
	private final int amount;
	private final DiscardAction discardAction;
	
	public MaliceSparkAction(int amount) {
		this.amount = amount;
		this.discardAction = null;
	}
	
	public MaliceSparkAction(int amount, DiscardAction discardAction) {
		this.amount = amount;
		this.discardAction = discardAction;
	}
	
	public void update() {
		if (!this.isDone) {
			if (this.discardAction != null && !this.discardAction.isDone)
				this.addToBot(new MaliceSparkAction(this.amount, this.discardAction));
			
			else {
				if (AbstractDungeon.player.hand.size() > this.amount) {
					DiscardAction discardAction = new DiscardAction(
							AbstractDungeon.player,
							AbstractDungeon.player,
							AbstractDungeon.player.hand.size() - this.amount,
							false,
							false
					);
					
					this.addToBot(new MaliceSparkAction(this.amount, discardAction));
				}
				else {
					this.addToBot(new ApplyPowerAction(
							AbstractDungeon.player,
							AbstractDungeon.player,
							new MaliceSparkPower(this.amount),
							this.amount
					));
					
					if (AbstractDungeon.player.hand.size() < this.amount)
						this.addToBot(new DrawCardAction(this.amount - AbstractDungeon.player.hand.size()));
				}
			}
			
			this.isDone = true;
		}
	}
}
