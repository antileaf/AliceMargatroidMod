package me.antileaf.alice.action.unique;

import com.evacipated.cardcrawl.mod.stslib.actions.common.DamageCallbackAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SunlightRayAction extends AbstractGameAction {
	private final SunlightRayPostAction postAction;
	private final int amount;
	private final int count;
	
	public SunlightRayAction(SunlightRayPostAction postAction, int amount, int count) {
		this.postAction = postAction;
		this.amount = amount;
		this.count = count;
	}
	
	public void update() {
		if (!this.isDone) {
			AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);
			
			if (m != null) {
				if (this.count > 1)
					this.addToTop(new SunlightRayAction(this.postAction, this.amount, this.count - 1));
				
				this.addToTop(new DamageCallbackAction(
						m,
						new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.NORMAL),
						AbstractGameAction.AttackEffect.FIRE,
						(amount) -> {
							if (amount > 0)
								this.postAction.amount += 1;
						}
				));
			}
			
			this.isDone = true;
		}
	}
}
