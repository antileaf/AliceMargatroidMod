package me.antileaf.alice.action.doll;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import me.antileaf.alice.doll.DollDamageInfo;

public class DollDamageAction extends DamageAction {
	private final DollDamageInfo dollDamageInfo;
	
	public DollDamageAction(AbstractCreature target, DollDamageInfo info, AbstractGameAction.AttackEffect effect) {
		super(target, info, effect);
		this.dollDamageInfo = info;
		this.startDuration = this.duration;
	}
	
	public DollDamageAction(AbstractCreature target, DollDamageInfo info) {
		this(target, info, AbstractGameAction.AttackEffect.NONE);
	}
	
	@Override
	public void update() {
		if (!(this.shouldCancelAction() && this.dollDamageInfo.type != DamageInfo.DamageType.THORNS)) {
			if (this.duration == this.startDuration) {
				if (!(this.dollDamageInfo.type != DamageInfo.DamageType.THORNS &&
						(this.dollDamageInfo.owner.isDying || this.dollDamageInfo.owner.halfDead))) {
					this.dollDamageInfo.applyPowers(this.source, this.target);
				}
			}
		}
		
		super.update();
	}
}
