package me.antileaf.alice.action.unique;

import ThMod.ThMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import me.antileaf.alice.powers.unique.MarisaTempStrengthLossPower;

public class MarisaWasteBombAction extends AbstractGameAction {
    private int damage;
    private int num;
    private int stacks;
    private AbstractCreature target;
    private DamageInfo info;

    public MarisaWasteBombAction(AbstractCreature target, int dmg, int numTimes, int stacks) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.damage = dmg;
        this.target = target;
        this.num = numTimes;
        this.stacks = stacks;
        this.info = new DamageInfo(AbstractDungeon.player, this.damage, DamageType.NORMAL);
    }

    public void update() {
        if (this.target == null) {
            this.isDone = true;
        } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
        } else if (this.target == null) {
            ThMod.logger.info("WasteBombAction : error : target == null !");
            this.isDone = true;
        } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
        } else {
            if (this.target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
                float tmp = (float)this.info.output;

                for(AbstractPower p : this.target.powers) {
                    tmp = p.atDamageReceive(tmp, this.info.type);
                    if (this.info.base != (int)tmp) {
                        this.info.isModified = true;
                    }
                }

                for(AbstractPower p : this.target.powers) {
                    tmp = p.atDamageFinalReceive(tmp, this.info.type);
                    if (this.info.base != (int)tmp) {
                        this.info.isModified = true;
                    }
                }

                this.info.output = MathUtils.floor(tmp);
                if (this.info.output < 0) {
                    this.info.output = 0;
                }

                this.target.damage(this.info);
                if (!this.target.isDeadOrEscaped() && !this.target.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, AbstractDungeon.player, new MarisaTempStrengthLossPower(this.target, this.stacks), this.stacks));
                }

                if (this.num > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    --this.num;
                    AbstractDungeon.actionManager.addToTop(new MarisaWasteBombAction(AbstractDungeon.getMonsters().getRandomMonster(true), this.damage, this.num, this.stacks));
                }
            }

            AbstractDungeon.actionManager.addToTop(new WaitAction(0.2F));
            this.isDone = true;
        }
    }
}
