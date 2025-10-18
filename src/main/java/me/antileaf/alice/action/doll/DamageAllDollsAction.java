package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;

public class DamageAllDollsAction extends AbstractGameAction {
	public int damage;
	public AbstractDoll except;
	public AbstractGameAction.AttackEffect effect;
	
	public DamageAllDollsAction(int damage, AbstractDoll except, AbstractGameAction.AttackEffect effect) {
		this.damage = damage;
		this.except = except;
		this.effect = effect;
		this.actionType = ActionType.DAMAGE;
		this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
	}
	
	@Override
	public void update() {
		if (this.duration == this.startDuration) {
			boolean playedSound = false;
			
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot) && doll != this.except) {
					AbstractDungeon.effectList.add(new FlashAtkImgEffect(doll.cX, doll.cY,
							this.effect, playedSound));
					
					if (!playedSound)
						playedSound = true;
				}
		}
		
		this.tickDuration();
		
		if (this.isDone) {
			for (AbstractDoll doll : DollManager.get().getDolls())
				if (!(doll instanceof EmptyDollSlot) && doll != this.except)
					DollManager.get().dollTakesDamage(doll, this.damage, false);
		}
		
		if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
			AbstractDungeon.actionManager.clearPostCombatActions();
	}
}
