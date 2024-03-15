package rs.antileaf.alice.action.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import rs.antileaf.alice.powers.unique.KirisameMahoutenPower;

public class KirisameMahoutenAction extends AbstractGameAction {
	private static final float DURATION = 0.1F;
	
	String potionName;
	
	public KirisameMahoutenAction(String potionName) {
		this.potionName = potionName;
		this.duration = DURATION;
	}
	
	@Override
	public void update() {
		if (this.duration == DURATION) {
			AbstractPlayer p = AbstractDungeon.player;
			
			if (p.hasPower(KirisameMahoutenPower.POWER_ID))
				((KirisameMahoutenPower) p.getPower(KirisameMahoutenPower.POWER_ID)).addPotion(potionName);
			else {
				this.addToTop(new ApplyPowerAction(p, p, new KirisameMahoutenPower(potionName)));
				this.isDone = true;
			}
		}
		
		if (!this.isDone)
			this.tickDuration();
	}
}
