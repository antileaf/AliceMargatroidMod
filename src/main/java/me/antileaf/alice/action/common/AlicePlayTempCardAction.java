package me.antileaf.alice.action.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MasterRealityPower;

public class AlicePlayTempCardAction extends AbstractGameAction {
	private static final float DURATION = Settings.ACTION_DUR_FAST;
	private final AbstractCard card;
	private final boolean masterReality;
	
	public AlicePlayTempCardAction(AbstractCard card, boolean masterReality) {
		this.actionType = ActionType.SPECIAL;
		this.duration = DURATION;
		
		this.card = card;
		this.masterReality = masterReality;
		
		if (this.masterReality && AbstractDungeon.player.hasPower(MasterRealityPower.POWER_ID))
			this.card.upgrade();
	}
	
	public void update() {
		if (this.duration == DURATION) {
			AbstractDungeon.player.limbo.addToBottom(this.card);
			
			this.card.current_y = -200.0F * Settings.scale;
			this.card.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
			this.card.target_y = (float)Settings.HEIGHT / 2.0F;
			this.card.targetAngle = 0.0F;
			this.card.lighten(false);
			this.card.drawScale = 0.12F;
			this.card.targetDrawScale = 0.75F;
			
			AbstractMonster target = AbstractDungeon.getCurrRoom().monsters
					.getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
			
//			if (!this.card.canUse(AbstractDungeon.player, target)) {
//				if (this.card.type != AbstractCard.CardType.POWER)
//					AliceSpireKit.addToTop(new UnlimboAction(this.card));
//
//				this.isDone = true;
//				return;
//			}
			
			this.card.applyPowers();
			this.card.calculateCardDamage(target);
			
			this.card.purgeOnUse = true;
			AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(
					this.card,
					target,
					this.card.energyOnUse,
					true,
					true
			), true);
		}
		
		this.tickDuration();
	}
}
