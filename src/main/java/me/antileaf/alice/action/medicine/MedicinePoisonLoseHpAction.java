//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.antileaf.alice.action.medicine;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import me.antileaf.alice.action.doll.DollPoisonLoseHpAction;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.powers.medicine.MedicinePoisonPower;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.MedicineHelper;

public class MedicinePoisonLoseHpAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public MedicinePoisonLoseHpAction(AbstractCreature target, AbstractCreature source, int amount, AbstractGameAction.AttackEffect effect) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.33F;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != RoomPhase.COMBAT) {
            this.isDone = true;
        } else {
            if (this.duration == 0.33F && this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.target.currentHealth > 0) {
                    this.target.tint.color = Color.CHARTREUSE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
					
                    this.target.damage(new DamageInfo(this.source, this.amount,
							MedicineHelper.isVenom() ? DamageType.HP_LOSS : DamageType.THORNS));
                }

                AbstractPower p = this.target.getPower(MedicinePoisonPower.POWER_ID);
                if (p != null) {
                    --p.amount;
                    if (p.amount == 0) {
                        this.target.powers.remove(p);
                    } else {
                        p.updateDescription();
                    }
                }
				
				for (AbstractDoll doll : DollManager.get().getDolls())
					if (!(doll instanceof EmptyDollSlot) && doll.poison > 0)
						AliceHelper.addActionToBuffer(new DollPoisonLoseHpAction(doll));
				
				AliceHelper.addActionToBuffer(new WaitAction(0.1F));
				AliceHelper.commitBuffer();

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }

        }
    }
}
