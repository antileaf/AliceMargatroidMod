package rs.antileaf.alice.action.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.function.Consumer;

public class AliceFatalAction extends AbstractGameAction {
	private DamageInfo damageInfo;
	Consumer<Boolean> callback;
	
	public AliceFatalAction(AbstractCreature target, DamageInfo damageInfo, AttackEffect effect, Consumer<Boolean> callback) {
		this.damageInfo = damageInfo;
		this.setValues(target, damageInfo);
		this.actionType = ActionType.DAMAGE;
		this.attackEffect = effect;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.callback = callback;
	}
	
	public void update() {
		if (this.shouldCancelAction() && this.damageInfo.type != DamageInfo.DamageType.THORNS) {
			this.callback.accept(false);
			this.isDone = true;
		} else {
			if (this.duration == Settings.ACTION_DUR_XFAST) {
				if (this.damageInfo.type != DamageInfo.DamageType.THORNS &&
						(this.damageInfo.owner.isDying || this.damageInfo.owner.halfDead)) {
					this.isDone = true;
					this.callback.accept(false);
					return;
				}
				
				AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
			}
			
			this.tickDuration();
			
			if (this.isDone) {
				if (this.attackEffect == AttackEffect.POISON) {
					this.target.tint.color.set(Color.CHARTREUSE.cpy());
					this.target.tint.changeColor(Color.WHITE.cpy());
				} else if (this.attackEffect == AttackEffect.FIRE) {
					this.target.tint.color.set(Color.RED);
					this.target.tint.changeColor(Color.WHITE.cpy());
				}
				
				this.target.damage(this.damageInfo);
				
				if ((this.target.isDying || this.target.currentHealth <= 0) &&
						!this.target.halfDead && !this.target.hasPower("Minion"))
					this.callback.accept(true);
				else
					this.callback.accept(false);
				
				if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
					AbstractDungeon.actionManager.clearPostCombatActions();
			}
		}
	}
}
